var argv = require('minimist')(process.argv.slice(2), {default: {n:100,m:10}});
var repeat = argv.n;
var limit = argv.m;

var request = require('request');
if (argv.d) {
    require('request').debug = true;
}

var count = 0;
var received = 0;

var startTime;

var send = function(n) {
    startTime = new Date();
    console.log('[' + startTime.toISOString() + '] sending ' + n + ' times.');
    var opts = {
        body: 'Hello'
    };
    var callback = function() {
        --count;
        ++received;
    }
    for (var i = 0; i < n; ++i) {
        ++count;
        request.post('http://localhost:8080/stomp/health', opts, callback);
    }
};

var end = function() {
    (function check() {
        var now = new Date();
        if (count <= 0 || startTime.getTime() + limit * 1000 < now.getTime()) {
            var total = now.getTime() - startTime.getTime();
            console.log('[' + now.toISOString() + '] '
                + received + ' received ('
                + 'total: ' + total + ' ms.'
                + ', avg: ' + total / repeat + ' ms.)');
        } else {
            setImmediate(check);
        }
    })();
}

send(repeat);
end();