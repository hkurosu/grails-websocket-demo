var path = require('path');

function logResult(startTime, endTime, args) {
    var totalTime = endTime.getTime() - startTime.getTime();
    var avgTime = totalTime / args.requests;
    console.log('[' + endTime.toISOString() + '] '
        + 'received ' + args.received + ' times ('
        + 'total: ' + totalTime + ' ms.'
        + ', avg: ' + avgTime + ' ms.)');
    var json = {
        script: path.basename(process.argv[1]),
        requests: args.requests,
        concurrency: args.concurrency,
        received: args.received,
        totalTime: totalTime,
        avgTime: avgTime,
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString()
    };
    if (args.useSockJs) {
        if (typeof args.useSockJs === 'string') {
            json.transport = args.useSockJs;
        } else {
            json.transport = 'websocket';
        }
    }
    var text = JSON.stringify(json);
    process.stderr.write(text+'\n');
}

function parseArgs() {
    var argv = require('minimist')(process.argv.slice(2), {default: {c:10,n:100,t:60}});
    var args = Object.assign({}, argv);
    args.requests = argv.n;
    args.timeLimit = argv.t;
    args.concurrency = argv.c;
    args.debug = argv.d;
    if (argv.k) {
        args.keepalive = true;
    }
    return args;
}

exports.logResult = logResult;
exports.parseArgs = parseArgs;