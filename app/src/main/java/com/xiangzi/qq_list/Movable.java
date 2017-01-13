package com.xiangzi.qq_list;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Movable {
	int startX = 0;
	int startY = 0;
	int currentX;
	int currentY;
	float startVX = 0f;
	float startVY = 0f;
	float currentVX = 0f;
	float currentVY = 0f;
	int r;
	double timeX;
	double timeY;
	Bitmap bitmap = null;
	BallThread bt = null;
	boolean bFall = false;
	float impactFactory = 0.5f;
	float impactFactoryX = 0.1f;
	int finalX = 500;

	public Movable(int x, int y, float startVX,float startVY,int r, Bitmap bitmap , Canvas canvas){
		this.startX = x;
		this.startY = y;
		this.currentX = x;
		this.currentY = y;
		this.r = r;
		this.startVX = startVX;
		this.startVY = startVY;
		this.bitmap = bitmap;
		timeX = System.nanoTime();
		this.currentVX = startVX;
		this.currentVY = startVY;
		bt = new BallThread(this , canvas);
		bt.start();
	}

	public void drawSelf(Canvas canvas){
		canvas.drawBitmap(bitmap, currentX, currentY, null);
	}
}
