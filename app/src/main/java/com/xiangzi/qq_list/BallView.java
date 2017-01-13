package com.xiangzi.qq_list;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;


public class BallView extends ListView{

	public static int GROUND_LING;
	public static final int UP_ZERO = 30;
	public static final int DOWN_ZERO = 60;
	Bitmap bitmap ;
	Movable m;
	Canvas canvas;
	public BallView(Context Activity) {
		super(Activity);
	}

	public BallView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void initMovables(){
		initBitmaps(getResources());
//		Random r = new Random();
//		int index = r.nextInt(32);
//		Bitmap tempBitmap = null;
//		tempBitmap = bitmap;
//		int startX = 0;
//		int startY = 300;
//		int finalX = 200;
//
//		float startVy = -(float)(1000);
//		double t1 = -startVy/BallThread.g;
//		GROUND_LING = 800;
//		double s = (startVy*t1+0.5*BallThread.g*t1*t1);
//		double t2 =Math.sqrt(((2*(GROUND_LING-startY+s))/BallThread.g));
//		double t = t1+t2;
//		float startVx = (float)((finalX-startX)/(t));
//
//		m = new Movable(startX, startY, startVx,startVy,tempBitmap.getWidth()/2, tempBitmap ,canvas);


		canvas.drawBitmap(bitmap, 200, 200, null);
		canvas.restore();
		invalidate();
	}

	public void initBitmaps(Resources r){
		bitmap = BitmapFactory.decodeResource(r, R.drawable.huaji);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
	}
}
