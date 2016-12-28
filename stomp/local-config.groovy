grails {
    plugin {
        springwebsocket {
            useCustomConfig = true
            dispatcherServlet.additionalMappings = ["/broker/*"]
            stompEndpoints = [["/broker"]]
            messageBroker.useSockJs = false
            clientInboundChannel.threadPoolSize = 4..1000
            clientOutboundChannel.threadPoolSize = 4..1000
        }
    }
}

log4j = log4j << {
//    debug 'org.springframework.messaging'
//    debug 'org.springframework.web'
//    trace 'org.springframework.web.socket'
//    trace 'org.springframework.web.socket.handler.LoggingWebSocketHandlerDecorator'

    trace 'org.springframework.web.socket.messaging.StompSubProtocolHandler'

    debug 'demo.websocket.HealthController'
}