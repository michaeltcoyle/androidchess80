/**
 * @author Michael Coyle
 */

package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Piece;
import chess.Space;

public class Bishop extends Piece {

	public Bishop(int file, int rank, boolean isWhite) {
		super(file, rank, isWhite);
	}
	
	public void setMoveList(Board board){
		
		int currFile = super.getFile();
		int currRank = super.getRank();
		
		ArrayList<Space> moves = new ArrayList<Space>();
		ArrayList<Space> protects = new ArrayList<Space>();
		
		for (int i = 1; currRank+i <= 7 && currFile+i <=7; i++){ //up-right direction
			if (board.getSpace(currFile+i, currRank+i).isOccupied()){
				if (board.getSpace(currFile+i, currRank+i).getPiece().getColor() != this.getColor()){
					moves.add(new Space(currFile+i, currRank+i));
					break;
				}else{
					protects.add(new Space(currFile+i, currRank+i));
					break;
				}
			}else{
				moves.add(new Space(currFile+i, currRank+i));
			}
		}
		for (int i = 1; currRank-i >= 0 && currFile-i >= 0; i++){ //down-left direction
			if (board.getSpace(currFile-i, currRank-i).isOccupied()){
				if (board.getSpace(currFile-i, currRank-i).getPiece().getColor() != this.getColor()){
					moves.add(new Space(currFile-i, currRank-i));
					break;
				}else{
					protects.add(new Space(currFile-i, currRank-i));
					break;
				}
			}else{
				moves.add(new Space(currFile-i, currRank-i));
			}
		}	
		for (int i = 1; currRank+i <= 7 && currFile-i >= 0; i++){ //up-left direction
			if (board.getSpace(currFile-i, currRank+i).isOccupied()){
				if (board.getSpace(currFile-i, currRank+i).getPiece().getColor() != this.getColor()){
					moves.add(new Space(currFile-i, currRank+i));
					break;
				}else{
					protects.add(new Space(currFile-i, currRank+i));
					break;
				}
			}else{
				moves.add(new Space(currFile-i, currRank+i));
			}
		}	
		for (int i = 1; currRank-i >= 0 && currFile+i <= 7; i++){ //down-right direction
			if (board.getSpace(currFile+i, currRank-i).isOccupied()){
				if (board.getSpace(currFile+i, currRank-i).getPiece().getColor() != this.getColor()){
					moves.add(new Space(currFile+i, currRank-i));
					break;
				}else{
					protects.add(new Space(currFile+i, currRank-i));
					break;
				}
			}else{
				moves.add(new Space(currFile+i, currRank-i));
			}
		}	
		
		moveList = moves;
		protectList = protects;
	}
}



