package com.example.bubblewrap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	BubbleGame game;

	public final static String EXTRA_MESSAGE = "com.example.bubblewrap.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		game = (BubbleGame) this.getApplication();
		game.initializeUi(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.new_game:
			game.newGame(game.currentWrap);
			return true;
		case R.id.large_wrap:
			game.newGame(game.LARGE_WRAP);
			game.currentWrap = game.LARGE_WRAP;
			return true;
		case R.id.medium_wrap:
			game.newGame(game.MEDIUM_WRAP);
			game.currentWrap = game.MEDIUM_WRAP;
			return true;
		case R.id.small_wrap:
			game.newGame(game.SMALL_WRAP);
			game.currentWrap = game.SMALL_WRAP;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		// EditText editText = (EditText) findViewById(R.id.edit_message);
		// String message = editText.getText().toString();
		// intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);

		// Do something in response to button
	}

}
