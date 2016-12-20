package demo.spring.integration.websocket

class ClientController {

    def webSocketInboundChannelAdapter

    def index() { }

    def start() {
        webSocketInboundChannelAdapter?.start()
        render 'OK'
    }
}
