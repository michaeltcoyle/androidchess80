/**
 * @author Michael Coyle
 */

package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Piece;
import chess.Space;

public class Queen extends Piece {

	public Queen(int file, int rank, boolean isWhite) {
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
