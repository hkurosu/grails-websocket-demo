<!DOCTYPE html>
<%@ page import="grails.plugin.springwebsocket.ConfigUtils" %>
<%
    def config = ConfigUtils.getSpringWebsocketConfig(grailsApplication)
%>
<html>
<head>
    <title>Monitoring Messages</title>

    <asset:javascript src="jquery" />
    <asset:javascript src="spring-websocket" />

    <script type="text/javascript">
    $(function() {
        <g:if test="${config.messageBroker.useSockJs}">
        // toggle socket to switch between SockJS and native WebSocket
        var socket = new SockJS("${createLink(uri: '/broker')}");
        </g:if>
        <g:else>
        var socket = new WebSocket("ws://localhost:8080/stomp/broker");
        </g:else>
        var client = Stomp.over(socket);

        var monitoring = "/notify/*";

        var subscribeCallback = function(message) {
            $("#messageDiv").text(message.headers.destination);
            message.ack();
        }

        client.connect({}, function() {
            $("#messageDiv").text("Monitoring '" + monitoring + "' ...");
            client.subscribe("/topic" + monitoring, subscribeCallback, {ack: 'client'});
        });
    });
    </script>
</head>

<body style="margin: 0">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript!
WebSocket relies on Javascript being enabled. Please enable Javascript and reload this page!</h2></noscript>
<div id="messageDiv"
     style="position: absolute;
     bottom: 0;
     font-size: 200%;
     height: 200px;
     margin: auto;
     text-align: center;
     top: 0;
     width: 100%;">Starting...</div>
</body>
</html>