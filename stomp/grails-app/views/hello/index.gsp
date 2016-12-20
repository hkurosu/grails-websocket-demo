<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>

    <asset:javascript src="jquery" />
    <asset:javascript src="spring-websocket" />

    <script type="text/javascript">
        $(function() {
            var socket = new SockJS("${createLink(uri: '/stomp')}");
//            var socket = new WebSocket("ws://localhost:8080/stomp/stomp");
            var client = Stomp.over(socket);

            client.connect({}, function() {
                client.subscribe("/topic/hello", function(message) {
//                    $("#helloDiv").append(message.body);
                    $("#helloDiv").text(message.body);
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
