var path = require('path');

function logResult(startTime, endTime, args) {
    var totalTime = endTime.getTime() - startTime.getTime();
    var avgTime = totalTime / args.requests;
    console.log('[' + endTime.toISOString() + '] '
        + args.received + ' received ('
        + 'total: ' + totalTime + ' ms.'
        + ', avg: ' + avgTime + ' ms.)');
    var text = JSON.stringify({
        script: path.basename(process.argv[1]),
        requests: args.requests,
        concurrency: args.concurrency,
        received: args.received,
        totalTime: totalTime,
        avgTime: avgTime,
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString()
    });
    process.stderr.write(text);
}

function parseArgs() {
    var argv = require('minimist')(process.argv.slice(2), {default: {c:10,n:100,t:10}});
    var args = {};
    args.requests = argv.n;
    args.timeLimit = argv.t;
    args.concurrency = argv.c;
    args.debug = argv.d;
    return args;
}

exports.logResult = logResult;
exports.parseArgs = parseArgs;