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

(function () {
    var connectToServer = function () {
        //Connect to your server here
        var mobileChatSocket = new SockJS('http://your.server:6975/mobilechat');

        mobileChatSocket.onopen = function () {
            clearInterval(connectRetry);
            $('.connect-status')
                .removeClass('disconnected')
                .addClass('connected')
                .text('Connected');
        };

        //Receive message from server
        mobileChatSocket.onmessage = function (e) {
            $('#chatBox').html($('#chatBox').html() + '</br>' + e.data);
            var objDiv = document.getElementById('chatBox');
            objDiv.scrollTop = objDiv.scrollHeight;
        };

        mobileChatSocket.onclose = function () {
            clearInterval(connectRetry);
            connectRetry = setInterval(connectToServer, 1000);
            $('.connect-status')
                .removeClass('connected')
                .addClass('disconnected')
                .text('Disconnected');
        };

        //Send your message to the server.
        $('#sendButton').on('click', function () {
            if ($('#userName').val() != '') {
                if ($('#messageBox').val() != '') {
                    mobileChatSocket.send($('#userName').val() + ': ' + $('#messageBox').val());
                    document.getElementById("messageBox").value = '';
                }
            } else {
                $('#chatBox').html($('#chatBox').html() + '</br>' + 'Please put you name in box above!');
                var objDiv = document.getElementById('chatBox');
                objDiv.scrollTop = objDiv.scrollHeight;
            }
        });

        //Prevent enter refreshing the page, it sends the text from now on
        $('#messageBox').keydown(function (e) {
            if (e.keyCode == 13) { // 13 is enter
                if ($('#userName').val() != '') {
                    if ($('#messageBox').val() != '') {
                        mobileChatSocket.send($('#userName').val() + ': ' + $('#messageBox').val());
                        document.getElementById("messageBox").value = '';
                    }
                } else {
                    $('#chatBox').html($('#chatBox').html() + '</br>' + 'Please put you name in box above!');
                    var objDiv = document.getElementById('chatBox');
                    objDiv.scrollTop = objDiv.scrollHeight;
                }
                return false;
            }
        });

        //Prevent enter refreshing the page
        $('#messageBox').keydown(function (e) {
            if (e.keyCode == 13) { // 13 is enter

                return false;
            }
        });
    };

    var connectRetry = setInterval(connectToServer, 1000);
})();