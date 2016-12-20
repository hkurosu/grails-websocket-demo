package demo.spring.integration.websocket.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.websocket.ClientWebSocketContainer
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport

@Configuration
@EnableIntegration
class ClientWebSocketConfig {

    private SockJsClient sockJsClient() {
        List<Transport> transports = []
        transports << new WebSocketTransport(new StandardWebSocketClient())
        transports << new RestTemplateXhrTransport()
        new SockJsClient(transports)
    }

    private WebSocketClient standardWebSocketClient() {
        new StandardWebSocketClient()
    }

    @Bean
    WebSocketClient webSocketClient() {
        sockJsClient()
    }

    @Bean
    ClientWebSocketContainer clientWebSocketContainer() {
        new ClientWebSocketContainer(webSocketClient(), 'ws://localhost:8080/time/websocket')
    }
}
