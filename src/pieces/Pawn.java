/**
 * @author Simon Wang, Michael Coyle
 */

package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Piece;
import chess.Space;

public class Pawn extends Piece {
	
	private boolean hasMoved;
	private boolean hasMovedLastTurn;	// needed for en passant

	public Pawn(int file, int rank, boolean isWhite) {
		super(file, rank, isWhite);
		moveList = new ArrayList<Space>();
		hasMoved = false;
		hasMovedLastTurn = false;
	}

	public boolean hasMoved(){
		return hasMoved;
	}
	
	public boolean hasMovedLastTurn(){
		return hasMovedLastTurn;
	}
	
	public void movePawn(boolean b){
		hasMoved = b;
	}
	
	public void pawnMovedLastTurn(){
		hasMovedLastTurn = true;
	}
	
	public void setMoveList(Board board){
		
		int currFile = super.getFile();
		int currRank = super.getRank();
		
		ArrayList<Space> moves = new ArrayList<Space>();
		ArrayList<Space> protects = new ArrayList<Space>();
		
		if (super.getColor()){ //white
			if (currRank + 1 <= 7){ //in board bounds (1 space move)
				if(board.getSpace(currFile, currRank + 1).isOccupied() == false){
					moves.add(new Space(currFile, currRank+1));
				}
			}
			if (hasMoved() == false && currRank + 2 <= 7){ //in board bounds (2 space move first turn)
				if(board.getSpace(currFile, currRank + 2).isOccupied() == false){
					moves.add(new Space(currFile, currRank+2));
				}
			}
			if (currRank + 1 <= 7 && currFile + 1 <= 7){ //forward-right capture (white perspective)
				if (board.getSpace(currFile + 1, currRank + 1).isOccupied()){
					if (board.getSpace(currFile + 1, currRank + 1).getPiece().getColor() == false){ //check for black piece in space
						moves.add(new Space(currFile+1, currRank+1));
					}else{
						protects.add(new Space(currFile+1, currRank+1));
					}
				}
			}
			if (currRank + 1 <= 7 && currFile - 1 >= 0){ //forward-left capture (white perspective)
				if (board.getSpace(currFile - 1, currRank + 1).isOccupied()){ 
					if (board.getSpace(currFile - 1, currRank + 1).getPiece().getColor() == false){ //check for black piece in space
						moves.add(new Space(currFile-1, currRank+1));
					}else{
						protects.add(new Space(currFile-1, currRank+1));
					}
				}
			}
		}else{ //black
			if (currRank - 1 >= 0){ //in board bounds (1 space move)
				if(board.getSpace(currFile, currRank - 1).isOccupied() == false){
					moves.add(new Space(currFile, currRank-1));
				}
			}
			if (hasMoved() == false && currRank - 2 >= 0){ //in board bounds (2 space move first turn)
				if(board.getSpace(currFile, currRank - 2).isOccupied() == false){
					moves.add(new Space(currFile, currRank-2));
				}
			}
			if (currRank - 1 >= 0 && currFile + 1 <= 7){ //forward-left capture (black perspective)
				if (board.getSpace(currFile + 1, currRank - 1).isOccupied()){
					if (board.getSpace(currFile + 1, currRank - 1).getPiece().getColor() == true){ //check for black piece in space
						moves.add(new Space(currFile+1, currRank-1));
					}else{
						protects.add(new Space(currFile+1, currRank-1));
					}
				}
			}
			if (currRank - 1 >= 0 && currFile - 1 >= 0){ //forward-right capture (black perspective)
				if (board.getSpace(currFile - 1, currRank - 1).isOccupied()){
					if (board.getSpace(currFile - 1, currRank - 1).getPiece().getColor() == true){ //check for black piece in space
						moves.add(new Space(currFile-1, currRank-1));
					}else{
						protects.add(new Space(currFile-1, currRank-1));
					}
				}
			}
		}
		
		moveList = moves;
		protectList = protects;
	}

	
	/**
	 * Given a pawn, checks if it's currently capable of doing en passant. If it is, it returns its destination square.
	 * 
	 * Since a pawn can only en passant on a just-recently moved enemy pawn, only one square is available for said pawn
	 * to do en passant on any given turn. However, two different pawns can en passant to capture the same pawn.
	 * 
	 * @param whiteToMove		Whether it's white's turn or not
	 * @param board				The current state of the board
	 * @return					Returns the destination space if en passant can be done, and null otherwise
	 */
	public Space checkEnPassant(boolean whiteToMove, Board board){
		
		int vert;
		if(whiteToMove){
			if(getRank() != 4)
				return null;
			
			vert = 1;	// moving towards rank 8
		}else{
			if(getRank() != 3)
				return null;
			
			vert = -1;	// moving towards rank 1
		}
		
		int[] neighbors = {-1, 1};
		
		for(int i : neighbors){
			Piece p = board.getSpace(getFile() + i, getRank()).getPiece();			// if neighboring pieces are pawns
			if(p != null){
				if(p instanceof Pawn){
					if(((Pawn)p).hasMoved() == true && ((Pawn)p).hasMovedLastTurn() == false){				// and they just moved 2 spaces
						return new Space(getFile() + i, getRank() + vert);			// we can en passant
					}
				}
			}
		}
		
		return null;
	}
	
}
