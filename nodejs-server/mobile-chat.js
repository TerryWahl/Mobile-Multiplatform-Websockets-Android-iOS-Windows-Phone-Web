/*
Mobile Chat (ff-mobile-chat) is a cross platform
(Android, iOS, Windows Phone 8, web) chat supported by NodeJS websockets.
<https://github.com/TerryWahl/ff-mobile-chat>

Copyright (C) 2013-2014  Terry Wahl & Marco Jacobs

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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