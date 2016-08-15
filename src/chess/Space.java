/**
 * @author Simon Wang, Michael Coyle
 */

package chess;

public class Space {
	private int file;
	private int rank;
	public Piece piece;

	/**
	 * Constructor for the Space object
	 * 
	 * @param file		The x-coordinate of the Space
	 * @param rank		The y-coordinate of the Space
	 */
	public Space(int file, int rank){
		this.file = file;
		this.rank = rank;
		this.piece = null;
	}
	
	/**
	 * Checks if a space is in bounds or not
	 * @return		Returns true if it is and false if it's not
	 */
	public boolean isValid(){
    	return !(file < 0 || file > 7 || rank < 0 || rank > 7);
	}
	
	/**
	 * Checks if a Space is occupied.
	 * 
	 * @return			Returns true if it's occupied and false if it's not
	 */
	public boolean isOccupied(){
		return piece != null;
	}
	
	public int getFile(){
		return file;
	}
	
	public int getRank(){
		return rank;
	}
	
	public Piece getPiece(){
		return piece;
	}

	public void setPiece(Piece piece){
		this.piece = piece;
	}
	
	/**
	 * Called when moving a Piece off of the current space
	 * 
	 * Copies a temporary Piece variable, sets this Space's instance of Piece to null,
	 * and then returns the temporary Piece for reference. Doesn't care if the space is
	 * empty to begin with.
	 * 
	 * @return			Returns whatever Piece was on the Space, if any.
	 */
	public Piece freeSpace(){
		Piece temp = this.piece;
		this.piece = null;
		return temp;
	}
	
	public boolean equals(Space s){
		return this.file == s.file && this.rank == s.rank;
	}
	
	public String toString(){
		return file +", " +rank;
	}
}
