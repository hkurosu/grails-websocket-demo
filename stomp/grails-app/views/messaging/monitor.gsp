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
        <g:if test="${config?.useSockJs}">
        // toggle socket to switch between SockJS and native WebSocket
        var socket = new SockJS("${createLink(uri: '/broker')}");
        </g:if>
        <g:else>
        var socket = new WebSocket("ws://localhost:8080/stomp/broker");
        </g:else>
        var client = Stomp.over(socket);

        var monitoring = "/notify/*";

        var subscribeCallback = function(message) {
            $('#messageTable tr:last').after('<tr><td>'+message.headers['message-id']+'</td><td>'+message.headers.destination+'</td></tr>')
            message.ack();
        }

        client.connect({}, function() {
            $("#messageDiv").text("Monitoring '" + monitoring + "'");
            client.subscribe("/topic" + monitoring, subscribeCallback, {ack: 'client'});
        });
    });
    </script>

    <style>
    table, th, td {
        border: 1px solid black;
        border-collapse: collapse;
    }
    #messageDiv {
        margin: auto;
        font-size: 200%;
        text-align: center;
        width: 100%;
    }
    </style>
</head>

<body style="margin: 0">
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript!
WebSocket relies on Javascript being enabled. Please enable Javascript and reload this page!</h2></noscript>
<div id="messageDiv">Starting...</div>
<table id="messageTable" style="width:100%">
    <tr><th style="width:20%;">ID</th><th>Destination</th></tr>
</table>
</body>
</html>