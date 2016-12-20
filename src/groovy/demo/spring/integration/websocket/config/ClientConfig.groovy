package demo.spring.integration.websocket.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.websocket.ClientWebSocketContainer
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter

@Configuration
class ClientConfig {

    @Autowired
    ClientWebSocketContainer clientWebSocketContainer

    @Bean
    InboundChannelAdapter websocketInboundChannelAdapter() {
        new WebSocketInboundChannelAdapter(clientWebSocketContainer)
    }
}
