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

namespace MobileChat
{
    public partial class MobileChat : PhoneApplicationPage
    {
        public MobileChat()
        {
            InitializeComponent();
        }

        private void btSend_Click(object sender, RoutedEventArgs e)
        {
            if (tbxName.Text == "Name" || tbxName.Text == "")
            {
                addMessageToChatBox("Please put you name in box above!");
            }
            else if (tbxMessage.Text != "")
            {
                string message = tbxMessage.Text;
                addMessageToChatBox(message);
                tbxMessage.Text = "";
            }
        }

        private void addMessageToChatBox(string message)
        {
            tbChat.Text = tbChat.Text + message + "\n";
            _scrollViewer.ScrollToVerticalOffset(_scrollViewer.ExtentHeight);
        }
    }
}