package demo.spring.integration.websocket.config

import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.websocket.ServerWebSocketContainer
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler
import org.springframework.messaging.MessageHandler

@CompileStatic
@Configuration
@EnableIntegration
class ServerWebSocketConfig {

    @Bean
    ServerWebSocketContainer serverWebSocketContainer() {
        return new ServerWebSocketContainer("/time").withSockJs()
    }

    @Bean
    WebSocketOutboundMessageHandler webSocketOutboundAdapter() {
        new WebSocketOutboundMessageHandler(serverWebSocketContainer())
    }
}
