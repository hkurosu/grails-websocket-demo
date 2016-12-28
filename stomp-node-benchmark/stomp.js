var argv = require('minimist')(process.argv.slice(2), {default: {n:100,m:10}});
var repeat = argv.n;
var limit = argv.m;

var Stomp = require('stompjs');
var client = Stomp.overWS("ws://localhost:8080/stomp/broker");

if (argv.d) {
    client.debug = function (message) {
        console.log(message);
    }
}

var Promise = require('promise');

var connect = new Promise(function(resolve, reject) {
    client.connect({}, resolve, reject);
});

var count = 0;
var received = 0;

var subscribe = function() {
    client.subscribe('/user/queue/health', function (message) {
        // console.log('Received: ' + message.body);
        ++received;
        --count;
        // message.ack();
    }, {ack: 'client'});
    // console.log('Subscribing /user/queue/health');
};

var startTime;

var send = function(n) {
    startTime = new Date();
    console.log('[' + startTime.toISOString() + '] sending ' + n + ' times.');
    for (var i = 0; i < n; ++i) {
        client.send('/app/health', {}, 'Hello');
        // console.log('Sent Hello');
        ++count;
    }
}

var receive = function(startTime) {
    (function check() {
        var now = new Date();
        if (count <= 0 || startTime.getTime() + limit * 1000 < now.getTime()) {
            var total = now.getTime() - startTime.getTime();
            console.log('[' + now.toISOString() + '] '
                + received + ' received ('
                + 'total: ' + total + ' ms.'
                + ', avg: ' + total / repeat + ' ms.)');
            client.disconnect();
        } else {
            setImmediate(check);
        }
    })();
}

connect                  // connect()
    .then(function() {   // subscribe()
        subscribe(); 
    }).then(function() { // send() messages
        send(repeat);
    }).then(function() { // wait for all subscription messages
        receive(startTime);
    }
);







