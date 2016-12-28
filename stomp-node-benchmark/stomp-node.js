var argv = require('minimist')(process.argv.slice(2), {default: {c:1,n:100,m:10}});
var repeat = argv.n;
var limit = argv.m;

var Stomp = require('stompjs');
var client = Stomp.overWS("ws://localhost:8080/stomp/broker");

if (argv.d) {
    client.debug = function (message) {
        console.log(message);
    }
}

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

var Promise = require('promise');

var connect = new Promise(function(resolve, reject) {
    client.connect({}, resolve, reject);
});

var label = repeat + ' times';
var intervalId;
var startTime;
var endTimer = function() {
    if (count <= 0 || startTime + limit * 1000 < new Date().getTime()) {
        console.timeEnd(label);
        console.log('[' + new Date().toISOString() + '] ' + received + ' received.');
        client.disconnect();
        clearInterval(intervalId);
    } else {
        // console.log('Remaining Messages: ' + count);
    }
}

connect.then(subscribe)
    .then(function() {
        startTime = new Date().getTime();
        console.log('[' + new Date().toISOString() + '] sending ' + repeat + ' times.');
        console.time(label);
        for (var i = 0; i < repeat; ++i) {
            client.send('/app/health', {}, 'Hello');
            // console.log('Sent Hello');
            ++count;
        }
        intervalId = setInterval(endTimer, 100);
    }
);







