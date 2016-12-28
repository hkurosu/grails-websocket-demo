var argv = require('minimist')(process.argv.slice(2), {default: {c:1,n:100,m:10}});

var script = argv.s;
if (script == null) {
    usage();
    return;
}
var concurrency = argv.c;
var repeat = argv.n;
var limit = argv.m;

var exec = require('child_process').exec;
var cmd = 'node ' + script + ' -n ' + repeat + ' -m ' + limit;
for (var i = 0; i < concurrency; ++i) {
    var child = exec(cmd, function(error, stdout, stderr) {
        console.log(stdout);
        if (stderr) {
            console.log('stderr:\n', stderr);
        }
        if (error !== null) {
            console.log('exec error:\n', error);
        }
    });
    //console.log('['+new Date().toISOString()+'] started child process['+(i+1)+']: ' + child.pid);
}

function usage() {
    console.log('TODO');
}
