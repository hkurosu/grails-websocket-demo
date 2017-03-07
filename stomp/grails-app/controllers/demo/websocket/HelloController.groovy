package demo.websocket

import groovy.util.logging.Slf4j
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.simp.annotation.SubscribeMapping

@Slf4j
class HelloController {

    def helloService

    def index() {
    }

    def echo() {
        render helloService.hello()
    }

    @SubscribeMapping("/hello")
    @SendToUser
    protected String hello(String world) {
        log.info("receiving hello...")
        return "hello from controller, ${world}!"
    }

}
