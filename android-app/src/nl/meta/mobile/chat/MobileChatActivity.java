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

package nl.meta.mobile.chat;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codebutler.android_websockets.WebSocketClient;

public class MobileChatActivity extends Activity {

	// Fields
	private TextView tvConnectStatus;
	private EditText etName, etMessage;
	private ScrollView svMessages;
	private LinearLayout llMessages;
	private Button btnSend;

	// This library requires a raw websocket url.
	// For NodeJS with SockJS this should be "ws://YOUR.URL:PORT/IDENTIFIER/websocket"
	private final String CONSTANT_WEBSOCKETS_URL = "ws://your.site.nl:6975/mobilechat/websocket";
	private final String CONSTANT_NO_NAME_ERROR_MSG = "Please put you name in box above!";

	private WebSocketClient mClient;
	private boolean connected = false;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_chat);

		// Declares
		tvConnectStatus = (TextView) findViewById(R.id.tvConnected);
		etName = (EditText) findViewById(R.id.etName);
		llMessages = (LinearLayout) findViewById(R.id.llMessages);
		svMessages = (ScrollView) findViewById(R.id.svMessages);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btnSend = (Button) findViewById(R.id.btnSend);

		mHandler = new Handler();

		// Update connection label
		setConnectionLabel();

		// Send message button handler
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// check connection
				if (connected) {
					// check name not null
					if (!etName.getText().toString().equals("")) {
						if (!etMessage.getText().toString().equals("")) {
							// Send message
							mClient.send(etName.getText().toString() + ": " + etMessage.getText().toString());
							etMessage.setText("");
						}
					} else {
						// Name is empty
						addMessageToChat(CONSTANT_NO_NAME_ERROR_MSG);
					}
				}
			}
		});

		// Websockets setup
		List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));
		mClient = new WebSocketClient(URI.create(CONSTANT_WEBSOCKETS_URL), new WebSocketClient.Listener() {

			@Override
			public void onMessage(byte[] data) {
				// Byte array not used
				System.out.println(data);
			}

			@Override
			public void onMessage(String message) {
				// Process message - Handler required because it's on the UI thread.
				final String messageForHandler = message;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						addMessageToChat(messageForHandler);
					}
				});
			}

			@Override
			public void onError(Exception error) {
				// Should handle these errors...
				error.getStackTrace();
				Thread.currentThread().getStackTrace();
			}

			@Override
			public void onDisconnect(int code, String reason) {
				connected = false;
				// This is already on a handler
				setConnectionLabel();
			}

			@Override
			public void onConnect() {
				connected = true;
				// This is already on a handler
				setConnectionLabel();
			}
		}, extraHeaders);

		// Tell the client to connect
		mClient.connect();
	}

	/**
	 * Depending on the connection status this method will set the tvConnectStatus
	 */
	private void setConnectionLabel() {
		if (connected) {
			tvConnectStatus.setText("Connected!");
			tvConnectStatus.setTextColor(getResources().getColor(R.color.light_green));
		} else {
			tvConnectStatus.setText("Disconnected.");
			tvConnectStatus.setTextColor(getResources().getColor(R.color.red));
		}
	}

	/**
	 * This method adds a new TextView with the message to the chatbox.
	 * 
	 * @param message
	 *            The message to be added.
	 */
	private void addMessageToChat(String message) {
		TextView tv = new TextView(this);
		tv.setText(message);
		llMessages.addView(tv);
		// Scroll to the end
		svMessages.fullScroll(View.FOCUS_DOWN);
	}
}
