package com.app.bubblewrap;

public class BubbleWrap {

	private int numberOfBubbles;
	private int poppedBubbles;
	private Integer[] bubbles;

	public BubbleWrap(int numberOfBubbles, int bubble) {
		this.numberOfBubbles = numberOfBubbles;
		this.poppedBubbles = 0;
		this.bubbles = new Integer[numberOfBubbles];
		for (int i = 0; i < numberOfBubbles; i++) {
			bubbles[i] = bubble;
		}

	}

	public void popBubble(int index, int bubble) {

		bubbles[index] = bubble;
		poppedBubbles++;
	}

	public int getNumberOfPopped() {
		return poppedBubbles;
	}

	public int getNumberOfBubbles() {
		return numberOfBubbles;
	}

	public Integer[] getBubbles() {

		return bubbles;

	}

	public boolean isBubble(int index, int bubble) {
		if (bubbles[index] == bubble) {
			return true;
		}

		return false;
	}

}
