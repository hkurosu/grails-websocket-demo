class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        grailsApplication.mainContext?.webSocketHandlerMapping?.alwaysUseFullPath = true
        grailsApplication.mainContext?.integrationWebSocketHandlerMapping?.alwaysUseFullPath = true
    }
    def destroy = {
    }
}
