package demo.websocket

import grails.transaction.Transactional
import groovy.util.logging.Slf4j
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.GenericMessage

@Transactional
@Slf4j
class HelloService {

    MessageChannel clientInboundChannel

    def hello() {
        log.info("sending hello...")
        String text = "service!"

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.MESSAGE)
        headerAccessor.sessionId = '999' // dummy id
        headerAccessor.sessionAttributes = [:]
        headerAccessor.destination = '/app/hello'
        Message msg = new GenericMessage(text, headerAccessor.messageHeaders)
        clientInboundChannel.send(msg)
        msg
    }
}
