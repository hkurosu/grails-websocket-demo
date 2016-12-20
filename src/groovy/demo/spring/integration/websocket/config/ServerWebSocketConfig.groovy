package demo.spring.integration.websocket.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.websocket.ServerWebSocketContainer

@Configuration
@EnableIntegration
class ServerWebSocketConfig {

    @Bean
    ServerWebSocketContainer serverWebSocketContainer() {
        return new ServerWebSocketContainer("/time").withSockJs()
    }

}
