
// parse argv
var utils = require('./utils.js');
var args = utils.parseArgs();

// load required ...
var Promise = require('promise');

var stompFactory;
stompFactory = function() {
    var Stomp = require('stompjs');
    return Stomp.overWS("ws://localhost:8080/stomp/broker");
};

// globals
var running = 0;
var sent = 0;
var received = 0;
var startTime;

var promises = [];
for (var i = 0; i < args.concurrency; ++i) {
    var promise = new Promise(function(resolve, reject) {
        var client = stompFactory();

        if (args.debug) {
            client.debug = function(message) {
                console.log(message);
            }
        }

        var receive = function() {
            ++received;
            --running;
        };

        var connectCallback = function() {
            client.subscribe('/user/queue/health', receive, {ack: 'client'});
            resolve(client);
        };

        client.connect({}, connectCallback, reject);
    });
    promises.push(promise);
}

var send = function(clients) {
    startTime = new Date();
    console.log('[' + startTime.toISOString() + '] sending  ' + args.requests + ' times.');
    (function sendRequest() {
        if (sent < args.requests) {
            if (running <= args.concurrency) {
                var client = clients[sent % clients.length];
                client.send('/app/health', {}, 'Hello');
                ++running;
                ++sent;
            }
            setImmediate(sendRequest);
        }
    })();
};

var end = function(clients) {
    (function check() {
        var now = new Date();
        var timeOut = startTime.getTime() + args.timeLimit * 1000 < now.getTime();
        if (running <= 0 || timeOut) {
            args.received = received;
            utils.logResult(startTime, now, args);
            process.exit(timeOut ? 1 : 0);
        } else {
            setImmediate(check);
        }
    })();
};

function disconnectAll(clients) {
    for (var i = 0; i < clients.length; ++i) {
        try {
            clients[i].disconnect();
        } catch (ignored) {
            console.error("failed to disconnect", ignored);
        }
    }
};

Promise.all(promises)
    .then(function(clients) { // send() messages
        send(clients);
        end(clients);
    });







