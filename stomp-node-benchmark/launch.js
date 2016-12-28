var argv = require('minimist')(process.argv.slice(2), {default: {c:1,n:100}});
var concurrency = argv.c;
var repeat = argv.n;

var exec = require('child_process').exec;

for (var i = 0; i < concurrency; ++i) {
    exec('node ./stomp-node.js -n ' + repeat, function(error, stdout, stderr) {
        console.log(stdout);
        if (stderr) {
            console.log('stderr:\n', stderr);
        }
        if (error !== null) {
            console.log('exec error:\n', error);
        }
    });
}