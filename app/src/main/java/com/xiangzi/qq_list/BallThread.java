package com.xiangzi.qq_list;

import android.graphics.Canvas;

public class BallThread extends Thread {
	Movable father;
	boolean flag = false;
	int sleepSpan = 10;
	public static float g = 4000;
	double current;
	double currentX;
	Canvas canvas;
	public BallThread(Movable father, Canvas canvas){
		this.father = father;
		currentX = father.timeX;
		this.flag = true;
		this.canvas = canvas;
	}

	public void run(){
		while(flag){
			father.drawSelf(canvas);
			current = System.nanoTime();
			System.out.println("father.currentY:  "+father.currentY);
			System.out.println("father.currentX:  "+father.currentX);
			double timeSpanX = (double)((current - father.timeX)/1000/1000/1000);
			father.currentX = (int)(father.startX + father.currentVX * timeSpanX);


			if(father.bFall){
				double timeSpanY = (double)((current - father.timeY)/1000/1000/1000);
				father.currentY = (int)(father.startY + father.startVY * timeSpanY + timeSpanY * timeSpanY * g/2);
				father.currentVY = (float)(father.startVY + g * timeSpanY);
				if(father.startVY < 0 && Math.abs(father.currentVY) <= BallView.UP_ZERO){
					father.timeY = System.nanoTime();
					father.currentVY = 0;
					father.startVY = 0;
					father.startY = father.currentY;
				}
				if(father.currentY + father.r*2 >= BallView.GROUND_LING && father.currentVY > 0){
					father.currentVX = father.currentVX * (father.impactFactoryX);
					father.currentVY = 0 - father.currentVY * (father.impactFactory);

					if(Math.abs(father.currentVY) < BallView.DOWN_ZERO){
						this.flag = false;
					}else{
						father.startX = father.currentX;
						father.timeX = System.nanoTime();
						father.startY = father.currentY;
						father.timeY = System.nanoTime();
						father.startVY = father.currentVY;
					}
				}
			}else {
				if(father.currentVY == 0 || father.currentVY > 0){
					father.timeY = System.nanoTime();
					father.bFall = true;
				}else{


					father.currentVY = (int)(father.startVY + g * timeSpanX);
					father.currentY = (int)(father.startVY + father.startVY * timeSpanX + timeSpanX * timeSpanX * g/2);


				}


			}

			try{
				Thread.sleep(sleepSpan);
			}catch(Exception e){
				e.printStackTrace();
			}

		}
	}
}
