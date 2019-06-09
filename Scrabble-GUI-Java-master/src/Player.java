/*
 * Dean Pakravan: 757389
 * Assignment 2 - Distributed Systems - Sem2 2018
 * Player Class
 */

public class Player {
	private int score;
	private int id;
	private int vote;
	private int pass;
	
	public Player(){
		setScore(0);
	}
	// Getting the score
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	// The Id of each player 0,1,2...
	public void setId (int id) {
		this.id= id;
	}
	public int getID () {
		return id;
	}
	// Methods for handling the votes
	public void setVote(int vote) {
		this.vote = vote;
	}
	public int getVote() {
		return vote;
	}
	// Methods for dealing with "pass"
	public void addPass(int pass) {
		this.pass = pass;
	}
	public int getPass() {
		return pass;
	}
	
}