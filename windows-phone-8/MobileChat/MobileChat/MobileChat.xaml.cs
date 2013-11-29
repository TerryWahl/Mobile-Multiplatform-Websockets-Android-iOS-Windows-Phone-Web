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
        private WebSocket websocket;

        public MobileChat()
        {
            InitializeComponent();

            System.Diagnostics.Debug.WriteLine("init");

            websocket = new WebSocket("ws://priv.twdev.nl:6975/mobilechat/websocket");
            websocket.Opened += new EventHandler(websocket_Opened);
            websocket.Error += new EventHandler<ErrorEventArgs>(websocket_Error);
            websocket.Closed += new EventHandler(websocket_Closed);
            websocket.MessageReceived += new EventHandler<MessageReceivedEventArgs>(websocket_MessageReceived);
            websocket.Open();
        }

        private void btSend_Click(object sender, RoutedEventArgs e)
        {
            if (tbxName.Text == "Name" || tbxName.Text == "")
            {
                addMessageToChatBox("Please put you name in box above!");
            }
            else if (tbxMessage.Text != "")
            {
                string message = tbxName.Text + ": " + tbxMessage.Text;
                tbxMessage.Text = "";
                websocket.Send(message);
            }
        }

        private void addMessageToChatBox(string message)
        {
            tbChat.Text = tbChat.Text + message + "\n";
            _scrollViewer.ScrollToVerticalOffset(_scrollViewer.ExtentHeight);
        }

        private void websocket_MessageReceived(object sender, MessageReceivedEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("messageReceived");
            System.Diagnostics.Debug.WriteLine(e.Message);

            Dispatcher.BeginInvoke(() =>
            {
                addMessageToChatBox(e.Message);
            });
        }

        private void websocket_Closed(object sender, EventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("closed");
        }

        private void websocket_Error(object sender, ErrorEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("error");
        }

        private void websocket_Opened(object sender, EventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("opened");
        }
    }
}