package demo.spring.integration.websocket.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.annotation.Poller
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.annotation.Transformer
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.channel.ExecutorChannel
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.MessageSource
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.splitter.DefaultMessageSplitter
import org.springframework.integration.transformer.AbstractPayloadTransformer
import org.springframework.integration.transformer.HeaderEnricher
import org.springframework.integration.transformer.support.ExpressionEvaluatingHeaderValueMessageProcessor
import org.springframework.integration.websocket.ServerWebSocketContainer
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.support.GenericMessage

import java.text.DateFormat
import java.util.concurrent.Executors

@Configuration
class ApplicationConfig {

    @Autowired
    ServerWebSocketContainer serverWebSocketContainer

    @Bean
    @InboundChannelAdapter(value = "splitChannel", poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
    MessageSource<?> webSocketSessionsMessageSource() {
        return new MessageSource<Iterator<String>>() {

            @Override
            public Message<Iterator<String>> receive() {
                return new GenericMessage<Iterator<String>>(serverWebSocketContainer.getSessions().keySet().iterator());
            }

        }
    }

    @Bean
    MessageChannel splitChannel() {
        new DirectChannel()
    }

    @Bean
    @ServiceActivator(inputChannel = "splitChannel")
    MessageHandler splitter() {
        DefaultMessageSplitter splitter = new DefaultMessageSplitter()
        splitter.setOutputChannelName("headerEnricherChannel")
        splitter
    }

    @Bean
    MessageChannel headerEnricherChannel() {
        new ExecutorChannel(Executors.newCachedThreadPool())
    }

    @Bean
    @Transformer(inputChannel = "headerEnricherChannel", outputChannel = "transformChannel")
    HeaderEnricher headerEnricher() {
        new HeaderEnricher(Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER,
                new ExpressionEvaluatingHeaderValueMessageProcessor<Object>("payload", null)))
    }

    @Bean
    @Transformer(inputChannel = "transformChannel", outputChannel = "sendTimeChannel")
    AbstractPayloadTransformer<?, ?> transformer() {
        return new AbstractPayloadTransformer<Object, Object>() {
            @Override
            protected Object transformPayload(Object payload) throws Exception {
                return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT).format(new Date())
            }
        }
    }


    @Bean
    MessageChannel sendTimeChannel() {
        new PublishSubscribeChannel()
    }


    @Bean
    @ServiceActivator(inputChannel = "sendTimeChannel")
    MessageHandler webSocketOutboundAdapter() {
        new WebSocketOutboundMessageHandler(serverWebSocketContainer)
    }

    @Bean
    @ServiceActivator(inputChannel = "sendTimeChannel")
    MessageHandler loggingChannelAdapter() {
        LoggingHandler loggingHandler = new LoggingHandler("info")
        loggingHandler.setExpression("'The time ' + payload + ' has been sent to the WebSocketSession ' + headers.simpSessionId")
        loggingHandler
    }

}
