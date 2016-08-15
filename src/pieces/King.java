/**
 * @author Simon Wang
 */

package pieces;

import java.util.ArrayList;

import chess.Board;
import chess.Piece;
import chess.Space;

public class King extends Piece {
	// needed for castling
	private boolean hasMoved;
	
	public King(int file, int rank, boolean isWhite) {
		super(file, rank, isWhite);
		hasMoved = false;
	}
	
	public void moveKing(){
		hasMoved = true;
	}
	
	public void setMoveList(Board board){
		int currFile = super.getFile();
		int currRank = super.getRank();
		
		ArrayList<Space> moves = new ArrayList<Space>();
		ArrayList<Space> protects = new ArrayList<Space>();
		
		// can iterate through these to examine the 8 possible king squares without lots of reused code
		int[] horizontal = {1, 1, 1, -1, -1, -1, 0, 0};
		int[] vertical = {1, 0, -1, 1, 0, -1, 1, -1};
		
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
		
		// implement castling
		if(hasMoved == false){
			// if king hasn't moved, it must be on e-file
			if(board.getSpace(currFile + 3, currRank).isOccupied()){				// castle kingside, make sure rook is there
				Piece pCastle = board.getSpace(currFile + 3, currRank).getPiece();	// check kingside rook
				if(pCastle instanceof Rook){
					if(((Rook)pCastle).hasMoved() == false){
						if(board.getSpace(currFile + 1, currRank).isOccupied() == false
							&& board.getSpace(currFile + 2, currRank).isOccupied() == false)
								moves.add(new Space(currFile + 2, currRank));
					}
				}
			}
			if(board.getSpace(currFile - 4, currRank).isOccupied()){				// castle queenside, make sure rook is there
				Piece pCastle = board.getSpace(currFile - 4, currRank).getPiece();	// check queenside rook
				if(pCastle instanceof Rook){
					if(((Rook)pCastle).hasMoved() == false){
						if(board.getSpace(currFile - 1, currRank).isOccupied() == false
							&& board.getSpace(currFile - 2, currRank).isOccupied() == false
								&& board.getSpace(currFile - 3, currRank).isOccupied() == false)
									moves.add(new Space(currFile - 2, currRank));
					}
				}
			}
		}
		
		moveList = moves;
		protectList = protects;
	}

}
