package demo.websocket.config

import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration
import org.springframework.web.util.UrlPathHelper

/**
 * Implements {@code WebSocketMessageBrokerConfigurer} to help Server-side WebSocket bean injection.
 */
class ServerWebSocketConfigurer extends AbstractWebSocketMessageBrokerConfigurer {

    ConfigObject config

    /**
     *  using constructor-based injection here because the overridden configuration methods
     *  are called (it seems) before property injection or @PostConstruct handling take place
     */
    ServerWebSocketConfigurer(ConfigObject config) {
        assert config
        this.config = config
    }

    @Override
    void configureClientInboundChannel(ChannelRegistration cr) {
        def poolSizeCore = config.clientInboundChannel.threadPoolSize.from
        def poolSizeMax = config.clientInboundChannel.threadPoolSize.to
        cr.taskExecutor().corePoolSize(poolSizeCore).maxPoolSize(poolSizeMax)
    }

    @Override
    void configureClientOutboundChannel(ChannelRegistration cr) {
        def poolSizeCore = config.clientOutboundChannel.threadPoolSize.from
        def poolSizeMax = config.clientOutboundChannel.threadPoolSize.to
        cr.taskExecutor().corePoolSize(poolSizeCore).maxPoolSize(poolSizeMax)
    }

    @Override
    void configureMessageBroker(MessageBrokerRegistry mbr) {
        String[] brokerPrefixes = config.messageBroker.brokerPrefixes
        mbr.enableSimpleBroker(brokerPrefixes)

        String[] applicationDestinationPrefixes = config.messageBroker.applicationDestinationPrefixes
        mbr.setApplicationDestinationPrefixes(applicationDestinationPrefixes)

        String userDestinationPrefix = config.messageBroker.userDestinationPrefix
        mbr.userDestinationPrefix = userDestinationPrefix

        mbr
    }

    @Override
    void registerStompEndpoints(StompEndpointRegistry ser) {
        // add custom urlPathHeader to registry
        UrlPathHelper urlPathHelper = new UrlPathHelper()
        urlPathHelper.alwaysUseFullPath = true
        ser.urlPathHelper = urlPathHelper
        // add endpoint
        StompWebSocketEndpointRegistration registration = ser.addEndpoint(config.stompEndpoint as String)
        // add origin
        if (config.allowedOrigins) {
            registration.allowedOrigins = config.allowedOrigins
        }
        if (config?.useSockJs) {
            SockJsServiceRegistration service = registration.withSockJS()
            if (config?.sockJs?.webSocketEnabled instanceof Boolean) {
                service.webSocketEnabled = config.sockJs.webSocketEnabled
            }
            if (config?.sockJs?.heartbeatTime) {
                service.heartbeatTime = config.sockJs.heartbeatTime
            }
            if (config?.sockJs?.disconnectDelay) {
                service.disconnectDelay = config.sockJs.disconnectDelay
            }
        }
    }

}
