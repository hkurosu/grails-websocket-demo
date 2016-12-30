var args = require('../utils.js').parseArgs();

// test 1. relative path
if (args.url === undefined) {
    args.url = '/health';
}
console.log("fullUrl="+args.fullUrl());
console.log();

console.log('args:', args);
console.log();

args.url = undefined;
args.sockjs = undefined;

args.sockjsServer = true;
changeUrl();
console.log("after sockjsServer=true");
console.log("fullUrl="+args.fullUrl());
console.log();

args.sockjsServer = false;
changeUrl();
console.log("after sockjsServer=false");
console.log("fullUrl="+args.fullUrl());

function changeUrl() {
    if (args.sockjs) {
        args.url = '/broker';
    } else if (args.sockjsServer) {
        args.url = args.baseUrl.replace(/^http/, 'ws') + '/broker/websocket';
    } else {
        args.url = args.baseUrl.replace(/^http/, 'ws') + '/broker';
    }
}
