package nl.meta.mobile.chat;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected static final String TAG = "MOBILE CHAT MESSAGE";
	// Fields
	TextView tvConnectStatus;
	EditText etName, etMessages, etMessage;
	Button btnSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvConnectStatus = (TextView) findViewById(R.id.tvConnected);
		etName = (EditText) findViewById(R.id.etName);
		etMessages = (EditText) findViewById(R.id.etMessages);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btnSend = (Button) findViewById(R.id.btnSend);

		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!etName.getText().toString().equals("")) {
					if (!etMessage.getText().toString().equals("")) {

					}
				}
			}
		});

		List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));

		WebSocketClient client = new WebSocketClient(URI.create(""), new WebSocketClient.Listener() {

			@Override
			public void onMessage(byte[] data) {
				Log.d(TAG, String.format("Got binary message! %s", data));
			}

			@Override
			public void onMessage(String message) {
				Log.d(TAG, String.format("Got string message! %s", message));
			}

			@Override
			public void onError(Exception error) { 
				Log.e(TAG, "Error!", error);
			}

			@Override
			public void onDisconnect(int code, String reason) {
				Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
			}

			@Override
			public void onConnect() {
				Log.d(TAG, "Connected!");
			}
		}, extraHeaders);

		client.connect();
		client.send("hello!");
		//client.send(new byte[] { (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF });
		client.disconnect();

	}
}
