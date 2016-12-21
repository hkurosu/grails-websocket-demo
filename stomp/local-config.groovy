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
