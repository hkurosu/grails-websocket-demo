<%@ page import="grails.plugin.springwebsocket.ConfigUtils" %>
<%
    def config = ConfigUtils.getSpringWebsocketConfig(grailsApplication)
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>

    <asset:javascript src="jquery" />
    <asset:javascript src="sockjs-1.1.1" />
    <asset:javascript src="stomp" />

    <script type="text/javascript">
        $(function() {
            <g:if test="${config?.useSockJs}">
            // toggle socket to switch between SockJS and native WebSocket
            var socket = new SockJS("${createLink(uri: '/broker')}");
            console.log('SockJS version: ' + SockJS.version);
            </g:if>
            <g:else>
            var socket = new WebSocket("ws://${request.serverName}:${request.serverPort}${request.contextPath}/broker");
            </g:else>
            var client = Stomp.over(socket);

            client.connect({}, function() {
                client.subscribe("/user/queue/hello", function(message) {
                    $("#helloDiv").text(message.body);
                    message.ack();
                });
            });

            $("#helloButton").click(function() {
                client.send("/app/hello", {}, JSON.stringify("world"));
            });
        });
    </script>
</head>
<body>
<button id="helloButton">hello</button>
<div id="helloDiv"></div>
</body>
</html>
