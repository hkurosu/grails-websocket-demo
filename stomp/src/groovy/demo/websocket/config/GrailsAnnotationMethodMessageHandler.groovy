package demo.websocket.config

import groovy.transform.CompileStatic
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler

/**
 * GRAILS extension of SimpAnnotationMethodMessageHandler.
 *
 * Supports Spring messaging annotations (e.g. @MessageMapping) in Grails Controllers (under grails-app/controllers).
 */
@CompileStatic
class GrailsAnnotationMethodMessageHandler extends SimpAnnotationMethodMessageHandler {

	GrailsApplication grailsApplication

    GrailsAnnotationMethodMessageHandler(SubscribableChannel clientInboundChannel,
                                         MessageChannel clientOutboundChannel, SimpMessageSendingOperations brokerTemplate) {
		super(clientInboundChannel, clientOutboundChannel, brokerTemplate)
	}
	
	@Autowired
	@Override
	@Qualifier("brokerMessageConverter")
	void setMessageConverter(MessageConverter messageConverter) {
		super.setMessageConverter messageConverter
	}
	
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return grailsApplication.isArtefactOfType(ControllerArtefactHandler.TYPE, beanType)
	}
	
}
