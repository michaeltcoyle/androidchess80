package com.example.androidchess80;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import chess.Chess;
import chess.Piece;
import chess.Space;

public class GameBoard extends Activity {

	
	static ImageButton[][] buttons = new ImageButton[8][8];
	
	ImageButton a8, a7, a6, a5, a4, a3, a2, a1;
	ImageButton b8, b7, b6, b5, b4, b3, b2, b1;
	ImageButton c8, c7, c6, c5, c4, c3, c2, c1;
	ImageButton d8, d7, d6, d5, d4, d3, d2, d1;
	ImageButton e8, e7, e6, e5, e4, e3, e2, e1;
	ImageButton f8, f7, f6, f5, f4, f3, f2, f1;
	ImageButton g8, g7, g6, g5, g4, g3, g2, g1;
	ImageButton h8, h7, h6, h5, h4, h3, h2, h1;
	
	

	
	
	Space first = null;
	Space second = null;
	
	String lastmoveStart = "";
	String lastmoveDest = "";
	
	Chess game = new Chess();
	
	Button move;
	Button undo;
	Button ai;
	Button resign;
	Button draw;
	
	static TextView from;
	static TextView to;
	static TextView whiteToMove;
	static TextView info;			// info text box
	
	// for recording games
	private String recordname;
	private File path;
	private FileOutputStream writer;
	private String gameTitle;


	public static void set_image(ImageButton image, Piece piece) {
		//System.out.println(piece.toString());
		if (piece.toString().equals("wR")) {
		image.setImageResource(R.drawable.wr);
		} else if (piece.toString().equals("wN")) {
		image.setImageResource(R.drawable.wn);
		} else if (piece.toString().equals("wB")) {
		image.setImageResource(R.drawable.wb);
		} else if (piece.toString().equals("wQ")) {
		image.setImageResource(R.drawable.wq);
		} else if (piece.toString().equals("wK")) {
		image.setImageResource(R.drawable.wk);
		} else if (piece.toString().equals("wP")) {
		image.setImageResource(R.drawable.white_pawn);
		} else if (piece.toString().equals("bR")) {
		image.setImageResource(R.drawable.br);
		} else if (piece.toString().equals("bN")) {
		image.setImageResource(R.drawable.bn);
		} else if (piece.toString().equals("bB")) {
		image.setImageResource(R.drawable.bb);
		} else if (piece.toString().equals("bQ")) {
		image.setImageResource(R.drawable.bq);
		} else if (piece.toString().equals("bK")) {
		image.setImageResource(R.drawable.bk);
		} else if (piece.toString().equals("bP")) {
		image.setImageResource(R.drawable.black_pawn);
		}
	}
	
	public static String getMove(){
		String tmp1 = (String) from.getText();
		tmp1 = tmp1.substring(12);
		String tmp2 = (String) to.getText();
		tmp2 = tmp2.substring(10);
		String tmp3 = " ";
		// System.out.println(tmp1+tmp2);
		return tmp1+tmp3+tmp2;
	}
	
	public void playGame(Chess game) throws IOException{
		String readMove = getMove();
		String infoMessage = game.move(new StringReader(readMove));
		
		if (infoMessage.isEmpty()){		// valid move, no info message needed
			System.out.println("RM: " +readMove);
			if(game.whiteToMove){
				whiteToMove.setText("White to move");
			}else{
				whiteToMove.setText("Black to move");
			}
			
			if(game.inCheck){
				info.setText("Check!");
			}else{
				info.setText("");
			}
			
			lastmoveStart = ((String)from.getText()).substring(12);
			lastmoveDest = ((String)to.getText()).substring(10);
			
			//recordMove(readMove);
			
		}else{
			info.setText(infoMessage);
			if(infoMessage.equals("Checkmate")){
				// END THE GAME
				//recordMove(readMove);
				if(game.whiteToMove){
					whiteToMove.setText("Black wins!");
				}else{
					whiteToMove.setText("White wins!");
				}
			}
		}
	}
	
	public static void updateGraphics(){
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				
				//System.out.println(i+" " +j);
				if (chess.Board.board[i][j].piece != null){
					set_image(buttons[i][j], chess.Board.board[i][j].piece);
				}
				else{
					if (i == 0 || i == 2 || i == 4 || i == 6){
						if (j == 0 || j == 2 || j == 4 || j == 6){
							buttons[i][j].setImageResource(R.drawable.black);
						}
						else if (j == 1 || j == 3 || j == 5 || j == 7){
							buttons[i][j].setImageResource(R.drawable.white);
						}
					}
					else if (i == 1 || i == 3 || i == 5 || i == 7){
						if (j == 0 || j == 2 || j == 4 || j == 6){
							buttons[i][j].setImageResource(R.drawable.white);
						}
						else if (j == 1 || j == 3 || j == 5 || j == 7){
							buttons[i][j].setImageResource(R.drawable.black);
						}
					}
				}
			}
		}
	}
	
	public void print(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_board);
		from = (TextView)findViewById(R.id.textView1);
		whiteToMove = (TextView)findViewById(R.id.textView2);
		to = (TextView)findViewById(R.id.textView3);
		info = (TextView)findViewById(R.id.textView4);
		
		from.setText("From Space: ");
		to.setText("To Space: ");
		whiteToMove.setText("White to move");
		info.setText("");
		
		
		move =		(Button)findViewById(R.id.button1);
		undo =		(Button)findViewById(R.id.button2);
		ai =		(Button)findViewById(R.id.button3);
		resign = 	(Button)findViewById(R.id.button4);
		draw =      (Button)findViewById(R.id.button5);
		
		
		a8 = (ImageButton)findViewById(R.id.a8);
		a7 = (ImageButton)findViewById(R.id.a7);
		a6 = (ImageButton)findViewById(R.id.a6);
		a5 = (ImageButton)findViewById(R.id.a5);
		a4 = (ImageButton)findViewById(R.id.a4);
		a3 = (ImageButton)findViewById(R.id.a3);
		a2 = (ImageButton)findViewById(R.id.a2);
		a1 = (ImageButton)findViewById(R.id.a1);
		
		b8 = (ImageButton)findViewById(R.id.b8);
		b7 = (ImageButton)findViewById(R.id.b7);
		b6 = (ImageButton)findViewById(R.id.b6);
		b5 = (ImageButton)findViewById(R.id.b5);
		b4 = (ImageButton)findViewById(R.id.b4);
		b3 = (ImageButton)findViewById(R.id.b3);
		b2 = (ImageButton)findViewById(R.id.b2);
		b1 = (ImageButton)findViewById(R.id.b1);
		
		c8 = (ImageButton)findViewById(R.id.c8);
		c7 = (ImageButton)findViewById(R.id.c7);
		c6 = (ImageButton)findViewById(R.id.c6);
		c5 = (ImageButton)findViewById(R.id.c5);
		c4 = (ImageButton)findViewById(R.id.c4);
		c3 = (ImageButton)findViewById(R.id.c3);
		c2 = (ImageButton)findViewById(R.id.c2);
		c1 = (ImageButton)findViewById(R.id.c1);
		
		d8 = (ImageButton)findViewById(R.id.d8);
		d7 = (ImageButton)findViewById(R.id.d7);
		d6 = (ImageButton)findViewById(R.id.d6);
		d5 = (ImageButton)findViewById(R.id.d5);
		d4 = (ImageButton)findViewById(R.id.d4);
		d3 = (ImageButton)findViewById(R.id.d3);
		d2 = (ImageButton)findViewById(R.id.d2);
		d1 = (ImageButton)findViewById(R.id.d1);
		
		e8 = (ImageButton)findViewById(R.id.e8);
		e7 = (ImageButton)findViewById(R.id.e7);
		e6 = (ImageButton)findViewById(R.id.e6);
		e5 = (ImageButton)findViewById(R.id.e5);
		e4 = (ImageButton)findViewById(R.id.e4);
		e3 = (ImageButton)findViewById(R.id.e3);
		e2 = (ImageButton)findViewById(R.id.e2);
		e1 = (ImageButton)findViewById(R.id.e1);
		
		f8 = (ImageButton)findViewById(R.id.f8);
		f7 = (ImageButton)findViewById(R.id.f7);
		f6 = (ImageButton)findViewById(R.id.f6);
		f5 = (ImageButton)findViewById(R.id.f5);
		f4 = (ImageButton)findViewById(R.id.f4);
		f3 = (ImageButton)findViewById(R.id.f3);
		f2 = (ImageButton)findViewById(R.id.f2);
		f1 = (ImageButton)findViewById(R.id.f1);
		
		g8 = (ImageButton)findViewById(R.id.g8);
		g7 = (ImageButton)findViewById(R.id.g7);
		g6 = (ImageButton)findViewById(R.id.g6);
		g5 = (ImageButton)findViewById(R.id.g5);
		g4 = (ImageButton)findViewById(R.id.g4);
		g3 = (ImageButton)findViewById(R.id.g3);
		g2 = (ImageButton)findViewById(R.id.g2);
		g1 = (ImageButton)findViewById(R.id.g1);
		
		h8 = (ImageButton)findViewById(R.id.h8);
		h7 = (ImageButton)findViewById(R.id.h7);
		h6 = (ImageButton)findViewById(R.id.h6);
		h5 = (ImageButton)findViewById(R.id.h5);
		h4 = (ImageButton)findViewById(R.id.h4);
		h3 = (ImageButton)findViewById(R.id.h3);
		h2 = (ImageButton)findViewById(R.id.h2);
		h1 = (ImageButton)findViewById(R.id.h1);
		
		buttons[0][0] = a1;
		buttons[0][1] = a2;
		buttons[0][2] = a3;
		buttons[0][3] = a4;
		buttons[0][4] = a5;
		buttons[0][5] = a6;
		buttons[0][6] = a7;
		buttons[0][7] = a8;
		
		buttons[1][0] = b1;
		buttons[1][1] = b2;
		buttons[1][2] = b3;
		buttons[1][3] = b4;
		buttons[1][4] = b5;
		buttons[1][5] = b6;
		buttons[1][6] = b7;
		buttons[1][7] = b8;

		buttons[2][0] = c1;
		buttons[2][1] = c2;
		buttons[2][2] = c3;
		buttons[2][3] = c4;
		buttons[2][4] = c5;
		buttons[2][5] = c6;
		buttons[2][6] = c7;
		buttons[2][7] = c8;
		
		buttons[3][0] = d1;
		buttons[3][1] = d2;
		buttons[3][2] = d3;
		buttons[3][3] = d4;
		buttons[3][4] = d5;
		buttons[3][5] = d6;
		buttons[3][6] = d7;
		buttons[3][7] = d8;

		buttons[4][0] = e1;
		buttons[4][1] = e2;
		buttons[4][2] = e3;
		buttons[4][3] = e4;
		buttons[4][4] = e5;
		buttons[4][5] = e6;
		buttons[4][6] = e7;
		buttons[4][7] = e8;
		
		buttons[5][0] = f1;
		buttons[5][1] = f2;
		buttons[5][2] = f3;
		buttons[5][3] = f4;
		buttons[5][4] = f5;
		buttons[5][5] = f6;
		buttons[5][6] = f7;
		buttons[5][7] = f8;
		
		buttons[6][0] = g1;
		buttons[6][1] = g2;
		buttons[6][2] = g3;
		buttons[6][3] = g4;
		buttons[6][4] = g5;
		buttons[6][5] = g6;
		buttons[6][6] = g7;
		buttons[6][7] = g8;

		buttons[7][0] = h1;
		buttons[7][1] = h2;
		buttons[7][2] = h3;
		buttons[7][3] = h4;
		buttons[7][4] = h5;
		buttons[7][5] = h6;
		buttons[7][6] = h7;
		buttons[7][7] = h8;
		
		View.OnClickListener spaceListener = new View.OnClickListener() {
		    public void onClick(View view)  {
		    	
		    	if (first == null){
			    	int file;
			    	int rank;

			    	file = (int)((String) view.getTag()).charAt(0) - 96;
			    	rank = (int)((String) view.getTag()).charAt(1) - 48;

			    	from.setText("From Space: "+(String)view.getTag());

			    	
			    	
		            first = new Space(file,rank);
		    	}
		    	else if (second == null){
		    		int file;
			    	int rank;

			    	file = (int)((String) view.getTag()).charAt(0) - 96;
			    	rank = (int)((String) view.getTag()).charAt(1) - 48;

			    	to.setText("To Space: "+(String)view.getTag());
			    	
			    	
		            second = new Space(file,rank);
		            

		            

		    	}
		    	else {
		    		first = null;
		    		second = null;
		    		from.setText("From Space: ");
		    		to.setText("To Space: ");
		    	}
	        }   
		};
		
		a8.setOnClickListener(spaceListener);
		a7.setOnClickListener(spaceListener);
		a6.setOnClickListener(spaceListener);
		a5.setOnClickListener(spaceListener);
		a4.setOnClickListener(spaceListener);
		a3.setOnClickListener(spaceListener);
		a2.setOnClickListener(spaceListener);
		a1.setOnClickListener(spaceListener);
		
		b8.setOnClickListener(spaceListener);
		b7.setOnClickListener(spaceListener);
		b6.setOnClickListener(spaceListener);
		b5.setOnClickListener(spaceListener);
		b4.setOnClickListener(spaceListener);
		b3.setOnClickListener(spaceListener);
		b2.setOnClickListener(spaceListener);
		b1.setOnClickListener(spaceListener);
		
		c8.setOnClickListener(spaceListener);
		c7.setOnClickListener(spaceListener);
		c6.setOnClickListener(spaceListener);
		c5.setOnClickListener(spaceListener);
		c4.setOnClickListener(spaceListener);
		c3.setOnClickListener(spaceListener);
		c2.setOnClickListener(spaceListener);
		c1.setOnClickListener(spaceListener);
		
		d8.setOnClickListener(spaceListener);
		d7.setOnClickListener(spaceListener);
		d6.setOnClickListener(spaceListener);
		d5.setOnClickListener(spaceListener);
		d4.setOnClickListener(spaceListener);
		d3.setOnClickListener(spaceListener);
		d2.setOnClickListener(spaceListener);
		d1.setOnClickListener(spaceListener);
		
		e8.setOnClickListener(spaceListener);
		e7.setOnClickListener(spaceListener);
		e6.setOnClickListener(spaceListener);
		e5.setOnClickListener(spaceListener);
		e4.setOnClickListener(spaceListener);
		e3.setOnClickListener(spaceListener);
		e2.setOnClickListener(spaceListener);
		e1.setOnClickListener(spaceListener);
		
		f8.setOnClickListener(spaceListener);
		f7.setOnClickListener(spaceListener);
		f6.setOnClickListener(spaceListener);
		f5.setOnClickListener(spaceListener);
		f4.setOnClickListener(spaceListener);
		f3.setOnClickListener(spaceListener);
		f2.setOnClickListener(spaceListener);
		f1.setOnClickListener(spaceListener);
		
		g8.setOnClickListener(spaceListener);
		g7.setOnClickListener(spaceListener);
		g6.setOnClickListener(spaceListener);
		g5.setOnClickListener(spaceListener);
		g4.setOnClickListener(spaceListener);
		g3.setOnClickListener(spaceListener);
		g2.setOnClickListener(spaceListener);
		g1.setOnClickListener(spaceListener);
		
		h8.setOnClickListener(spaceListener);
		h7.setOnClickListener(spaceListener);
		h6.setOnClickListener(spaceListener);
		h5.setOnClickListener(spaceListener);
		h4.setOnClickListener(spaceListener);
		h3.setOnClickListener(spaceListener);
		h2.setOnClickListener(spaceListener);
		h1.setOnClickListener(spaceListener);
		

		
		move.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				if (first != null && second != null){
					try{
						playGame(game);
			            from.setText("From Space: ");
			    		to.setText("To Space: ");
			    		updateGraphics();
					}catch(IOException e){
						
					}
				}
	        }

			
		});
		
		
		undo.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				if(lastmoveStart.isEmpty() == false && lastmoveDest.isEmpty() == false){
					if(game.tryUndo(lastmoveStart, lastmoveDest)){
						first = null;
			            second = null;
			            from.setText("From Space: ");
			    		to.setText("To Space: ");
			    		if(game.whiteToMove){
							whiteToMove.setText("White to move");
						}else{
							whiteToMove.setText("Black to move");
						}
				    	updateGraphics();
					}else{
						info.setText("Only one undo per turn.");
					}
				}else{
					info.setText("Nothing to undo.");
				}
	        }

			
		});
		
		
		ai.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				String infoMessage = game.AImove();
				
				if (infoMessage.isEmpty()){		// valid move, no info message needed
					if(game.whiteToMove){
						whiteToMove.setText("White to move");
					}else{
						whiteToMove.setText("Black to move");
					}
					
					if(game.inCheck){
						info.setText("Check!");
					}else{
						info.setText("");
					}
					
					lastmoveStart = ((String)from.getText()).substring(12);
					lastmoveDest = ((String)to.getText()).substring(10);
					
				}else{
					info.setText(infoMessage);
					if(infoMessage.equals("Checkmate")){
						// END THE GAME
						if(game.whiteToMove){
							whiteToMove.setText("Black wins!");
						}else{
							whiteToMove.setText("White wins!");
						}
					}
				}
				
	            first = null;
	            second = null;
	            from.setText("From Space: ");
	    		to.setText("To Space: ");
	    		updateGraphics();
	        }

			
		});
		

		
		resign.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {				
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				builder.setTitle("Are you sure?");


				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				    	if(game.whiteToMove){
							whiteToMove.setText("Black wins!");
						}else{
							whiteToMove.setText("White wins!");
						}
				    	dialog.dismiss();
						//endGame();
				    }
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.dismiss();
				    }
				});

				builder.show();
	        }
		});
		
		draw.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				builder.setTitle("Are you sure?");


				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				    	dialog.dismiss();
						//endGame();
				    }
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.dismiss();
				    }
				});

				builder.show();
	        }

			
		});
			
		
		// initializeFileWriter(this.getApplicationContext());
		path = this.getApplicationContext().getFilesDir();
		game.setBoard();
		updateGraphics();
		
	}
	
	/*
	private void initializeFileWriter(Context context){
		
		try{
			String name = "counter";
			FileOutputStream outputStream;
			
			int ch;
			StringBuffer fileContent = new StringBuffer("");
			FileInputStream fis;
			try {
			    fis = context.openFileInput(name);
			    try {
			    	fis.skip(4);
			        while( (ch = fis.read()) != -1){
			            fileContent.append((char)ch);
			        }
			        int i = Integer.parseInt(fileContent.toString()) + 1;
			        recordname = "game" +i;
			       
					outputStream = openFileOutput(name, Context.MODE_PRIVATE);
					outputStream.write(recordname.getBytes());
					outputStream.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    fis.close();
			}catch(FileNotFoundException e){
				outputStream = openFileOutput(name, Context.MODE_PRIVATE);
				outputStream.write("1".getBytes());
				outputStream.close();
				
				recordname = "game1";
			}
			
			outputStream = openFileOutput(recordname, Context.MODE_PRIVATE);
			System.out.println("success");
		}catch(IOException e){
			 System.out.println("failure");
		}
	}
	
	private void recordMove(String s) throws IOException{
		String ret = s + "\n";
		writer.write(ret.getBytes());
	}
	
	private void endGame(){
		if(!isFinishing()){
			AlertDialog.Builder builder = new AlertDialog.Builder(GameBoard.this);
			builder.setTitle("Do you want to save this game?");

			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	dialog.dismiss();
			    }
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.dismiss();
			        finish();
			    }
			});

			builder.show();
			
			System.out.println("Please enter a title for this game.");
			
			gameTitle = "Title: Untitled game";
			//writer.write(gameTitle.getBytes());
			
			/*int ch;
			StringBuffer fileContent = new StringBuffer("");
			FileInputStream fis;
			try {
			    fis = context.openFileInput("counter");
			    try {
			        while( (ch = fis.read()) != -1){
			            fileContent.append((char)ch);
			        }
			        int i = Integer.parseInt(fileContent.toString());
			       
					outputStream = openFileOutput(name, Context.MODE_PRIVATE);
					outputStream.write(recordname.getBytes());
					outputStream.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    fis.close();
			}catch(FileNotFoundException e){
				outputStream = openFileOutput(name, Context.MODE_PRIVATE);
				outputStream.write("1".getBytes());
				outputStream.close();
				
				recordname = "game1.txt";
			}
			
			if(f.exists()) {
				Scanner in = new Scanner(new FileReader(f));
				int i = Integer.parseInt(in.next());
				in.close();
				
				FileWriter out = new FileWriter(f);
	            out.write(String.valueOf(i-1));
	            out.close();
			}
			
			//writer.close();
			finish();
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
	    for (final File fileEntry : path.listFiles()){
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
	*/
	
}



