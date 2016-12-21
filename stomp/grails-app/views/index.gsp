<%@ page import="grails.plugin.springwebsocket.ConfigUtils" %>
<%
    def config = ConfigUtils.getSpringWebsocketConfig(grailsApplication)
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>

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

            client.connect({}, function() {
                client.subscribe("/topic/hello", function(message) {
                    $("#helloDiv").text(message.body);
                });
            });

            $("#helloButton").click(function() {
                client.send("/app/hello", {}, JSON.stringify("world"));
//                client.send("/topic/hello", {}, JSON.stringify("world"));
            });
        });
    </script>
</head>
<body>
<button id="helloButton">hello</button>
<div id="helloDiv"></div>
</body>
</html>
