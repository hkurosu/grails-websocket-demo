package demo.websocket

import grails.transaction.Transactional
import groovy.util.logging.Slf4j

@Transactional
@Slf4j
class HelloService {

    def brokerMessagingTemplate

    def hello() {
        log.info("sending hello...")
        String msg = "hello from service!"
        brokerMessagingTemplate.convertAndSend "/topic/hello", msg
        msg
    }
}
