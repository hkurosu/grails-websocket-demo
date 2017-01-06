grails {
    plugin {
        springwebsocket {
            useCustomConfig = true
            dispatcherServlet.additionalMappings = ["/broker/*"]
            stompEndpoints = [["/broker"]]
            clientInboundChannel.threadPoolSize = 100..1000
            clientOutboundChannel.threadPoolSize = 100..1000
            useSockJs = false
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

    debug 'demo.websocket.HealthController'
}