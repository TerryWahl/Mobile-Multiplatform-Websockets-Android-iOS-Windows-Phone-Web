Mobile-Multiplatform-Websockets-Android-iOS-Windows-Phone-Web
==============

Cross platform (Android, iOS, Windows Phone 8, web) chat supported by NodeJS websockets.

Quick install
==============

Server:
Install Node.js http://nodejs.org/ with SockJS https://github.com/sockjs/sockjs-node on your server. Then run the mobile-chat.js in nodejs-server with the command <code>node mobile-chat.js</code>.

Webclient:
Make all files available through an installed webserver, for example xampp.
Remember to update the following line for your server.

For the Webclient change: <code>mobile-chat.js</code>
```javascript
//Connect to your server here
var mobileChatSocket = new SockJS('http://your.server:6975/mobilechat');
```

For Android change: <code>MobileChatActivity.java</code>
```java
// This library requires a raw websocket url.
// For NodeJS with SockJS this should be
// "ws://YOUR.URL:PORT/IDENTIFIER/websocket"
private final String CONSTANT_WEBSOCKETS_URL = "ws://your.site.nl:6975/mobilechat/websocket";
```

For iOS change: <code>ViewController.m</code>
```objective-c
//This library requires a raw websocket url. It does accept http instead of ws.
//For NodeJS with SockJS this should be "http://YOUR.URL:PORT/IDENTIFIER/websocket"
self.sockjsSocket = [[SRWebSocket alloc] initWithURL:[[NSURL alloc] initWithString:@"http://your.site:6975/mobilechat/websocket"]];
```

For Windows Phone 8 change: <code>MobileChat.xaml.cs</code>
```c#
//This library requires a raw websocket url.
//For NodeJS with SockJS this should be "ws://YOUR.URL:PORT/IDENTIFIER/websocket"
private readonly String webSocketLink = "ws://your.site:6975/mobilechat/websocket";
```

Libraries used
==============
 * Web: SockJS
 * Android : Codebutler
 * IOS: SocketRocket
 * Windows phone 8: WebSocket4Net
