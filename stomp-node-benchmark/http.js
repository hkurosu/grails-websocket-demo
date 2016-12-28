var argv = require('minimist')(process.argv.slice(2), {default: {n:100,t:10}});
var requests = argv.n;
var timeLimit = argv.t;
var blocking = argv.b;
var debug = argv.d;

var request = require('request');
if (debug) {
    request.debug = true;
}

var running = 0;
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
        body: 'Hello'
    };
    for (var i = 0; i < n; ++i) {
        ++running;
        request(opts, callback);
    }
};

var end = function() {
    (function check() {
        var now = new Date();
        if (running <= 0 || startTime.getTime() + timeLimit * 1000 < now.getTime()) {
            require('./utils.js').logResult(startTime, now, requests, received);
        } else {
            setImmediate(check);
        }
    })();
}

send(requests);
end();
