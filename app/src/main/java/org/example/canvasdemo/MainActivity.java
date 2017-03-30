package org.example.canvasdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.canvasdemo.R.id.gameView;

public class MainActivity extends Activity {


	MyView gameView;

	private final int LEVEL_TIME = 20;
	private Timer pacmanMovingTimer;
	private Timer timeLeftTimer;
	private Timer enemyMovingTimer;
	private int current_level = 1;
	private int timePassed = 0;
	private boolean running = false;
	private String direction = "Right";
	private TextView pointsView;
	private TextView timeLeftView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button right = (Button) findViewById(R.id.rightButton);
		Button left = (Button) findViewById(R.id.leftButton);
		Button up = (Button) findViewById(R.id.upButton);
		Button down = (Button) findViewById(R.id.downButton);
		gameView = (MyView) findViewById(R.id.gameView);
		pointsView = (TextView) findViewById(R.id.points_num);
		timeLeftView = (TextView) findViewById(R.id.timeLeft_num);
		pointsView.setText(Integer.toString(gameView.point));
		timeLeftView.setText(Integer.toString(LEVEL_TIME) + " sec");
		//listeners of pacman
		right.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				int action = motionevent.getAction();
				if (action == MotionEvent.ACTION_DOWN && running) {
					direction = "Right";
				}
				return false;
			}
		});

		left.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				int action = motionevent.getAction();
				if (action == MotionEvent.ACTION_DOWN && running) {
					direction = "Left";
				}
				return false;
			}
		});

		up.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				int action = motionevent.getAction();
				if (action == MotionEvent.ACTION_DOWN && running) {
					direction = "Top";
				}
				return false;
			}
		});

		down.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				int action = motionevent.getAction();
				if (action == MotionEvent.ACTION_DOWN && running) {
					direction = "Bottom";
				}
				return false;
			}
		});

		pacmanMovingTimer = new Timer();
		pacmanMovingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(Pacman_Move);
			}

		}, 0, 30); //0 indicates the start of counting.
		// It call a move method every 30 milliseconds which redraws everything correctly.

		enemyMovingTimer = new Timer();
		enemyMovingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(Enemy_Move);
			}

		}, 0, 50); //0 indicates a start of counting.
		// Enemy moves every 50 milliseconds.

		timeLeftTimer = new Timer();
		timeLeftTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(Time_Countdown);
			}

		}, 0, 1000); //0 indicates the start of counting,
		// Time_Countdown calls every 1000 milliseconds(1sec.)

		final Button pause_continue = (Button) findViewById(R.id.pause_continue);
		pause_continue.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				int action = motionevent.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					if (running) {
						running = false;
						pause_continue.setText("Continue");
					} else {
						running = true;
						pause_continue.setText("Pause");
					}
					if (gameView.finished) {
						gameView.finished = false;
					}
				}
				return false;
			}
		});


	}

	private Runnable Pacman_Move = new Runnable() {
		public void run() {
			if (running && gameView.finished ) {
				running = false;
				final Button pause_start = (Button) findViewById(R.id.pause_continue);
				pause_start.setText("Restart");
				Toast toast = Toast.makeText(getApplicationContext(), "Game over", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				gameView.pacman = new Pacman(0, 0);
				gameView.enemies = new ArrayList<>();
				current_level = 1;
				timePassed = 0;
				direction = "Right";

			}
			if (running && !gameView.finished) {
				switch (direction) {
					case "Right":
						gameView.moveRight(10);
						break;
					case "Left":
						gameView.moveLeft(10);
						break;
					case "Top":
						gameView.moveUp(10);
						break;
					case "Bottom":
						gameView.moveDown(10);
						break;
					default:
						break;
				}
				pointsView.setText(Integer.toString(gameView.point));
			}

		}
	};

	private Runnable Time_Countdown = new Runnable() {
		public void run() {
			if (running && !gameView.finished) {
				if (timePassed < LEVEL_TIME) {
					timePassed++;
					timeLeftView.setText(Integer.toString(LEVEL_TIME - timePassed) + " sec");
				} else {
					timePassed = 0;
					current_level++;
					running = false;
					Enemy enemy = new Enemy(gameView.w / 2, gameView.h / 2);
					gameView.enemies.add(enemy);
					new CountDownTimer(5000, 1000) {
						int tick = 4;
						Toast toast;

						public void onTick(long millisUntilFinished) {
							if (tick == 4) {
								toast = Toast.makeText(getApplicationContext(), "Level " + Integer.toString(current_level - 1) + " Finished", Toast.LENGTH_SHORT);
							} else {
								toast.cancel();
								toast = Toast.makeText(getApplicationContext(), "Level " + Integer.toString(current_level) + ". Start in " + Integer.toString(tick) + " sec", Toast.LENGTH_SHORT);
							}
							tick--;
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}

						public void onFinish() {
							running = true;
						}
					}.start();
				}
			}
		}
	};


	private Runnable Enemy_Move = new Runnable() {
		public void run() {
			if (running) {
				if (gameView.enemies.size() < 3) {
					for (int i = 0; i < 3; i++) {
						Enemy enemy = new Enemy(gameView.w / 2, gameView.h / 2);
						gameView.enemies.add(enemy);
					}
				}
				gameView.moveEnemies(10, 10);

			}

		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		//stopping the timer when the app is killed.
		pacmanMovingTimer.cancel();
		timeLeftTimer.cancel();
		enemyMovingTimer.cancel();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
