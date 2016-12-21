package demo.websocket

class MessagesController {

    def brokerMessagingTemplate

    def index() {}

    def monitor() {}

    def send() {
        brokerMessagingTemplate.convertAndSend("/topic/notify/" + UUID.randomUUID(), '')
        render 'OK'
    }

    def sendNoId() {
        brokerMessagingTemplate.convertAndSend("/topic/notify", '')
        render 'OK'
    }

    def send100() {
        (1..100).each {
            brokerMessagingTemplate.convertAndSend("/topic/notify/" + UUID.randomUUID(), '')
        }
        render 'OK'
    }
}