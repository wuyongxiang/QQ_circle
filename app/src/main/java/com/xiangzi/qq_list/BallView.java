package com.xiangzi.qq_list;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;


public class BallView extends View {
	int GROUND_LING = 0 ; //表示球在第几个坐标

	public static final int UP_ZERO = 30; //向上最小速度，小于UP_ZERO表示升到最高处
	public static final int DOWN_ZERO = 60; //小于DOWN_ZERO表示小球已落地
	public static float g = 4000; //重力加速度

	int startX = 0; //表情运动前的坐标
	int startY = 0;

	int currentX= -100;//表情实时运动坐标
	int currentY= -100;

	float startVX = 0f;//表情初始速度
	float startVY = -(float)(1200);

	float currentVX = 0f;//表情实时速度
	float currentVY = 0f;

	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.huaji);
	int r = bitmap.getHeight()/2;

	float impactFactoryY = 0.5f;//衰减系数
	float impactFactoryX = 0.1f;

	double timeX;
	double timeY;



	List<Point> pointList;
	Canvas canvas;

	Bitmap wood = BitmapFactory.decodeResource(getResources(), R.drawable.wood);
	BallThread bt = null;
	boolean bFall = false;



	public BallView(Context Activity) {
		super(Activity);

	}

	public BallView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void initMovables(List<Point> pointList){
		this.pointList =pointList;

		GROUND_LING = 0;
		startX = pointList.get(GROUND_LING).x;
		startY = pointList.get(GROUND_LING).y;
		bt = new BallThread();
		bt.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		canvas.drawBitmap(bitmap, currentX, currentY, null);
	}
	public class BallThread extends Thread {
		boolean flag = false; //一次小球运动标志
		double current;//实时时间
		public BallThread(){
			timeX = System.nanoTime();
			this.flag = true;
		}

		public void run(){
			while(GROUND_LING<pointList.size()-1){
				flag = true;
				startVY = -(float)(1200);
				double t1 = -startVY/g; //V=V0+g*t  V=0 ,则t=-V0/g
				double s = (startVY*t1+0.5*g*t1*t1);
				double t2 =Math.sqrt(((2*(pointList.get(GROUND_LING+1).y-startY+(Math.abs(s))))/g));
				double t = t1+t2;
				startVX = (float)((pointList.get(GROUND_LING+1).x-startX)/(t));
				currentVX = startVX;
				currentX = startX;
				currentY = startY;
				while(flag){
					postInvalidate();
					current = System.nanoTime();
					double timeSpanX = (double)((current -  timeX)/1000/1000/1000);
					currentX = (int)(startX +  currentVX * timeSpanX);
					if( bFall){
						double timeSpanY = (double)((current -  timeY)/1000/1000/1000);
						currentY = (int)( startY +  startVY * timeSpanY + timeSpanY * timeSpanY * g/2);
						currentVY = (float)( startVY + g * timeSpanY);
						if( startVY < 0 && Math.abs( currentVY) <= BallView.UP_ZERO){
							timeY = System.nanoTime();
							currentVY = 0;
							startVY = 0;
							startY =  currentY;
						}
						if( currentY +  r*2 >= pointList.get(GROUND_LING+1).y &&  currentVY > 0){
							//到达目标坐标点，开始弹跳
							currentVX =  currentVX * ( impactFactoryX);//速度衰减
							currentVY = 0 -  currentVY * ( impactFactoryY);

							if(Math.abs( currentVY) < BallView.DOWN_ZERO){
								//当速度衰减到最小速度以后，小球落地，运动结束
								try{
									Thread.sleep(100);
								}catch(Exception e){
									e.printStackTrace();
								}
								this.flag = false;
								System.out.println("线程结束"+GROUND_LING);
								GROUND_LING++;
							}else{
								startX = (int) currentX;
								timeX = System.nanoTime();
								startY =  currentY;
								timeY = System.nanoTime();
								startVY =  currentVY;
							}
						}
					}else {
						if( currentVY == 0 ||  currentVY > 0){
							timeY = System.nanoTime();
							bFall = true;//bFall参数用来判断是否进行S2的自由落体运动
						}else{
							currentVY = (int)( startVY + g * timeSpanX);
							currentY = (int)( currentY +  startVY * timeSpanX + timeSpanX * timeSpanX * g/2);
						}


					}
				}
			}

		}
	}
//	Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			int now = msg.what;
//			if(now<pointList.size()-1){
//				double t1 = -startVY/g;
//				double s = (startVY*t1+0.5*g*t1*t1);
//				double t2 =Math.sqrt(((2*(pointList.get(now+1).y-pointList.get(now).y+(Math.abs(s))))/g));
//				double t = t1+t2;
//				startVX = (float)((pointList.get(now+1).x-startX)/(t));
//				currentVX = startVX;
//				GROUND_LING = now+1;
//				bt = new BallThread();
//				bt.start();
//			}
//
//
//
//		};
//	};
}
