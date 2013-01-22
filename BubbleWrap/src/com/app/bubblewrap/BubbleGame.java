package com.app.bubblewrap;

import java.util.Random;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class BubbleGame extends Application implements OnItemClickListener,
		OnScrollListener, OnTouchListener {

	public int SMALL_WRAP = 100;
	public int MEDIUM_WRAP = 500;
	public int LARGE_WRAP = 1000;

	public int DEFAULT_WRAP = SMALL_WRAP;
	public int currentWrap = DEFAULT_WRAP;

	private int[] POP_SOUNDS = { R.raw.pop_sound_1, R.raw.pop_sound_2,
			R.raw.pop_sound_3, R.raw.pop_sound_4 };

	private int DRAG_SOUND = R.raw.drag_sound;

	private int UNPOPPED_BUBBLE = R.drawable.un_popped;
	private int[] POPPED_BUBBLES = { R.drawable.popped_1, R.drawable.popped_2,
			R.drawable.popped_3, R.drawable.popped_4 };

	private Random random = new Random();
	private SoundPool sound;
	int[] popSounds;
	int dragSound;
	int dragSoundStream = 0;
	private boolean scrollStarted = false;
	Vibrator vibrator;

	private int score;
	private BubbleWrap bubbleWrap;

	// UI Elements
	private MainActivity activity;
	private TextView scoreText;
	private GridView bubbleGrid;
	// private TextView numberOfBubblesText;

	private ImageAdapter adapter;

	public int getScore() {
		return bubbleWrap.getNumberOfBubbles() - score;
	}

	public BubbleWrap getBubbleWrap() {
		return bubbleWrap;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		bubbleWrap = new BubbleWrap(DEFAULT_WRAP, UNPOPPED_BUBBLE);

	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		if (bubbleWrap.isBubble(position, UNPOPPED_BUBBLE)) {
			int n = random.nextInt(POPPED_BUBBLES.length);
			bubbleWrap.popBubble(position, POPPED_BUBBLES[n]);
			vibrator.vibrate(50);
			n = random.nextInt(popSounds.length);
			sound.play(popSounds[n], (float) 1.0, (float) 1.0, 1, 0,
					(float) 1.0);

			score = bubbleWrap.getNumberOfPopped();
			updateUi();
		}

	}

	public void initializeUi(MainActivity activity) {
		this.activity = activity;

		adapter = new ImageAdapter(activity, bubbleWrap.getBubbles());

		scoreText = (TextView) activity.findViewById(R.id.poppedNumber);
		scoreText.setText(this.getScore() + "");
		Typeface lcdFont = Typeface.createFromAsset(getAssets(),
				"fonts/LCDM2N__.TTF");
		scoreText.setTypeface(lcdFont);

		// numberOfBubblesText = (TextView) activity
		// .findViewById(R.id.unPoppedNumber);
		// numberOfBubblesText.setText(bubbleWrap.getNumberOfBubbles() + "");

		bubbleGrid = (GridView) activity.findViewById(R.id.gridview);
		bubbleGrid.setAdapter(adapter);
		bubbleGrid.setOnItemClickListener(this);
		bubbleGrid.setOnScrollListener(this);
		bubbleGrid.setOnTouchListener(this);
		bubbleGrid.setBackgroundResource(R.color.back_ground);

		sound = new SoundPool(POP_SOUNDS.length + 1, AudioManager.STREAM_MUSIC,
				0);
		popSounds = new int[POP_SOUNDS.length];

		for (int i = 0; i < popSounds.length; i++) {
			popSounds[i] = sound.load(activity, POP_SOUNDS[i], 1);
		}

		dragSound = sound.load(activity, DRAG_SOUND, 1);

		// bubbleGrid.setOnTouchListener(this);

	}

	public void updateUi() {

		adapter.notifyDataSetChanged();
		scoreText.setText(this.getScore() + "");
		// numberOfBubblesText.setText(bubbleWrap.getNumberOfBubbles() + "");
		checkDone();

	}

	public void newGame(int size) {
		bubbleWrap = new BubbleWrap(size, UNPOPPED_BUBBLE);
		score = bubbleWrap.getNumberOfPopped();
		adapter = new ImageAdapter(activity, bubbleWrap.getBubbles());
		bubbleGrid.setAdapter(adapter);
		updateUi();

	}

	public void checkDone() {
		if (this.getScore() == 0) {
			scoreText.setText(R.string.done_text);
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		dragSoundStream = 0;

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		scrollStarted = true;

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			dragSoundStream = sound.play(dragSound, (float) 1.0, (float) 1.0,
					1, 0, (float) 1.0);

			return false;
		}
		return false;
	}

}
