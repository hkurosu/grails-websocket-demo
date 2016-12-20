package demo.websocket

import groovy.util.logging.Slf4j
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

@Slf4j
class HelloController {

    def helloService

    def index() {
    }

    def echo() {
        render helloService.hello()
    }

    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    protected String hello(String world) {
        log.info("receiving hello...")
        return "hello from controller, ${world}!"
    }

}
