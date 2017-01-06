var utils = require('./utils.js');
var args = utils.parseArgs();
// additional args
if (args.url === undefined) {
    args.url = '/health';
}

var request = require('request');
if (args.debug) {
    request.debug = true;
}

var debug = require('debug')('http-bench');

var running = 0;
var sent = 0;
var received = 0;

var startTime;

var send = function(n) {
    startTime = new Date();
    console.log('[' + startTime.toISOString() + '] sending  ' + n + ' times.');
    var callback = function() {
        --running;
        ++received;
        if (received > 0 && received % 1000 == 0) {
            debug('Received: %d, Running: %d', received, running);
        }
    };
    var opts = {
        uri: args.fullUrl(),
        method: 'POST',
        forever: args.keepAlive,
        body: 'Hello',
        agentOptions: {
            maxSockets: args.concurrency
        }
    };
    debug('Sending HTTP requests: %d', args.requests);
    debug('Request options: ' + JSON.stringify(opts));
    (function sendRequest() {
        if (sent < args.requests) {
            var stop = new Date().getTime() + 100; // stop looping after 100ms
            while (sent < args.requests && running < args.concurrency && stop > new Date().getTime()) {
                request(opts, callback);
                ++running;
                ++sent;
                if (sent > 0 && sent % 1000 == 0) {
                    debug('Sent: %d, Running: %d', sent, running);
                }
            }
            setImmediate(sendRequest);
        }
    })();
};

var end = function() {
    (function check() {
        var now = new Date();
        if (received >= args.requests || startTime.getTime() + args.timeLimit * 1000 < now.getTime()) {
            args.received = received;
            utils.logResult(startTime, now, args);
            debug('Received HTTP Requests: %d', received);
        } else {
            setImmediate(check);
        }
    })();
};

send(args.requests);
end();
