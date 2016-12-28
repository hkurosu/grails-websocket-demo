var utils = require('./utils.js');
var args = utils.parseArgs();

var Stomp = require('stompjs');
var client = Stomp.overWS("ws://localhost:8080/stomp/broker");

if (args.debug) {
    client.debug = function (message) {
        console.log(message);
    }
}

var Promise = require('promise');

var connect = new Promise(function(resolve, reject) {
    client.connect({}, resolve, reject);
});

var running = 0;
var sent = 0;
var received = 0;

var startTime;

var subscribe = function() {
    client.subscribe('/user/queue/health', receive, {ack: 'client'});
};

var receive = function() {
    ++received;
    --running;
};

var send = function(n) {
    startTime = new Date();
    console.log('[' + startTime.toISOString() + '] sending ' + n + ' times.');
    (function sendRequest() {
        if (sent < args.requests) {
            if (running <= args.concurrency) {
                client.send('/app/health', {}, 'Hello');
                ++running;
                ++sent;
            }
            setImmediate(sendRequest);
        }
    })();
};

var end = function(startTime) {
    (function check() {
        var now = new Date();
        if (running <= 0 || startTime.getTime() + args.timeLimit * 1000 < now.getTime()) {
            args.received = received;
            utils.logResult(startTime, now, args);
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
        send(args.requests);
    }).then(function() { // wait for all subscription messages
        end(startTime);
    }
);







