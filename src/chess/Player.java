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

public class Player {
	
	private int id; //player id, 1 is white, 2 is black
	
	Piece[] owned_Pieces;
	// owned_Pieces 0-7 are pawns. 8 and 9 are rooks, 10 and 11 knights, 12 and 13 bishops, 14 the queen, and 15 the king.
	
	ArrayList<Space> threatenedSquares;
	// an arrayList of the spaces controlled by the current player - if a piece were to move onto one of these squares, it could be captured the next turn.
	
	int numMoves;
	
	public Player(int pid){
		id = pid;
		numMoves = 0;
		owned_Pieces = new Piece[16];
		threatenedSquares = new ArrayList<Space>();
		
		if (id == 1){
			// create pawns
			for (int i = 0; i < 8; i++){
				owned_Pieces[i] = new Pawn(i, 1, true);
			}
			//create rooks
			owned_Pieces[8] = new Rook(0, 0, true);
			owned_Pieces[9] = new Rook(7, 0, true);
			//create knights
			owned_Pieces[10] = new Knight(1, 0, true);
			owned_Pieces[11] = new Knight(6, 0, true);
			//create bishops
			owned_Pieces[12] = new Bishop(2, 0, true);
			owned_Pieces[13] = new Bishop(5, 0, true);
			//create queen
			owned_Pieces[14] = new Queen(3, 0, true);
			//create king
			owned_Pieces[15] = new King(4, 0, true);
		}
		else if (id == 2){
			// create pawns
			for (int i = 0; i < 8; i++){
				owned_Pieces[i] = new Pawn(i, 6, false);
			}
			//create rooks
			owned_Pieces[8] = new Rook(0, 7, false);
			owned_Pieces[9] = new Rook(7, 7, false);
			//create knights
			owned_Pieces[10] = new Knight(1, 7, false);
			owned_Pieces[11] = new Knight(6, 7, false);
			//create bishops
			owned_Pieces[12] = new Bishop(2, 7, false);
			owned_Pieces[13] = new Bishop(5, 7, false);
			//create queen
			owned_Pieces[14] = new Queen(3, 7, false);
			//create king
			owned_Pieces[15] = new King(4, 7, false);
		}
	}
	
	public void setPieceMoveLists(Board board){
		numMoves = 0;
		
		
		for(Piece p: owned_Pieces){
			if(p != null){
				p.moveList.clear();
				p.protectList.clear();
				
				if(p instanceof Pawn)
					((Pawn)p).setMoveList(board);
				else if(p instanceof Rook)
					((Rook)p).setMoveList(board);
				else if(p instanceof Knight)
					((Knight)p).setMoveList(board);
				else if(p instanceof Bishop)
					((Bishop)p).setMoveList(board);
				else if(p instanceof Queen)
					((Queen)p).setMoveList(board);
				else if(p instanceof King)
					((King)p).setMoveList(board);
				
				numMoves += p.moveList.size();
			}
		}
	}
	
	public int getId(){
		return id;
	}
	
	public void updateOwnedPieces(){
		for (int i = 0; i < 16; i++){
			if(owned_Pieces[i] != null){
				if(owned_Pieces[i].isAlive() == false){
					owned_Pieces[i] = null;
				}
			}
		}
	}
	
	/**
	 * Updates the list of squares that the player "threatens"
	 * 
	 * If at the end of a turn, the enemy king is on one of these squares, the enemy king is in check.
	 * If at the end of an attempted move, the player's king is in check, the move is not legal.
	 */
	public void updateThreatenedSquares(){
		threatenedSquares.clear();
		
		for(Piece p : owned_Pieces){
			if(p != null){
				
				if(p.moveList != null){
					for(Space s : p.moveList){
						if(p instanceof Pawn){
							if(s.getFile() != p.getFile())			// pawns can only capture diagonally
								threatenedSquares.add(s);
						}else{
							threatenedSquares.add(s);
						}
					}
				}
				
				if(p.protectList != null){
					for(Space s : p.protectList){					// any piece that a piece protects is dangerous for the enemy king
						threatenedSquares.add(s);
					}
				}
			}
		}
	}
	
	/**
	 * Checks if hasMoved fields need updating
	 * 
	 * The Pawn and Rook classes have a "hasMovedLastTurn" field, which is required to undo moves.
	 * This method is called every time a player moves, and updates the correct fields accordingly.
	 */
	
	// technically the field should be called justMoved but this is more consistent
	public void updateHasMoved(){
		// iterate through first 9 elements of owned_Pieces, which represent the pawns and rooks
		for(int i = 0; i < 8; i ++){	// pawns	
			if(owned_Pieces[i] instanceof Pawn && owned_Pieces[i] != null){	// need instanceof check in case we promoted something
				Pawn p = (Pawn)owned_Pieces[i];
				
	    		if(p.hasMoved() == true && p.hasMovedLastTurn() == false)
	    			p.pawnMovedLastTurn();

			}
		}
		for(int i = 8; i < 10; i ++){	// rooks
			Rook r = (Rook)owned_Pieces[i];									// no instanceof check here needed since rooks can't be promoted
																			// we don't need to find promoted rooks because they've always been moved
			if(r != null){
	    		if(r.hasMoved() == true && r.hasMovedLastTurn() == false)
	    			r.rookMovedLastTurn();
			}
		}
	}
	
	public Piece getKing(){
		return owned_Pieces[15];
	}
}

