//vars
var http = require('http'),
    sockjs = require('sockjs'),
    mobileChat = sockjs.createServer(),
    connections = [];

//configure message relays
mobileChat.on('connection', function (conn) {
    console.log('New connection');
    connections.push(conn);
    conn.on('data', function (message) {
        console.log('New data: ' + message);
        // send message to all connections
        for (var i = 0; i < connections.length; i++) {
            connections[i].write(message);
        }
    });
    conn.on('close', function () {
        connections.splice(connections.indexOf(conn), 1);
        console.log('Closed connection');
    });
});

//server loc
var server = http.createServer();
mobileChat.installHandlers(server, {
    prefix: '/mobilechat'
});
//location is self
server.listen(6975, '0.0.0.0');