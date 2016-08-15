/**
 * @author Simon Wang, Michael Coyle
 */

package chess;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class Board {
	public static Space[][] board = new Space[8][8];

 	/**
 	 * Constructor for the Board object
 	 */
    public Board() {
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                Board.board[i][j] = new Space(i, j);
            }
        }
    }

    /**
     * Returns the Space at the given coordinates
     * 
     * @param file		The file of the Space to be found
     * @param rank		The rank of the Space to be found
     * @return
     */
    public Space getSpace(int file, int rank){
    	if(file < 0 || file > 7 || rank < 0 || rank > 7)
    		return null;
    	
        return board[file][rank];
    }
    
    /**
     * Called when a VALID move has been cleared by the program - we now move the actual piece
     * 
     * @param p
     * @param newFile
     * @param newRank
     */
    public Piece movePiece(Piece p, Space newSpace){
    	Space s = board[newSpace.getFile()][newSpace.getRank()];
    	Piece ret = null;
    	
    	if(s.isOccupied()){					// capture enemy piece
    		s.getPiece().setAlive(false);	// piece is kill
    		ret = s.getPiece();
    	}
    	
    	if(p instanceof Pawn){
    		((Pawn)p).movePawn(true);
    		
    		// promotion code here probably
    		
    	}else if(p instanceof King){
    		
    		((King)p).moveKing();
    		
    		if(Math.abs(p.getFile() - newSpace.getFile()) > 1){		// castling
    			Piece pCastle = null;
    			Space newSpaceCastle = null;
    			
				if(p.getFile() - newSpace.getFile() == -2){			// castle kingside
					pCastle = board[p.getFile() + 3][p.getRank()].getPiece();
					newSpaceCastle = board[p.getFile() + 1][p.getRank()];
					board[p.getFile() + 3][p.getRank()].setPiece(null);
				}else if(p.getFile() - newSpace.getFile() == 2){	// castle queenside
					pCastle = board[p.getFile() - 4][p.getRank()].getPiece();
					newSpaceCastle = board[p.getFile() - 1][p.getRank()];
					board[p.getFile() - 4][p.getRank()].setPiece(null);
				}
				
				newSpaceCastle.setPiece(pCastle);
				pCastle.movePiece(newSpaceCastle);
				((Rook)pCastle).moveRook(true);
    		}
			
    	}else if(p instanceof Rook){
    		
    		((Rook)p).moveRook(true);
    		
    	}
    	
    	s.setPiece(p);						// update space with its new piece
    	p.movePiece(newSpace);				// update piece with its new space
    	
    	return ret;
    }
    
    /**
     * Prints out the current content of the board, with file-rank labels on the right and bottom.
     */
    public void printBoard(){
    	String[][] output = new String[9][9];
    	int i;
    	
		/* The code in this method is intentionally weird because we need to "flip" the board
		 * Java's way of reading 2D arrays is different from how the file-rank system of chess
		 * is set up
		 * */
    	
    	for(i = 0; i < 8; i++){
    		for(int j = 0; j < 8; j++){
    			
    			
    			Piece p = board[i][7-j].piece;
    			if(p instanceof Pawn){
    				if(p.isWhite){
    					output[j][i] = "wP";
    				}else{
    					output[j][i] = "bP";
    				}
    			}else if(p instanceof Rook){
    				if(p.isWhite){
    					output[j][i] = "wR";
    				}else{
    					output[j][i] = "bR";
    				}
    			}else if(p instanceof Bishop){
    				if(p.isWhite){
    					output[j][i] = "wB";
    				}else{
    					output[j][i] = "bB";
    				}
    			}else if(p instanceof Knight){
    				if(p.isWhite){
    					output[j][i] = "wN";
    				}else{
    					output[j][i] = "bN";
    				}
    			}else if(p instanceof Queen){
    				if(p.isWhite){
    					output[j][i] = "wQ";
    				}else{
    					output[j][i] = "bQ";
    				}
    			}else if(p instanceof King){
    				if(p.isWhite){
    					output[j][i] = "wK";
    				}else{
    					output[j][i] = "bK";
    				}
    			}else{	// square is a blank space, determine if we need to make it black or white
	    			if(((i & 1) == 0) && ((j & 1) == 0)){			// if i and j are even
	    				output[j][i] = "  ";
	    			}else if(((i & 1) != 0) && ((j & 1) != 0)){		// if i and j are odd
	    				output[j][i] = "  ";
	    			}else{											// these lines create the black and white chessboard pattern
	    				output[j][i] = "##";
	    			}
    			}
    		}
    	}
    	
    	String[] files = {"a ", "b ", "c ", "d ", "e ", "f ", "g ", "h "};
    	for(i = 0; i < 8; i++){
    		output[8][i] = files[i];
    		output[i][8] = Integer.toString(8 - i);
    	}
    	output[8][8] = " ";
    	
    	System.out.println();
    	
    	for(i = 0; i < 9; i++){
    		for(int j = 0; j < 9; j++){
    			System.out.print(output[i][j] +" ");
    		}
    		System.out.println();
    	}
    	System.out.println();
    }
}
