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

/*
 * BubbleGame.java  is a the class the handles the gaming action (Popping Bubble Wraps) 
 * 
 * @author Amente Bekele
 * @version 0.1
 * 
 */
public class BubbleGame extends Application implements OnItemClickListener,
		OnScrollListener, OnTouchListener {

	// BubbleWrap sizes
	public int SMALL_WRAP = 100; 
	public int MEDIUM_WRAP = 500;
	public int LARGE_WRAP = 1000;

		
	public int DEFAULT_WRAP = SMALL_WRAP; // The default size (Application starts)
	public int currentWrap = DEFAULT_WRAP;//The currently selected size (Application restored from background) 

	// Sound resources
	private int[] POP_SOUNDS = { R.raw.pop_sound_1, R.raw.pop_sound_2,
			R.raw.pop_sound_3, R.raw.pop_sound_4 };
	private int DRAG_SOUND = R.raw.drag_sound;

	//Image resources
	private int UNPOPPED_BUBBLE = R.drawable.un_popped;
	private int[] POPPED_BUBBLES = { R.drawable.popped_1, R.drawable.popped_2,
			R.drawable.popped_3, R.drawable.popped_4 };

	private Random random = new Random(); // Use for randomly selecting sound and popped images
	private SoundPool sound; // Sound pool used for drag sound
	int[] popSounds; // popSounds
	int dragSound;  // drag Sounds
	int dragSoundStream = 0; 
	private boolean scrollStarted = false;// Is the view bieng scrolled
	Vibrator vibrator; // Device vibrator reference

	private int score; // Score (Number of popped bubbles
	private BubbleWrap bubbleWrap; // BubbleWrap reference

	// UI Elements
	private MainActivity activity; // Main App activity
	private TextView scoreText; 
	private GridView bubbleGrid; 
	

	private ImageAdapter adapter;// Image adapter used for rendering and updating images 

	/* 
	 * Gets the current score  
	 * @return score
	 */
	public int getScore() {
		return bubbleWrap.getNumberOfBubbles() - score;
	}

	/*
	 * Gets the bubble wrap
	 * @return bubbleWrap
	 */
	public BubbleWrap getBubbleWrap() {
		return bubbleWrap;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	public void onCreate() {
		super.onCreate();
		bubbleWrap = new BubbleWrap(DEFAULT_WRAP, UNPOPPED_BUBBLE);

	}
     
	/*
	 * (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
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

	/*
	 * Initialises the main activity (Renders views)
	 * 
	 */
	public void initializeUi(MainActivity activity) {
		//Get the activity
		this.activity = activity;

		//New image adapter, for the bubbles from the Wrap's GridView
		adapter = new ImageAdapter(activity, bubbleWrap.getBubbles());

		// Set the score text and font
		scoreText = (TextView) activity.findViewById(R.id.poppedNumber);
		scoreText.setText(this.getScore() + "");
		Typeface lcdFont = Typeface.createFromAsset(getAssets(),
				"fonts/LCDM2N__.TTF");
		scoreText.setTypeface(lcdFont);

		
		// Get the grid and attach listeners
		bubbleGrid = (GridView) activity.findViewById(R.id.gridview);
		bubbleGrid.setAdapter(adapter);
		bubbleGrid.setOnItemClickListener(this);
		bubbleGrid.setOnScrollListener(this);
		bubbleGrid.setOnTouchListener(this);
		bubbleGrid.setBackgroundResource(R.color.back_ground);

		// Create the sound pool
		sound = new SoundPool(POP_SOUNDS.length + 1, AudioManager.STREAM_MUSIC,
				0);
		popSounds = new int[POP_SOUNDS.length];

		// Keep reference of the sounds
		for (int i = 0; i < popSounds.length; i++) {
			popSounds[i] = sound.load(activity, POP_SOUNDS[i], 1);
		}

		//Keep reference for the drag sound (Currently Buggy)
		dragSound = sound.load(activity, DRAG_SOUND, 1);

		
	}

	/*
	 * Updates the the user interface 
	 */
	public void updateUi() {

		//Notify the adapter to replace images
		adapter.notifyDataSetChanged();
		//Update the score
		scoreText.setText(this.getScore() + "");
		//Check all bubbles are popped
		checkDone();

	}

	/*
	 * Creates a new game
	 * 
	 * @param size the size of the bubble wrap
	 */
	public void newGame(int size) {
		bubbleWrap = new BubbleWrap(size, UNPOPPED_BUBBLE);
		score = bubbleWrap.getNumberOfPopped();
		adapter = new ImageAdapter(activity, bubbleWrap.getBubbles());
		bubbleGrid.setAdapter(adapter);
		updateUi();

	}
	
	
    /*
     * Checks if the game is over
     */
	public void checkDone() {
		if (this.getScore() == 0) {
			scoreText.setText(R.string.done_text);
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		dragSoundStream = 0;

	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		scrollStarted = true;

	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			dragSoundStream = sound.play(dragSound, (float) 1.0, (float) 1.0,
					1, 0, (float) 1.0);

			return false;
		}
		return false;
	}

}
