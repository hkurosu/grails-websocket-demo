import demo.websocket.config.WebSocketMessageBrokerConfigurer
import grails.plugin.springwebsocket.ConfigUtils
import grails.plugin.springwebsocket.GrailsSimpAnnotationMethodMessageHandler
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter

// Place your Spring DSL code here
beans = {
    def config = ConfigUtils.getSpringWebsocketConfig application

    grailsSimpAnnotationMethodMessageHandler(
            GrailsSimpAnnotationMethodMessageHandler,
            ref("clientInboundChannel"),
            ref("clientOutboundChannel"),
            ref("brokerMessagingTemplate")
    ) {
        destinationPrefixes = config.messageBroker.applicationDestinationPrefixes
    }

    webSocketMessageBrokerConfigurer(WebSocketMessageBrokerConfigurer, config) {}
}
