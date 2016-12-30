var path = require('path');

function logResult(startTime, endTime, args) {
    var totalTime = endTime.getTime() - startTime.getTime();
    var avgTime = totalTime / args.requests;
    var script = path.basename(process.argv[1], '.js');
    console.log('[' + endTime.toISOString() + '] '
        + 'received ' + args.received + ' times ('
        + 'total: ' + totalTime + ' ms.'
        + ', avg: ' + avgTime + ' ms.)');
    var json = {
        script: script,
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString(),
        requests: args.requests,
        concurrency: args.concurrency,
        received: args.received,
        totalTime: totalTime,
        avgTime: avgTime
    };
    if (args.sockjs) {
        if (typeof args.sockjs === 'string') {
            json.transport = args.sockjs;
        } else {
            json.transport = 'sockjs';
        }
    } else if (script == 'stomp') {
        json.transport = 'websocket'
    }
    var text = JSON.stringify(json);
    process.stderr.write(text+'\n');
}

var opts = {
    c: {
        alias: 'concurrency',
        default: 10,
        type: 'number',
        description: 'Number of multiple requests to make at a time'
    },
    n: {
        alias: 'requests',
        default: 100,
        type: 'number',
        description: 'Number of requests to perform'
    },
    t: {
        alias: 'timeLimit',
        default: 60,
        type: 'number',
        description: 'Seconds to max. to spend on benchmarking'
    },
    k: {
        alias: 'keepAlive',
        default: false,
        type: 'boolean',
        description: 'Use HTTP KeepAlive feature (HTTP only)'
    },
    d: {
        alias: 'debug',
        default: false,
        type: 'boolean',
        description: 'Enable debug output'
    },
    'sockjs': {
        default: false,
        description: 'Use SockJS client (WebSocket only)'
    },
    'sockjs-server': {
        default: true,
        description: 'Server supports SockJS (WebSocket only)'
    },
    'base-url': {
        default: 'http://localhost:8080/stomp',
        description: 'Server base URL'
    },
    url: {
        description: 'Either full URL or relative URL'
    }
};

function parseArgs() {
    var args = require('yargs')
        .options(opts)
        .config()
        .help()
        .alias('help', ['h', '?'])
        .usage('Usage: $0 [options]')
        .argv;
    args.fullUrl = function() {
        self = this;
        if (self.url === undefined) {
            return self.baseUrl;
        } else if (self.url.startsWith('/')) {
            return self.baseUrl + self.url;
        } else {
            return self.url;
        }
    }
    return args;
}

exports.logResult = logResult;
exports.parseArgs = parseArgs;