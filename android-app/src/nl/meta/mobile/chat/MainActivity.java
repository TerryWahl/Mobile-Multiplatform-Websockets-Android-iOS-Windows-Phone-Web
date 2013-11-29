package nl.meta.mobile.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	//Fields
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
				if(!etName.getText().toString().equals("")){
					if(!etMessage.getText().toString().equals("")){
						
					}
				}
			}
		});
	}
}
