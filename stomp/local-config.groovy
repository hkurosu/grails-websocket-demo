grails {
    plugin {
        springwebsocket {
            useCustomConfig = true
            dispatcherServlet.additionalMappings = ["/broker/*"]
            stompEndpoints = [["/broker"]]
            messageBroker.useSockJs = false
        }
    }
}

log4j = log4j << {
    trace 'org.springframework.messaging'
    debug 'org.springframework.web'
    trace 'org.springframework.web.socket'
    trace 'org.springframework.web.socket.handler.LoggingWebSocketHandlerDecorator'
}