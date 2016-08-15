package com.example.androidchess80;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ChessMain extends Activity {
	
	Button newGame;
	Button savedGames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chess_main);
		
		newGame = (Button)findViewById(R.id.button1);
		savedGames = (Button)findViewById(R.id.button2);
		
		newGame.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(view.getContext(), GameBoard.class);
	            startActivityForResult(myIntent, 0);
	        }

			
		});
		
		savedGames.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(view.getContext(), SavedGames.class);
	            startActivityForResult(myIntent, 0);
	        }

			
		});
	}
}
