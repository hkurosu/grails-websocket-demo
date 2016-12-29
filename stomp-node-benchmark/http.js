var utils = require('./utils.js');
var args = utils.parseArgs();

var request = require('request');
if (args.debug) {
    request.debug = true;
}

var running = 0;
var sent = 0;
var received = 0;

var startTime;

var send = function(n) {
    startTime = new Date();
    console.log('[' + startTime.toISOString() + '] sending ' + n + ' times.');
    var callback = function() {
        --running;
        ++received;
    };
    var opts = {
        uri: 'http://localhost:8080/stomp/health',
        method: 'POST',
        forever: true,
        body: 'Hello'
    };
    (function sendRequest() {
        if (sent < args.requests) {
            if (running <= args.concurrency) {
                request(opts, callback);
                ++running;
                ++sent;
            }
            setImmediate(sendRequest);
        }
    })();
};

var end = function() {
    (function check() {
        var now = new Date();
        if (running <= 0 || startTime.getTime() + args.timeLimit * 1000 < now.getTime()) {
            args.received = received;
            utils.logResult(startTime, now, args);
        } else {
            setImmediate(check);
        }
    })();
}

send(args.requests);
end();
