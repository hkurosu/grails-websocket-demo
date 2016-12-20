import demo.spring.integration.websocket.config.ApplicationConfig
import demo.spring.integration.websocket.config.ClientWebSocketConfig
import demo.spring.integration.websocket.config.ServerWebSocketConfig
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter

// Place your Spring DSL code here
beans = {
    configureWebSocket.delegate = delegate
    configureWebSocket()

    applicationConfig(ApplicationConfig) {}
}

configureWebSocket = {

    httpRequestHandlerAdapter(HttpRequestHandlerAdapter) {}

    // server web-socket container
    serverWebSocketConfig(ServerWebSocketConfig)

    // client
    clientWebSocketConfig(ClientWebSocketConfig)
}
