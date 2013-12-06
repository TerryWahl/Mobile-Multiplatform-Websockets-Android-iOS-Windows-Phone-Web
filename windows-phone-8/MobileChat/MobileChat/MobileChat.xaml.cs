/*
Mobile Chat (ff-mobile-chat) is a cross platform
(Android, iOS, Windows Phone 8, web) chat supported by NodeJS websockets.
<https://github.com/TerryWahl/ff-mobile-chat>

Copyright (C) 2013-2014 Terry Wahl & Marco Jacobs

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using MobileChat.Resources;
using System.Windows.Threading;
using WebSocket4Net;
using SuperSocket.ClientEngine;

namespace MobileChat
{
    public partial class MobileChat : PhoneApplicationPage
    {
        //This library requires a raw websocket url.
        //For NodeJS with SockJS this should be "ws://YOUR.URL:PORT/IDENTIFIER/websocket"
        private readonly String webSocketLink = "ws://priv.twdev.nl:6975/mobilechat/websocket";

        private WebSocket websocket;
        private bool socketOpen = false;

        public MobileChat()
        {
            InitializeComponent();

            //Init the websocket  
            websocket = new WebSocket(webSocketLink);
            websocket.Opened += new EventHandler(websocket_Opened);
            websocket.Error += new EventHandler<ErrorEventArgs>(websocket_Error);
            websocket.Closed += new EventHandler(websocket_Closed);
            websocket.MessageReceived += new EventHandler<MessageReceivedEventArgs>(websocket_MessageReceived);
            websocket.Open();
        }

        private void btSend_Click(object sender, RoutedEventArgs e)
        {
            //Wait for socket to open
            if (socketOpen)
            {
                //Check name is not Name (default) and not empty
                if (_tbxName.Text == "Name" || _tbxName.Text == "")
                {
                    addMessageToChatBox("Please put you name in box above!");
                }
                else if (_tbxMessage.Text != "")
                {
                    //If message is not empty, send the message
                    string message = _tbxName.Text + ": " + _tbxMessage.Text;
                    _tbxMessage.Text = "";
                    websocket.Send(message);
                }
            }
        }

        private void addMessageToChatBox(string message)
        {
            //Add message to chat box.
            _tbChatbox.Text = _tbChatbox.Text + message + "\n";
            _svChatboxHolder.ScrollToVerticalOffset(_svChatboxHolder.ExtentHeight);
        }

        private void websocket_MessageReceived(object sender, MessageReceivedEventArgs e)
        {
            //Dispatcher is needed to work on the UI thread.
            Dispatcher.BeginInvoke(() =>
            {
                addMessageToChatBox(e.Message);
            });
        }

        private void websocket_Closed(object sender, EventArgs e)
        {
            socketOpen = false;
        }

        private void websocket_Error(object sender, ErrorEventArgs e)
        {
            //This should be used...
        }

        private void websocket_Opened(object sender, EventArgs e)
        {
            socketOpen = true;
        }
    }
}