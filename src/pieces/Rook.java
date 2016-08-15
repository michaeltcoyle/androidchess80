/**
 * @author Michael Coyle
 */

package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Piece;
import chess.Space;

public class Rook extends Piece {

	// needed for castling
	private boolean hasMoved;
	
	// needed for move undoing
	private boolean hasMovedLastTurn;
	
	public Rook(int file, int rank, boolean isWhite) {
		super(file, rank, isWhite);
		hasMoved = false;
		hasMovedLastTurn = false;
	}
	
	public boolean hasMoved(){
		return hasMoved;
	}
	
	public boolean hasMovedLastTurn(){
		return hasMovedLastTurn;
	}
	
	public void moveRook(boolean b){
		hasMoved = b;
	}
	
	public void rookMovedLastTurn(){
		hasMovedLastTurn = true;
	}
	
	public void setMoveList(Board board){
		
		int currFile = super.getFile();
		int currRank = super.getRank();
		
		ArrayList<Space> moves = new ArrayList<Space>();
		ArrayList<Space> protects = new ArrayList<Space>();
		
		for (int i = currRank+1; i <= 7; i++){ //vertical positive direction
			if (board.getSpace(currFile, i).isOccupied()){
				if (board.getSpace(currFile, i).getPiece().getColor() != this.getColor()){
					moves.add(new Space(currFile, i));
					break;
				}else{
					protects.add(new Space(currFile, i));
					break;
				}
			}else{
				moves.add(new Space(currFile, i));
			}
		}
		for (int i = currRank-1; i >= 0; i--){ //vertical negative direction
			if (board.getSpace(currFile, i).isOccupied()){
				if (board.getSpace(currFile, i).getPiece().getColor() != this.getColor()){
					moves.add(new Space(currFile, i));
					break;
				}else{
					protects.add(new Space(currFile, i));
					break;
				}
			}else{
				moves.add(new Space(currFile, i));
			}
		}	
		for (int i = currFile+1; i <= 7; i++){ //horizontal positive direction
			if (board.getSpace(i, currRank).isOccupied()){
				if (board.getSpace(i, currRank).getPiece().getColor() != this.getColor()){
					moves.add(new Space(i, currRank));
					break;
				}else{
					protects.add(new Space(i, currRank));
					break;
				}
			}else{
				moves.add(new Space(i, currRank));
			}
		}	
		for (int i = currFile-1; i >= 0; i--){ //horizontal negative direction
			if (board.getSpace(i, currRank).isOccupied()){
				if (board.getSpace(i, currRank).getPiece().getColor() != this.getColor()){
					moves.add(new Space(i, currRank));
					break;
				}else{
					protects.add(new Space(i, currRank));
					break;
				}
			}else{
				moves.add(new Space(i, currRank));
			}
		}			
		
		moveList = moves;
		protectList = protects;
	}
}
