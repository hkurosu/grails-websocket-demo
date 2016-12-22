package demo.websocket

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.GenericMessage

class MessagingController {

    MessageChannel clientInboundChannel

    def index() {}

    def monitor() {}

    def send() {
        sendInternal(UUID.randomUUID())
        render 'OK'
    }

    def sendNoId() {
        sendInternal()
        render 'OK'
    }

    def send100() {
        (1..100).each {
            sendInternal(UUID.randomUUID())
        }
        render 'OK'
    }

    void sendInternal(def id = null) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.MESSAGE)
        headerAccessor.sessionId = id
        headerAccessor.sessionAttributes = [:]
        headerAccessor.destination = '/app/notify' + (id ? "/$id" : '')
        Message msg = new GenericMessage('', headerAccessor.messageHeaders)
        clientInboundChannel.send(msg)
    }
}