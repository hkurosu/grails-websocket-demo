package demo.websocket

import groovy.util.logging.Slf4j
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.simp.annotation.SubscribeMapping

@Slf4j
class HealthController {

    def index() {
        log.debug("HTTP /health")
        render(text: 'OK', contentType: 'text/plain', encoding: 'UTF-8')
    }

    @MessageMapping('/health')
    @SendToUser
    protected String handleMessage() {
        log.debug("STOMP /health")
        'OK'
    }
}
