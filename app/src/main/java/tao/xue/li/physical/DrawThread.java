package tao.xue.li.physical;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/*
 * 这个类的作用有两个
 * （1）定时重绘屏幕
 * （2）计算帧速率
 */

public class DrawThread extends Thread {
	BallView bv;
	SurfaceHolder surfaceHolder;
	boolean flag = false;	//线程执行标志位
	int sleepSpan = 30;	//休眠时间 
	long start = System.nanoTime();	//记录起始时间，该变量用于计算帧速率
	int count = 0;	//记录帧数，该变量用于计算帧速率
	
	public DrawThread(BallView bv, SurfaceHolder surfaceHolder){
		this.bv = bv;
		this.surfaceHolder = surfaceHolder;
		this.flag = true;
	}
	
	public void run(){
		Canvas canvas = null;
		while(flag){
			try{
				canvas = surfaceHolder.lockCanvas(null);	//获取Ballview的画布
				synchronized(surfaceHolder){	//对象锁
					bv.doDraw(canvas);	//调用BallView的doDraw方法进行绘制
				}
			}catch(Exception e){
				e.printStackTrace();	
			}finally{
				if(canvas != null){
					surfaceHolder.unlockCanvasAndPost(canvas);	//判断一下，如果画布不为空，就解锁画布
				}
			}
			this.count++;
			if(count == 20){	//如果记满20帧
				count = 0;	//清空计数器
				long tempStamp = System.nanoTime();
				long span = tempStamp - start;	//记录时间间隔，即记满20帧需要的时间
				start = tempStamp;
				double fps = Math.round(100000000000.0 / span * 20)/100.0;	//计算帧速率
																			//（100s内包含的时间间隔（span)个数*20，即100s能绘制的帧数）
				bv.fps = "FPS:" + fps;	//将帧速率
			}
			try{
				Thread.sleep(sleepSpan);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
