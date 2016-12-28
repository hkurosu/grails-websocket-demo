var path = require('path');

function logResult(startTime, endTime, requests, received) {
    var totalTime = endTime.getTime() - startTime.getTime();
    var avgTime = totalTime / requests;
    console.log('[' + endTime.toISOString() + '] '
        + received + ' received ('
        + 'total: ' + totalTime + ' ms.'
        + ', avg: ' + avgTime + ' ms.)');
    var text = JSON.stringify({
        script: path.basename(process.argv[1]),
        requests: requests,
        received: received,
        totalTime: totalTime,
        avgTime: avgTime,
        startTime: startTime.toISOString(),
        endTime: endTime.toISOString()
    });
    process.stderr.write(text);
}

exports.logResult = logResult;