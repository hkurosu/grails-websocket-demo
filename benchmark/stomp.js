// parse argv
var utils = require('./utils.js');
var args = utils.parseArgs();
if (args.url === undefined) {
    if (args.sockjs) {
        args.url = '/broker';
    } else if (args.sockjsServer) {
        args.url = args.baseUrl.replace(/^http/, 'ws') + '/broker/websocket';
    } else {
        args.url = args.baseUrl.replace(/^http/, 'ws') + '/broker';
    }
}

var debug = require('debug')('stomp-bench');
var receiveDdg = require('debug')('stomp-bench:receive');
var sendDbg = require('debug')('stop-bench:send');

// load required ...
var Promise = require('promise');

var stompFactory;
if (args.sockjs) {
    debug('Loading SockJS client ...');
    stompFactory = function() {
        var Stomp = require('stompjs');
        var SockJS = require('sockjs-client');
        var url = args.fullUrl();
        debug('Connecting to ', url);
        var opts = {};
        if (typeof args.sockjs === 'string') {
            opts.transports = args.sockjs;
            debug('Using transport:', opts.transports);
        }
        var socket = new SockJS(url, [], opts);
        return new Stomp.over(socket);
    };
} else {
    debug('Loading Native WebSocket client ...');
    stompFactory = function () {
        var Stomp = require('stompjs');
        var url = args.fullUrl();
        debug('Connecting to ', url);
        return Stomp.overWS(url);
    };
}

// globals
var running = 0;
var sent = 0;
var received = 0;
var startTime;

var promises = [];
for (var i = 0; i < args.concurrency; ++i) {
    var promise = new Promise(function(resolve, reject) {
        var client = stompFactory();

        client.debug = require('debug')('stomp');

        var receive = function() {
            ++received;
            --running;
            receiveDdg('Received: %d, Running: %d', received, running);
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
                sendDbg('Sent: %d, Running: %d', sent, running);
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
            disconnectAll(clients);
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
}

Promise.all(promises)
    .then(function(clients) { // send() messages
        send(clients);
        end(clients);
    });







