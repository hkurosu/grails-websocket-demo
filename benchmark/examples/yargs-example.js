var args = require('../utils.js').parseArgs();

if (args.url === undefined) {
    args.url = '/health';
}

console.log("fullUrl="+args.fullUrl());
console.log(args);