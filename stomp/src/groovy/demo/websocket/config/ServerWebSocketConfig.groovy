package demo.websocket.config

import grails.plugin.springwebsocket.GrailsSimpAnnotationMethodMessageHandler
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration

/**
 * Inject Server-side WebSocket spring beans.
 */
@Configuration
class ServerWebSocketConfig extends DelegatingWebSocketMessageBrokerConfiguration {

    final ConfigObject config

    @Autowired
    GrailsApplication grailsApplication

    ServerWebSocketConfig(ConfigObject config) {
        this.config = config
        configurers = [ new ServerWebSocketConfigurer(config) ]
    }

    @Override
    SimpAnnotationMethodMessageHandler simpAnnotationMethodMessageHandler() {
        GrailsSimpAnnotationMethodMessageHandler handler = new GrailsSimpAnnotationMethodMessageHandler(
                clientInboundChannel(), clientOutboundChannel(), brokerMessagingTemplate())
        handler.grailsApplication = grailsApplication

        handler.destinationPrefixes = config.messageBroker.applicationDestinationPrefixes as List
        handler.messageConverter = brokerMessageConverter()
        handler.validator = simpValidator()

        List<HandlerMethodArgumentResolver> argumentResolvers = []
        addArgumentResolvers(argumentResolvers)
        handler.customArgumentResolvers = argumentResolvers

        List<HandlerMethodReturnValueHandler> returnValueHandlers = []
        addReturnValueHandlers(returnValueHandlers)
        handler.customReturnValueHandlers = returnValueHandlers

        handler
    }
}
