package org.example.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.canvasdemo.R.drawable.pacman;

public class MyView extends View{
	
	Bitmap tempPacman = BitmapFactory.decodeResource(getResources(),R.drawable. pacman);
	Bitmap bitmapPacman = Bitmap.createScaledBitmap(tempPacman, 80, 80, false);
	//The coordinates for our dear pacman: (0,0) is the top-left corner
	Pacman pacman = new Pacman(0, 0);

	Bitmap tempEnemy = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
	Bitmap bitmapEnemy = Bitmap.createScaledBitmap(tempEnemy, 80, 80, false);
	List<Enemy> enemies = new ArrayList<Enemy>();
	Paint paint = new Paint();
	int h,w; //used for storing our height and width
	int highScore = 0;
	boolean finished = false;

	int point = 0;
	//generate first coin
	Coin coin = new Coin(500, 500);

	public void moveEnemies(int x, int y) {
		Random rand = new Random();
		for (Enemy enemy : enemies) {
			if (enemy.move_duration == 0) {
				enemy.move_direction = rand.nextInt(4);
				enemy.move_duration = rand.nextInt(15) + 1;
			}
			switch (enemy.move_direction) {
				case 0:
					if (enemy.enemyX + x + bitmapEnemy.getWidth() <= w)
						enemy.moveRight(x);
					else {
						enemy.move_duration = 1;
						enemy.moveLeft(x);
					}
					invalidate(); //redraw everything - this ensures onDraw() is called.
					break;
				case 1:
					if (enemy.enemyX - x >= 0)
						enemy.moveLeft(x);
					else {
						enemy.move_duration = 1;
						enemy.moveRight(x);
					}
					invalidate(); //redraw everything - this ensures onDraw() is called.
					break;
				case 2:
					if (enemy.enemyY - y >= 0)
						enemy.moveUp(y);
					else {
						enemy.move_duration = 1;
						enemy.moveDown(x);
					}
					invalidate(); //redraw everything - this ensures onDraw() is called.
					break;
				case 3:
					if (enemy.enemyY + y + bitmapEnemy.getHeight() <= h)
						enemy.moveDown(y);
					else {
						enemy.move_duration = 1;
						enemy.moveUp(x);
					}
					invalidate(); //redraw everything - this ensures onDraw() is called.
					break;
				default:
					break;
			}
			enemy.move_duration--;
		}
	}

    public void moveRight(int x)
    {
    	//still within our boundaries?
    	if (pacman.pacx+x+bitmapPacman.getWidth()<=w)
    		pacman.moveRight(x);
    	invalidate(); //redraw everything - this ensures onDraw() is called.
    }

	public void moveLeft(int x)
	{
		//still within our boundaries?
		if (pacman.pacx - x >= 0)
			pacman.moveLeft(x);
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}

	public void moveUp(int y) {
		//still within our boundaries?
		if (pacman.pacy - y >= 0)
			pacman.moveUp(y);
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}

	public void moveDown(int y) {
		//still within our boundaries?
		if (pacman.pacy + y + bitmapPacman.getHeight() <= h)
			pacman.moveDown(y);
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public MyView(Context context) {
		super(context);
		
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		//Here we get the height and weight
		h = canvas.getHeight();
		w = canvas.getWidth();
		for (Enemy enemy : enemies) {
//               check if enemy in range of pacman
			boolean expression1 = ((75 > pacman.pacx - enemy.enemyX && pacman.pacx - enemy.enemyX > -75) && (75 > pacman.pacy - enemy.enemyY && pacman.pacy - enemy.enemyY > -75));
			if (expression1) {
				finished = true;

			}
		}
        //calculate the distance between a coin and the pacman
		if (Math.sqrt(((pacman.pacx + 40 - coin.x) * (pacman.pacx + 40 - coin.x)) + ((pacman.pacy + 40 - coin.y) * (pacman.pacy + 40 - coin.y))) < 40) {
			point += 1;
			coin = new Coin(w, h);
		}

		canvas.drawColor(Color.WHITE);

		paint.setColor(Color.RED);

		//drawing a circle based on the coin instance
		canvas.drawCircle(coin.x, coin.y, Coin.radius, paint);

		canvas.drawBitmap(bitmapPacman, pacman.pacx, pacman.pacy, paint);
		for (Enemy enemy : enemies) {

			canvas.drawBitmap(bitmapEnemy, enemy.enemyX, enemy.enemyY, paint);
		}
		super.onDraw(canvas);
	}



}
