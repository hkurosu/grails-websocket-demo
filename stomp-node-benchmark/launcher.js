var argv = require('minimist')(process.argv.slice(2), {default: {c:1,n:100,t:10}});

var script = argv.s;
if (script == null) {
    usage();
    return;
}
var concurrency = argv.c;
var requests = argv.n;
var timeLimit = argv.t;

var exec = require('child_process').exec;
var cmd = buildCmdLine(argv);
console.log("Executing " + cmd + "");
for (var i = 0; i < concurrency; ++i) {
    var child = exec(cmd, function(error, stdout, stderr) {
        console.log(stdout);
        if (stderr) {
            console.error(stderr);
        }
        if (error !== null) {
            console.assert('exec error:', error);
        }
    });
    //console.log('['+new Date().toISOString()+'] started child process['+(i+1)+']: ' + child.pid);
}

function buildCmdLine(opts) {
    cmd = 'node ' + opts.s;
    Object.keys(opts).forEach(function (key) {
        if (key == '_')
            return;
        cmd += ' -' + key;
        if (opts[key] && opts[key] !== true) {
            cmd += ' ' + opts[key];
        }
    });
    return cmd;
}

function usage() {
    console.log('TODO');
}
