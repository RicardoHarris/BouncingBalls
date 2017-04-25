package com.ricardojh.bouncingballs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class BouncingBalls extends ApplicationAdapter {
	//BallObject myBall;
	int screenWidth, screenHeight;
	ShapeRenderer myRenderer;
	float ballRad = 7f;
	private BallObject[] balls;
	int numBalls = 1000;
	float initSpeed = 1f;
	
	@Override
	public void create () {
	//	myBall = new BallObject();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		myRenderer = new ShapeRenderer();
		balls = new BallObject[numBalls];
		int ballRows =(int) Math.ceil(  Math.sqrt(numBalls)  );

		for (int i = 0 ; i < numBalls ; i++){
			balls[i] = new BallObject();
			float initAngle = (float) (Math.random()*2*3.14159);
			balls[i].vx = (float) ( initSpeed * Math.cos(initAngle));
			balls[i].vy = (float) ( initSpeed * Math.cos(initAngle));
			//etc for the other parameters
// we're changing references of myBall to balls[i] so we can refer to each ball in the array.
			balls[i].x = (0.5f+i/ballRows) * screenWidth/(ballRows);
			balls[i].y = (0.5f+i% ballRows) * screenHeight/(ballRows);
			balls[i].redF = (float) Math.random();
			balls[i].greenF = (float) Math.random();
			balls[i].blueF = (float) Math.random();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		myRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for(BallObject myBall: balls){
			myRenderer.setColor(myBall.redF, myBall.greenF, myBall.blueF, 1);
			myRenderer.circle(myBall.x, myBall.y, ballRad);
		}
		myRenderer.end();

		mover();
	}
	private void mover(){
		for(int i=0; i < numBalls;i++){
			balls[i].x += balls[i].vx;
			balls[i].y += balls[i].vy;
			if(balls[i].x < ballRad)
				balls[i].vx = Math.abs(balls[i].vx);
			if(balls[i].y < ballRad)
				balls[i].vy = Math.abs(balls[i].vy);
			if(balls[i].x > screenWidth-ballRad)
				balls[i].vx = -Math.abs(balls[i].vx);
			if(balls[i].y > screenHeight-ballRad)
				balls[i].vy = -Math.abs(balls[i].vy);
			for(int j = i+1; j < numBalls; j++){
				float dx = balls[i].x - balls[j].x;
				float dy = balls[i].y - balls[j].y;
				float distance = (float) Math.sqrt(dx*dx+dy*dy);
				if (distance < 2 * ballRad) {
					Vector2 dV = colliderator(i,j);
					balls[i].vx -= dV.x;
					balls[i].vy -= dV.y;
					balls[j].vx += dV.x;
					balls[j].vy += dV.y;
					Vector2 newP = setBall2Rad(i, j );
					balls[i].x = newP.x;
					balls[i].y = newP.y;
				}
			}
		}
	}
	private Vector2 colliderator(int i, int j){
		Vector2 V1 = new Vector2(balls[i].vx, balls[i].vy);
		Vector2 V2 = new Vector2(balls[j].vx, balls[j].vy);
		Vector2  P1 = new Vector2(balls[i].x, balls[i].y);
		Vector2 P2 = new Vector2(balls[j].x, balls[j].y);

		Vector2 Vdiff = new Vector2(V1.x-V2.x, V1.y-V2.y);// V1 - V2 Vec
		Vector2 Pdiff = new Vector2(P1.x-P2.x, P1.y-P2.y); // P1-P2 Vec
		float dotter = Vdiff.dot(Pdiff);
		float Pdist2 = Pdiff.len2();

		return Pdiff.scl(dotter/(Pdist2));
	}
	private Vector2 setBall2Rad(int i,int j){
		Vector2 ball1 = new Vector2(balls[i].x, balls[i].y);
		Vector2 ball2 = new Vector2(balls[j].x, balls[j].y);

		Vector2 ballDiff = new Vector2(ball1.x-ball2.x, ball1.y-ball2.y);
		ballDiff.setLength(2*ballRad+1);

		return ballDiff.add(ball2);
	}
}
