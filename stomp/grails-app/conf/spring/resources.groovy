import demo.websocket.config.ServerWebSocketConfig
import grails.plugin.springwebsocket.ConfigUtils
import org.springframework.jmx.export.MBeanExporter

// Place your Spring DSL code here
beans = {

    // uncomment to use custom config
    // def config = ConfigUtils.getSpringWebsocketConfig application
    // serverWebSocketConfig(ServerWebSocketConfig, config) {}

    //
    // Optional - to support JMX beans
    //

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
