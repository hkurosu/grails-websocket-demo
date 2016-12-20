package demo.spring.integration.websocket.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.websocket.ClientWebSocketContainer
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler

@Configuration
class ClientConfig {

    @Autowired
    ClientWebSocketContainer clientWebSocketContainer

    @Bean
    MessageChannel webSocketInputChannel() {
        new DirectChannel()
    }

    @Bean
    @ServiceActivator(inputChannel = "webSocketInputChannel")
    MessageHandler clientLoggingChannelAdapter() {
        LoggingHandler loggingHandler = new LoggingHandler("info")
        loggingHandler.setExpression("'The time ' + payload + ' has been sent to the WebSocketSession ' + headers.simpSessionId")
        loggingHandler
    }

    @Bean
    @ServiceActivator
    WebSocketInboundChannelAdapter websocketInboundChannelAdapter() {
        WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(clientWebSocketContainer)
        adapter.outputChannel = webSocketInputChannel()
        adapter
    }
}
