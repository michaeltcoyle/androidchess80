/**
 * @author Simon Wang, Michael Coyle
 */

package chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

public class Chess {
	
	public Board gameBoard;			// the chessboard
	
	private Player player1; 	// white
	private Player player2; 	// black
	public boolean whiteToMove;
	public boolean inCheck;
	
	private Piece limbo;		// this is where pieces go when they're captured to determine if they get sent to heaven or hell
	
	private boolean justPromoted;	// if we promoted a piece last turn, switch this flag (used to set tryUndo)
	private boolean undoUsed;		// can only call undo once per turn!
	
	// this stuff is for recording games
	private String gameTitle;
	
	private String filename;
	private String recordname;
	private File file;
	private FileWriter fw;
	
	public Chess(){
		this.gameBoard = new Board();
		
		this.player1 = new Player(1); 	// create players with ids (1 = white, 2 = black)
		this.player2 = new Player(2);
		whiteToMove = true; 			// white moves first
		inCheck = false;
		
		justPromoted = false;
		undoUsed = false;
		
		initializeFileWriter();
	}
	
	private void initializeFileWriter(){
		try{
			
			filename = new File(".").getCanonicalPath() +"\\" +"saved\\";
			
			File f = new File(filename + "counter.txt");
			if(f.exists()) {
				Scanner in = new Scanner(new FileReader(f));
				int i = Integer.parseInt(in.next());
				in.close();
				
				i++;
				recordname = "game" +i +".txt";
				
				
				FileWriter out = new FileWriter(f);
	            out.write(String.valueOf(i));
	            out.close();
			}else{
	            FileWriter out = new FileWriter(f);
	            out.write("1");
	            out.close();
	            
	            recordname = "game1.txt";
			}
			
			fw = new FileWriter(filename + recordname);	// opens file writer
			// System.out.println("success");
		}catch(IOException e){
			// System.out.println("failure");
		}
	}
	
	private void recordMove(String s) throws IOException{
		fw.write(s + "\n");
	}
	
	private void closewriter(){
		try{
			
			Scanner reader = new Scanner(System.in);	// take this out when we move to Android
			String line = "";
			
			// TODO: For Android - prompt user if they want to save a record of this game
			// Make a pop-up box, maybe?
			System.out.println("Do you want to save this game? (Y/N)");
			line = reader.nextLine();
			
			if(line.equals("Y")){
				System.out.println("Please enter a title for this game.");
				line = reader.nextLine();
				if(line.isEmpty())
					gameTitle = "Untitled Game";
				else
					gameTitle = line;
				
				fw.write("Title: " +gameTitle);
			}else{
				File f = new File(filename + "counter.txt");
				if(f.exists()) {
					Scanner in = new Scanner(new FileReader(f));
					int i = Integer.parseInt(in.next());
					in.close();
					
					FileWriter out = new FileWriter(f);
		            out.write(String.valueOf(i-1));
		            out.close();
				}
			}
			
			reader.close();
			fw.close();
		}catch(IOException e){
			
		}
	}
	
	private String getTitle(File savedGame) throws FileNotFoundException{
		String ret = "";
		boolean found = false;
		
		Scanner scanner = new Scanner(new FileReader(savedGame));
		
		while (scanner.hasNextLine()) {
	        ret = scanner.nextLine();
	        if(ret.length() > 7){		// search for "Title:" line
		        if(ret.substring(0, 5).equals("Title")){ 
		            ret = ret.substring(7);
		            found = true;
			    }
	        }
		}
		 
		scanner.close();
		
		if(found)
			return ret;
		else
			return "";
	}
	
	private void getSavedGames() throws FileNotFoundException{
		File folder = new File(filename);
	    for (final File fileEntry : folder.listFiles()){
	    	if(fileEntry.getName().substring(0, 4).equals("game")){
	    		String title = getTitle(fileEntry);
	    		if(title.isEmpty() == false){				// not a valid game otherwise
		    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");	
		    		String date = sdf.format(fileEntry.lastModified());
		    		System.out.println(title +"\t\t" +date);
	    		}
	    	}
	    }
	}
	
	
	
	
	/**
	 * Adds chess pieces in their starting positions.
	 */
	public void setBoard(){
        for(Piece p : player1.owned_Pieces){
        	if(p != null)
        		gameBoard.board[p.getFile()][p.getRank()].piece = p;
        }
        
        for(Piece p : player2.owned_Pieces){
        	if(p != null)
        		gameBoard.board[p.getFile()][p.getRank()].piece = p;
        }
	}
	
	public String AImove(){
		
		updateGameState();
		
		Player currPlayer;
		
		if(whiteToMove)
			currPlayer = player1;
		else
			currPlayer = player2;
		
		for(Piece p: currPlayer.owned_Pieces){
			if(p != null){
				if(p.moveList.isEmpty() == false){
					Space start = gameBoard.board[p.getFile()][p.getRank()];
					Space dest = p.moveList.get(0);
					
					if(p instanceof Pawn){
						if((p.getColor() == true && dest.getRank() == 7) || (p.getColor() == false && dest.getRank() == 0)){
							p = new Queen(dest.getFile(), dest.getRank(), p.getColor());		// autopromote to queen
							justPromoted = true;
						}
					}else{
						justPromoted = false;
					}
					
					start.setPiece(null);
					limbo = gameBoard.movePiece(p, dest);		// make the first move we can

					undoUsed = true;			// can't call undo after an AI move
					whiteToMove = !whiteToMove;
					
					if(checkGameOver())		// tells us if next player is checkmated
						return "Checkmate";
					
					return "";		// valid move that doesn't end the game
				}
			}
		}
		
		return "AI could not find a move";
	}
	
	/**
	 * Determines whether the game is over. If the game is not over, determines whether the current player's king is in check.
	 * 
	 * @return		Returns true if checkmate and false otherwise
	 */
	private boolean checkGameOver(){
		Player currPlayer;
		
		if(whiteToMove)
			currPlayer = player1;
		else
			currPlayer = player2;
		
		if(isMyKingSafe() == false){
			
			if(currPlayer.getKing().moveList.size() == 0){
				return true;
			}
			
			inCheck = true;
		}else{
			inCheck = false;
		}
		
		return false;
	}
	
	/**
	 * Checks if the player's king is safe
	 * 
	 * This method is called before a player's move to determine if the player's king is in check. If it is, the program
	 * prints "Check". If the player's king has no moves, the game ends in a victory for the opposing player.
	 * 
	 * This method is also called after attempting to make a move to determine if it would endanger the player's king.
	 * If it does, the move is invalid and is reversed, and the player is prompted to move again. 
	 * 
	 * @return		Returns true if the king is safe and false if he is not.
	 */
	private boolean isMyKingSafe(){
		Player enemyPlayer;
		
		if(whiteToMove)
			enemyPlayer = player2;
		else
			enemyPlayer = player1;
		
		for(Space s : enemyPlayer.threatenedSquares){
			Piece p = gameBoard.getSpace(s.getFile(), s.getRank()).getPiece();
			if(p instanceof King && p.getColor() == whiteToMove){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Updates the list of squares to which the king can legally move
	 * 
	 * Maintaining a list of spaces that the king can move to is necessary in order to properly and efficiently
	 * check whether checkmate has occurred.
	 * 
	 * The code looks at all of the squares that the enemy player is threatening (can capture next turn) and removes any of them
	 * that may be in the player's king's list of viable moves.
	 * 
	 * @param pl		The player whose king's moveList is to be updated
	 */
	private void updateKingMoveList(Player pl){
		
		Piece king = pl.getKing();
		
		Object[] list1 = king.moveList.toArray();		// THIS NEEDS TO BE AN ARRAY OR ELSE THE CODE BREAKS BECAUSE ARRAYLISTS ARE FUCKING STUPID
		ArrayList<Space> list2 = null;
		if(pl.getId() == 1)
			list2 = player2.threatenedSquares;
		else if(pl.getId() == 2)
			list2 = player1.threatenedSquares;
		
		for(int i = 0; i < list1.length; i++){				// we iterate through the enemy player's threatenedSquares list and remove
			Space s1 = (Space)list1[i];						// any of them from the current player's king's list of possible moves
			
			if(Math.abs(s1.getFile() - king.getFile()) > 1){	// if we're castling we need to make sure all squares b/t king and rook are safe
				Space between = null;
				
				if(king.getFile() - s1.getFile() == -2){			// castle kingside
					between = new Space(king.getFile() + 1, king.getRank());
				}else if(king.getFile() - s1.getFile() == 2){		// castle queenside
					between = new Space(king.getFile() - 1, king.getRank());
				}
				
				for(Space s2 : list2){						// if the king needs to travel through unsafe squares to castle, he can't castle
					if(s1.equals(s2) || between.equals(s2)){
						list1[i] = null;
						break;
					}
				}
				
			}else{
				for(Space s2 : list2){
					if(s1.equals(s2)){
						list1[i] = null;
						break;
					}
				}
			}
		}
		
		pl.getKing().moveList.clear();						// clear old movelist and prepare to insert new one
		
		for(int i = 0; i < list1.length; i++){
			if(list1[i] != null)
				pl.getKing().moveList.add((Space)list1[i]);
		}
	}
	
	/**
	 * See if we can move a piece from one space to another, and move the piece if we can.
	 * 
	 * Each Piece owned by a Player has a list of possible moves. If the move we're trying to do
	 * isn't in that list, we return false.
	 * 
	 * @param startSpace		The starting space of the piece
	 * @param endSpace			The desired destination of the piece
	 * @return					Returns true if the piece was successfully moved and false otherwise
	 */
	private boolean tryMove(String startSpace, String endSpace){
		Space start = parseSpace(startSpace);
		// Check to see if our starting position is valid
		if(start == null){
			// System.out.println("Illegal move, try again");
		}else if(!start.isOccupied()){
			// System.out.println("Square " +startSpace +" is empty! Enter another move.");
		}else{
			// Check to see if where we end up is occupied
			Piece p = start.getPiece();
			if(p.getColor() == whiteToMove){		// piece we're moving needs to match the player who's going
				
				if(p.moveList.isEmpty()){			// If piece can't move anywhere, we can ignore it
					return false;
				}
				
				Space dest = parseSpace(endSpace);
				if(dest == null){
					return false;
				}
				
				if(p instanceof Pawn){				// cannot promote with a 2-argument input
					if(p.getColor() == true && dest.getRank() == 7){
						return false;
					}else if(p.getColor() == false && dest.getRank() == 0){
						return false;
					}
					
					Space enPassant = ((Pawn)p).checkEnPassant(whiteToMove, gameBoard);

					if(enPassant != null){			// if en passant can be done
						if(dest.equals(enPassant)){	// and we're trying to do it
							start.setPiece(null);	// move pawn
							gameBoard.movePiece(p, dest);
							
							Space captureThis = gameBoard.getSpace(dest.getFile(), start.getRank()); // "capture" pawn that was victim of en passant
							captureThis.getPiece().setAlive(false);
							limbo = captureThis.getPiece();		// pieces goes in limbo
							captureThis.setPiece(null);
							
							return true;
						}
					}
				}
				
				updateGameState();
				
				for(Space s : p.moveList){
					if(dest.equals(s)){			// if our desired destination is found in our legal moves list
						start.setPiece(null);
						limbo = gameBoard.movePiece(p, dest);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * See if we can promote a pawn given the input spaces, then actually promote it if we can
	 * 
	 * Lots of borrowed code from tryMove, but it's necessary that this is its own method
	 * 
	 * @param startSpace		The starting space of the piece
	 * @param endSpace			The desired destination of the piece
	 * @param newPiece			The type of piece to promote to (must be Q, R, B, or N)
	 * @return					Returns true if the piece was successfully moved and false otherwise
	 */
	private boolean tryPromote(String startSpace, String endSpace, String newPiece){
		Space start = parseSpace(startSpace);
		// Check to see if our starting position is valid
		if(start == null)
			return false;
		
		if(!start.isOccupied())
			return false;
		
		Piece p = start.getPiece();
		if(!(p instanceof Pawn))				// can only promote pawns
			return false;
		
		if(p.getColor() == whiteToMove){		// piece we're moving needs to match the player who's going
			
			if(p.moveList.isEmpty()){			// If piece can't move anywhere, we can ignore it
				return false;
			}
			
			Space dest = parseSpace(endSpace);
			if(dest == null){
				return false;
			}
			
			if(whiteToMove == true && dest.getRank() != 7)	// white pawns must be promoted on rank 8
				return false;
			
			if(whiteToMove == false && dest.getRank() != 0)	// black pawns must be promoted on rank 1
				return false;
			
			// if we reach this point we know our piece is a pawn of the correct color that can move
			// with our attempted destination being on an end rank
			
			for(Space s : p.moveList){
				if(dest.equals(s)){			// if our desired destination is found in our legal moves list
					
					switch(newPiece){
					case "Q":
						p = new Queen(dest.getFile(), dest.getRank(), p.getColor());
						break;
						
					case "R":
						p = new Rook(dest.getFile(), dest.getRank(), p.getColor());
						break;
						
					case "B":
						p = new Bishop(dest.getFile(), dest.getRank(), p.getColor());
						break;
						
					case "N":
						p = new Knight(dest.getFile(), dest.getRank(), p.getColor());
						break;
						
					default:
						return false;
					}
					
					start.setPiece(null);
					limbo = gameBoard.movePiece(p, dest);
					
					return true;
				}
			}
		}
		return false;
	}
	
	// true on success, false otherwise
	public boolean tryUndo(String startSpace, String endSpace){
		
		if(undoUsed)
			return false;
		
		System.out.println("UNDOING MOVE: " +startSpace +" " +endSpace);
		if(justPromoted){
			undoPromotion(startSpace, endSpace);
		}else{
			undoMove(startSpace, endSpace);
		}
		
		undoUsed = true;				// only 1 called undo per turn!
		whiteToMove = !whiteToMove;		// return to previous state
		
		return true;
	}
	
	/**
	 * If after a move, the king would be in check, the move is undone and relevant fields are restored to their original value.
	 * 
	 * @param startSpace		The space that the piece was on before the move
	 * @param endSpace			The space that the piece is now illegally on
	 */
	private void undoMove(String startSpace, String endSpace){

		// System.out.println("UNDOING MOVE: " +startSpace +" " +endSpace);
		
		Space temp = parseSpace(startSpace);
		Space start = gameBoard.getSpace(temp.getFile(), temp.getRank());
		
		temp = parseSpace(endSpace);
		Space end = gameBoard.getSpace(temp.getFile(), temp.getRank());
		
		Piece p = end.getPiece();

		// restore the piece to its original position
		end.setPiece(limbo);
		gameBoard.movePiece(p, start);
		
		if(p instanceof Pawn){
			if(((Pawn)p).hasMovedLastTurn() == false){
				((Pawn)p).movePawn(false);
			}
		}else if(p instanceof Rook){
			if(((Rook)p).hasMovedLastTurn() == false)
				((Rook)p).moveRook(false);
		}
		
		updateGameState();
	}
	
	/**
	 * If after a promotion, the king would be in check, the promotion is undone and the piece is returned to being a pawn
	 * 
	 * @param startSpace		The space that the pawn was on before the move
	 * @param endSpace			The space that the pawn is now illegally on
	 */
	private void undoPromotion(String startSpace, String endSpace){

		Space temp = parseSpace(startSpace);
		Space start = gameBoard.getSpace(temp.getFile(), temp.getRank());
		
		temp = parseSpace(endSpace);
		Space end = gameBoard.getSpace(temp.getFile(), temp.getRank());
		
		Piece p = end.getPiece();
		p = new Pawn(temp.getFile(), temp.getRank(), p.getColor());
		
		// restore the piece to its original position
		end.setPiece(limbo);
		gameBoard.movePiece(p, start);
		
		updateGameState();
	}
	
	/**
	 * Routine maintenance that keeps all player info up-to-date
	 */
	private void updateGameState(){
		
		Player[] players = {player1, player2};
		
		for(Player pl : players){
			pl.updateOwnedPieces();				// update player's list of owned pieces
			pl.setPieceMoveLists(gameBoard);	// initialize the list of possible moves	
			pl.updateThreatenedSquares();		// update threatened squares list
			updateKingMoveList(pl);				// remove threatened squares from player's list of viable king moves
		}
	}
	
	/**
	 * Accepts an argument through the terminal and attempts to move a piece.
	 * @param readMove	StringReader
	 * @return		returns message that android app will print
	 */
	public String move(StringReader readMove){
		String currentLine;
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(readMove);
		String color = "";
		
		if(whiteToMove){
			color = "White";
			player1.updateHasMoved();
		}else{
			color = "Black";
			player2.updateHasMoved();
		}
		
		updateGameState();
		
		// System.out.println(color +" to move.\n");
		
		try{
			while ((currentLine = br.readLine()) != null) {
				String[] move = currentLine.split(" ");
				
				if(move.length == 2){		// traditional chess move
					if(move[0].equals("print")){
						Space s = parseSpace(move[1]);
						gameBoard.getSpace(s.getFile(), s.getRank()).getPiece().printMoveList();
					}else if(tryMove(move[0], move[1]) == true){
						updateGameState();
						if(isMyKingSafe() == false){				// check if making this move places your king in danger
							printInvalidMoveInput();
							// System.out.println("This move would endanger the " +color +" king");
							
							undoMove(move[0], move[1]);				// undo move that places king in danger (this should never directly move the king)
						}else{
							gameBoard.printBoard();
							System.out.println(color +"'s move: " +currentLine);
							justPromoted = false;
							
							whiteToMove = !whiteToMove;
							
							if(checkGameOver())		// tells us if next player is checkmated
								return "Checkmate";
							
							return "";		// valid move that doesn't end the game
						}
					}else{
						// printInvalidMoveInput();
					}
					
				}else if(move.length == 3){		// pawn promotion or offering a draw
					
					if(move[2].length() == 1){	// promoting a pawn
						if(tryPromote(move[0], move[1], move[2]) == true){		// this is the same code that's in the move.length == 2 block above
							updateGameState();
							if(isMyKingSafe() == false){						// check if promoting places your king in danger
								printInvalidMoveInput();
								//System.out.println("This move would endanger your king");
								
								undoPromotion(move[0], move[1]);						// undo move that places king in danger (this should never directly move the king)
							}else{
								gameBoard.printBoard();
								System.out.println(color +"'s move: " +currentLine);
								justPromoted = true;
								
								whiteToMove = !whiteToMove;
								
								if(checkGameOver())		// tells us if next player is checkmated
									return "Checkmate";
								
								return "";		// valid move that doesn't end the game
							}
						}
					}
				}
				printInvalidMoveInput();
			}
		}catch (IOException e) {
			printInvalidMoveInput();
			e.printStackTrace();
		}
		
		return "Invalid move";		// 0 is end of game, 1 is valid move, 2 is error
	}
	
	/**
	 * Accepts a string argument in the form of a chess square (i.e. "e2") and returns the corresponding Space.
	 * 
	 * @param str			The String form of the space to be parsed
	 * @return				The Space represented by the input String
	 */
	private Space parseSpace(String str){
		
		if(str.length() != 2)
			return null;
		
		int file = str.charAt(0) - 'a';
		int rank = (str.charAt(1) - '0' - 1);
		
		if(file < 0 || file > 7 || rank < 0 || rank > 7){
			// System.out.println("The space " +move +" is invalid.");
			printInvalidMoveInput();
			return null;
		}
		
		return gameBoard.getSpace(file, rank);
	}
	
	public String unparseSpace(Space s){
		
		if(s == null)
			return null;
		
		char letter = (char)(s.getFile()  + 'a');
		char number = (char)(s.getRank() + '0' + 1);
		
		return "" +letter +number;
	}
	
	/**
	 * Helps get rid of repetitive print statements in the code
	 */
	private void printInvalidMoveInput(){
		System.out.println("Illegal move, try again");
	}
	
	/*public void playGame(){
		String readMove;
		
		while(move() != 0){
			if(whiteToMove){
				whiteToMove = false;
			}else{
				whiteToMove = true;
			}
		}
	}*/
	
	public static void main(String[] args){
		Chess game = new Chess();
		
		game.setBoard();
		game.gameBoard.printBoard();
		
		//game.playGame();
	}
}
