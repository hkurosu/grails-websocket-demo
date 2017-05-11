# grails-websocket-demo
Demo application of Grails + Spring websocket
# Modules
## stomp
Simple Grails (2.5.3) application integrated with the Spring Websocket Message Broker (w/ spring-websoket 4.1.8).

The demo extends spring-websocket plugin to be able to turn ON/OFF SockJS features by configuration.

To turn OFF SockJS,
 1. set "useSockJs" to false

 
    grails {
        plugin {
        springwebsocket {
            useSockJs = false
        }
    }

 2. Uncomment these two lines in resources.groovy
 
 
     // def config = ConfigUtils.getSpringWebsocketConfig application
     // serverWebSocketConfig(ServerWebSocketConfig, config) {}

## benckmark
Websocket benchmarking tools written by Node.js.

## reverse-proxy/nging
A simple reverse proxy / load balancing configuration for NGINX.
