package tao.xue.li.physical;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/*这个类是定义一个小球具有的属性和方法*/
public class Movable {
	int startX = 0;
	int startY = 0;		//小球的初始坐标X,Y，小球的实时坐标应该等于初始坐标加位移
	int currentX;
	int currentY;		//实时坐标
//	float Ax = 0f;      //x方向加速度
	float startVX = 0f;
	float startVY = 0f;		//初始时水平和竖直方向的速度
	float currentVX = 0f;
	float currentVY = 0f;	//实时速度
	int r;		//可移动物体的半径
	double timeX;
	double timeY;		//物体在X，Y上移动的时间，当物体从一个阶段进入到另一个阶段，此属性被重置
	Bitmap bitmap = null;		//声明一个要使用的图片
	BallThread bt = null;		//自己写的物理引擎，为一个线程，将根据物理公式对小球的位置坐标等属性修改
	boolean bFall = false;		//判断小球是否从木板上落下来了
	float impactFactory = 0.5f;		//小球撞地后的速度衰减系数
	float impactFactoryX = 0.00000000001f;		//小球撞地后的X速度衰减系数
	int finalX = 500;

	public Movable(int x, int y, float startVX,float startVY,int r, Bitmap bitmap){
		this.startX = x;
		this.startY = y;
		this.currentX = x;
		this.currentY = y;		//构造函数初始化的时候，初始位置和实时位置相同
		this.r = r;
		this.startVX = startVX;
		this.startVY = startVY;
		this.finalX = finalX;
		this.bitmap = bitmap;
		/*
		 * System.nanoTime是专门用来算间隔的(衡量时间段)。System.nanoTime()返回的是纳秒，nanoTime而返回的可能是任意时间，
		 * 甚至可能是负数
		 * System.currentTimeMillis()返回的毫秒，这个毫秒其实就是自1970年1月1日0时起的毫秒数，Date()其实就是相当于
		 * Date(System.currentTimeMillis());因为Date类还有构造Date(long date)，用来计算long秒与1970年1月1日之间的毫秒差。
		 */
		timeX = System.nanoTime();
		this.currentVX = startVX;
		this.currentVY = startVY;
		bt = new BallThread(this);
		bt.start();		//创建并启动物理引擎线程
	}

	public void drawSelf(Canvas canvas){		//将自己绘制（Bitmap贴图）画到画布上
		canvas.drawBitmap(bitmap, currentX, currentY, null);
	}
}
