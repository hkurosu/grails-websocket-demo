var argv = require('minimist')(process.argv.slice(2), {default: {n:100,t:10}});
var requests = argv.n;
var timeLimit = argv.t;

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

var running = 0;
var received = 0;

var subscribe = function() {
    client.subscribe('/user/queue/health', function (message) {
        // console.log('Received: ' + message.body);
        ++received;
        --running;
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
        ++running;
    }
};

var receive = function(startTime) {
    (function check() {
        var now = new Date();
        if (running <= 0 || startTime.getTime() + timeLimit * 1000 < now.getTime()) {
            require('./utils.js').logResult(startTime, now, requests, received);
            client.disconnect();
        } else {
            setImmediate(check);
        }
    })();
};

connect                  // connect()
    .then(function() {   // subscribe()
        subscribe(); 
    }).then(function() { // send() messages
        send(requests);
    }).then(function() { // wait for all subscription messages
        receive(startTime);
    }
);







