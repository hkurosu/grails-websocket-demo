import demo.websocket.config.WebSocketMessageBrokerConfigurer
import grails.plugin.springwebsocket.ConfigUtils
import grails.plugin.springwebsocket.GrailsSimpAnnotationMethodMessageHandler
import org.springframework.jmx.export.MBeanExporter

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

    // enable util namespace
    xmlns util: "http://www.springframework.org/schema/util"

    // enable JMX beans
    mbeanExporer(MBeanExporter) { bean ->
        beans = ref('mbeanMap')
    }
    util.map(id: 'mbeanMap') {
        entry(key: 'WebSocket:name=messageBrokerStats', 'value-ref': 'webSocketMessageBrokerStats')
    }
}
