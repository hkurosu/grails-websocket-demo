package demo.spring.integration.websocket.config

import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.websocket.ClientWebSocketContainer
import org.springframework.integration.websocket.inbound.WebSocketInboundChannelAdapter
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler
import org.springframework.messaging.MessageChannel
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport

@CompileStatic
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
        new ClientWebSocketContainer(webSocketClient(), 'ws://localhost:8080/sockjs/time')
    }

    @Bean
    MessageChannel clientWebSocketInputChannel() {
        new DirectChannel()
    }

    @Bean
    WebSocketInboundChannelAdapter clientWebSocketInboundChannelAdapter() {
        WebSocketInboundChannelAdapter adapter = new WebSocketInboundChannelAdapter(clientWebSocketContainer())
        adapter.outputChannel = clientWebSocketInputChannel()
        adapter.autoStartup = false
        adapter
    }

    @Bean
    WebSocketOutboundMessageHandler clientWebSocketOutboundAdapter() {
        new WebSocketOutboundMessageHandler(clientWebSocketContainer())
    }
}
