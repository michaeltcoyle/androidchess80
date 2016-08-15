/**
 * @author Simon Wang
 */

package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Piece;
import chess.Space;

public class Knight extends Piece {

	public Knight(int file, int rank, boolean isWhite) {
		super(file, rank, isWhite);
	}
	
	public void setMoveList(Board board){
		int currFile = super.getFile();
		int currRank = super.getRank();
		
		ArrayList<Space> moves = new ArrayList<Space>();
		ArrayList<Space> protects = new ArrayList<Space>();
		
		// can iterate through these to examine the 8 possible knight squares without lots of reused code
		int[] horizontal = {2, -2, 1, -1, 2, -2, 1, -1};
		int[] vertical = {1, 1, 2, 2, -1, -1, -2, -2};
		
		for(int i = 0; i < 8; i++){
			int tempFile = currFile + horizontal[i];
			int tempRank = currRank + vertical[i];
			
			Space s = new Space(tempFile, tempRank);
			if(s.isValid()){
				if (board.getSpace(tempFile, tempRank).isOccupied()){
					if (board.getSpace(tempFile, tempRank).getPiece().getColor() != this.getColor()){
						moves.add(s);
					}else{
						protects.add(s);
					}
				}else{
					moves.add(s);
				}
			}
		}
		moveList = moves;
		protectList = protects;
	}
}
