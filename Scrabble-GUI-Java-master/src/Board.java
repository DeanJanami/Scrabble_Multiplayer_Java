/*
 *  Dean Pakravan: 757389
 *  Assignment 2: Distributed Systems - Sem2 2018
 *  Class to hold the board of scrabble and its values
 */

public class Board {
	// This is the board that will hold all the values
	private char[][] objBoard = new char[20][20];
	
	public Board() {
		// @ is for when we start the game
		setSpace(200, '@');
	}

	public char[][] getObjBoard() {
		return objBoard;
	}

	//Returns true if letter was able to be placed
	public boolean setSpace(int space,char input){
		int x = space/20;
		int y = space%20;
		// case when we begin the game
		if (input == '@') {
			return false;
		}

		else {
			objBoard[x][y]=input;
			return true;
		}

	}
	
	public char getLetter(int space) {
		int x = space/20;
		int y = space%20;
		return objBoard[x][y];
	}
	
}
