var argv = require('minimist')(process.argv.slice(2), {default: {c:1,n:100}});
var limit = argv.n;

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
        --count;
        ++received;
        // message.ack();
    }, {ack: 'client'});
    console.log('Subscribing /user/queue/health');
};

var Promise = require('promise');

var connect = new Promise(function(resolve, reject) {
    client.connect({}, resolve, reject);
});

var label = limit + ' times';
var intervalId;
var endTimer = function() {
    if (count <= 0) {
        console.timeEnd(label);
        console.log(received + ' received');
        client.disconnect();
        clearInterval(intervalId);
    } else {
        console.log('Remaining Messages: ' + count);
    }
}

connect.then(subscribe)
    .then(function() {
        console.time(label);
        for (var i = 0; i < limit; ++i) {
            client.send('/app/health', {}, 'Hello');
            // console.log('Sent Hello');
            ++count;
        }
        intervalId = setInterval(endTimer, 1000);
    }
);







