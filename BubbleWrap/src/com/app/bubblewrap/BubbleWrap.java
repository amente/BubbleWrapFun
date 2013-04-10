package com.app.bubblewrap;

/*
 * Represents a BubbleWrap object. It keeps track of the number of poppedBubbles * 
 * 
 * @author Amente Bekele
 * @version 0.1
 */
public class BubbleWrap {

	private int numberOfBubbles;
	private int poppedBubbles;
	private Integer[] bubbles;

	/*
	 *Constructs a new BubbleWrap
	 *
	 *@param numberOfBubbles the number of bubbles
	 *@param bubble the resource id of unpopped bubble image
	 */
	public BubbleWrap(int numberOfBubbles, int bubble) {
		this.numberOfBubbles = numberOfBubbles;
		this.poppedBubbles = 0;
		this.bubbles = new Integer[numberOfBubbles];
		for (int i = 0; i < numberOfBubbles; i++) {
			bubbles[i] = bubble;
		}

	}
    
	/*
	 * Pops a bubble
	 * @param index the index of the bubble
	 * @param resource id of the image to replace with
	 */
	public void popBubble(int index, int bubble) {

		bubbles[index] = bubble;
		poppedBubbles++;
	}

	/*
	 * @return the number of popped bubbles
	 */
	public int getNumberOfPopped() {
		return poppedBubbles;
	}

	/*
	 * @return the number of bubbles
	 */
	public int getNumberOfBubbles() {
		return numberOfBubbles;
	}

	/*
	 * @return the bubbles
	 */
	public Integer[] getBubbles() {

		return bubbles;

	}

	/*
	 * 
	 * @param the index of a bubble
	 * @param the resource id of the image used
	 * 
	 * @return true if the bubble is not popped 
	 */
	public boolean isBubble(int index, int bubble) {
		if (bubbles[index] == bubble) {
			return true;
		}

		return false;
	}

}
