/**
 * @author Simon Wang, Michael Coyle
 */

package chess;

import java.util.ArrayList;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class Piece {
	
	protected ArrayList<Space> moveList;
	protected ArrayList<Space> protectList;
	
	private int file;
	private int rank;
	private boolean alive;
	
	boolean isWhite;		// this is here because we're lazy
	
	/**
	 * Constructor for the Space object
	 * 
	 * @param file			The current file of the piece
	 * @param rank			The current rank of the piece
	 */
	public Piece(int file, int rank, boolean isWhite){
		moveList = new ArrayList<Space>();
		protectList = new ArrayList<Space>();
		
		this.file = file;
		this.rank = rank;
		this.isWhite = isWhite;
		alive = true;
	}
	
	/**
	 * Tells the program if a piece has been captured or not
	 * 
	 * @return				Returns true if the piece is still live and false otherwise
	 */
	public boolean isAlive(){
		return alive;
	}
	
	/**
	 * Changes the status of a piece - whether it's alive or dead
	 * 
	 * @param alive			The new status of the piece
	 */
	public void setAlive(boolean alive){
		this.alive = alive;
	}

	/**
	 * Gets the file of a piece
	 * 
	 * @return				See above
	 */
	public int getFile(){
		return file;
	}
	
	/**
	 * Gets the rank of a piece
	 * 
	 * @return				See above
	 */
	public int getRank(){
		return rank;
	}
	
	/**
	 * Gets the color of a piece
	 * 
	 * @return				Returns true if the piece is white and false if it's black
	 */
	public boolean getColor(){
		return isWhite;
	}
	
	/**
	 * Called when a VALID move has been cleared by the program - we now move the actual piece
	 * 
	 * Will be called from Board.java
	 * 
	 * @param newSpace
	 */
	public void movePiece(Space newSpace){
		this.file = newSpace.getFile();
		this.rank = newSpace.getRank();
	}
	
	/**
	 * toString method used in debugging
	 */
	/*public String toString(){
		return "Generic piece: " +file +", " +rank;
	}*/
    public String toString(){
        StringBuilder ret = new StringBuilder();
        if(isWhite){
                ret.append('w');
        }else{
                ret.append('b');
        }
       
        if(this instanceof Pawn)
                ret.append('P');
        else if(this instanceof Rook)
                ret.append('R');
        else if(this instanceof Knight)
                ret.append('N');
        else if(this instanceof Bishop)
                ret.append('B');
        else if(this instanceof Queen)
                ret.append('Q');
        else if(this instanceof King)
                ret.append('K');
       
        return ret.toString();
        
}
	
	/**
	 * for debugging
	 * prints out all available moves for the current piece
	 */
	public void printMoveList(){
		if(moveList == null){
			System.out.println("Piece at " +file +", " +rank +" has no moves.");
		}else if(moveList.isEmpty()){
			System.out.println("Piece at " +file +", " +rank +" has no moves.");
		}else{
			System.out.println("List of valid moves for piece at " +file +", " +rank +":");
			for(Space s : moveList){
				System.out.println(s);
			}
		}
	}
}
