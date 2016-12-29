var argv = require('minimist')(process.argv.slice(2), {default: {c:10,n:100,t:10,p:1}});

if (argv.s == null) {
    usage();
    return;
}

var processes = argv.p;

var exec = require('child_process').exec;
var cmd = buildCmdLine(argv);
console.log("Executing " + cmd + "");
for (var i = 0; i < processes; ++i) {
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
        if (key == '_') {
            return;
        } else {
            cmd += ' -' + key;
            if (opts[key] && opts[key] !== true) {
                cmd += ' ' + opts[key];
            }
        }
    });
    return cmd;
}

function usage() {
    console.log('TODO');
}
