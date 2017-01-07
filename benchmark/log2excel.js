var fs = require('fs');
var inputStream = fs.createReadStream(process.argv[2], {autoClose: true});
const readline = require('readline').createInterface({
    input: inputStream
});

var json = [];
readline.on('line', function(line) {
    if (line) {
        var obj = JSON.parse(line);
        json.push(obj);
    }
});
readline.on('close', function() {
    var xls = require('json2xls')(json);
    var file = process.argv[2].replace(/\..*$/, '.xlsx');
    fs.writeFileSync(file, xls, 'binary');
});


