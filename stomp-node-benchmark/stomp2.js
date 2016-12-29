
// parse argv
var utils = require('./utils.js');
var args = utils.parseArgs();
if (args.clients == null) {
    args.clients = 1;
}

// load rquired ...
var Stomp = require('stompjs');
var Promise = require('promise');

// globals
var running = 0;
var sent = 0;
var received = 0;
var startTime;

var promises = [];
for (var i = 0; i < args.clients; ++i) {
    var promise = new Promise(function(resolve, reject) {
        var client = Stomp.overWS("ws://localhost:8080/stomp/broker");

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
        }

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

var end = function(startTime, endCallback) {
    (function check() {
        var now = new Date();
        var timeOut = startTime.getTime() + args.timeLimit * 1000 < now.getTime();
        if (running <= 0 || timeOut) {
            args.received = received;
            utils.logResult(startTime, now, args);
            endCallback();
            process.exit(timeOut ? 1 : 0);
        } else {
            setImmediate(check);
        }
    })();
};

Promise.all(promises)
    .then(function(clients) { // send() messages
        send(clients);
        return clients;
    }).then(function(clients) { // wait for all subscription messages
        var endCallback = function() {
            for (var i = 0; i < clients.length; ++i) {
                try {
                    clients[i].disconnect();
                } catch (ignored) {
                    console.error("failed to disconnect", ignored);
                }
            }
        };
        end(startTime, endCallback);
    });







