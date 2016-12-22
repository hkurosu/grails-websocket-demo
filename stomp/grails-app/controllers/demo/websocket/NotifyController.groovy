package demo.websocket

import groovy.util.logging.Slf4j
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping

@Slf4j
class NotifyController {

    @MessageMapping("/notify/{id}")
    protected String handleMessage(@DestinationVariable String id) {
        log.info("handling notification: $id")
        id
    }
}
