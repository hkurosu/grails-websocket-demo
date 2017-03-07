grails {
    plugin {
        springwebsocket {
            useCustomConfig = true
            dispatcherServlet.additionalMappings = ["/broker/*"]
            stompEndpoint = '/broker'
            useSockJs = true
            sockJs {
//                webSocketEnabled = false
//                heartbeatTime = 1000
            }
        }
    }
}

log4j = log4j << {
//    debug 'org.springframework.messaging'
//    debug 'org.springframework.web'
//    trace 'org.springframework.web.socket'
//    trace 'org.springframework.web.socket.handler.LoggingWebSocketHandlerDecorator'

    trace 'org.springframework.web.socket.messaging.StompSubProtocolHandler'
    trace 'org.springframework.web.socket.server.support.DefaultHandshakeHandler'
    trace 'org.springframework.web.socket.sockjs.transport'

    trace 'org.springframework.messaging'

    debug 'demo.websocket.HealthController'
}