grails {
    plugin {
        springwebsocket {
            dispatcherServlet.additionalMappings = ["/broker/*"]
            stompEndpoints = [["/broker"]]
        }
    }
}
