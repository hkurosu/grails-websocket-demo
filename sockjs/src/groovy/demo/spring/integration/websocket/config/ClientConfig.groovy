package demo.spring.integration.websocket.config

import groovy.transform.CompileStatic
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

@CompileStatic
@Configuration
class ClientConfig {

    @Autowired
    ClientWebSocketContainer clientWebSocketContainer

    @Bean
    MessageChannel clientWebSocketInputChannel() {
        new DirectChannel()
    }

    @Bean
    @ServiceActivator(inputChannel = "clientWebSocketInputChannel")
    MessageHandler clientLoggingChannelAdapter() {
        LoggingHandler loggingHandler = new LoggingHandler("info")
        loggingHandler.logExpressionString = "'The WebSocketClient received: ' + payload"
        loggingHandler
    }

    @Bean
    WebSocketInboundChannelAdapter webSocketInboundChannelAdapter() {
        WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(clientWebSocketContainer)
        adapter.outputChannel = clientWebSocketInputChannel()
        adapter.autoStartup = false
        adapter
    }
}
