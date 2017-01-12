package tao.xue.li.physical;

/*
 * 该类为一个物理引擎，主要功能是改变小球运动轨迹！另，在android中，坐标系以Y向下为正
 * 全是初中就学过的物理，我觉得大家应该能看懂
 * 这个类就是这整个程序的核心了~~~，物理引擎啊，好好看吧，我尽量注释详细点
 */

public class BallThread extends Thread {
	Movable father;	//声明一个小球的实例
	boolean flag = false;	//线程是否执行的标记
	int sleepSpan = 10;	//休眠时间
	public static float g = 4000;	//求落下的加速度
	double current;	//记录当前时间
	double currentX;
	public BallThread(Movable father){	//让每个小球都要自己的物理引擎，按自己的引擎去运动
		this.father = father;
		currentX = father.timeX;
		this.flag = true;	//一旦初始化线程就开始执行
	}

	//run方法进行两项工作：（run方法又是整个这个类的核心）
	//（1）按照物理公式移动改变小球位置
	//（2）检测并处理小球达到最高点以及撞击地面的事件
	//而且，请各位注意，每个阶段一开始的时候，要把所有的时间的记录啊，X,Y的速度啊，X,Y位置啊之类的全更新
	public void run(){
		while(flag){	//只要线程在，就一直循环
			current = System.nanoTime();	//取得当前的时间
			System.out.println("father.currentX:  "+father.currentX);
			double timeSpanX = (double)((current - father.timeX)/1000/1000/1000);
			father.currentX = (int)(father.startX + father.currentVX * timeSpanX);


			//下面处理竖直方向的运动
			if(father.bFall){	//bFall是判断小球有没有从木板上落下来，落下来了才有竖向速度
				double timeSpanY = (double)((current - father.timeY)/1000/1000/1000);
				father.currentY = (int)(father.startY + father.startVY * timeSpanY + timeSpanY * timeSpanY * g/2);	//s=vt+1/2at
				father.currentVY = (float)(father.startVY + g * timeSpanY);	//求竖向当前速度，当前速度=v + at(高中物理)
				//下面这个是判断小球是否达到最高点
				if(father.startVY < 0 && Math.abs(father.currentVY) <= BallView.UP_ZERO){	//startVY<0证明小球是向上运动的
					father.timeY = System.nanoTime();	//设置新的运动阶段竖直方向的开始时间
					father.currentVY = 0;	//设置新的运动阶段竖直方向的实时速度，达到最高点是时，速度肯定为0
					father.startVY = 0;	//设置新的运动阶段竖直方向的初始速度，达到最高点以后开始改变方向，起始速度为0
					father.startY = father.currentY;	//设置新的运动阶段竖直方向的初始位置，这时刚刚要改变运动方向，所以初始位置=当前位置
				}
				//判断小球是否落地
				if(father.currentY + father.r*2 >= BallView.GROUND_LING && father.currentVY > 0){
					father.currentVX = father.currentVX * (father.impactFactoryX);	//衰减水平方向的速度
					father.currentVY = 0 - father.currentVY * (father.impactFactory);	//衰减竖向的速度，并改变速度的方向（反弹）
					if(Math.abs(father.currentVY) < BallView.DOWN_ZERO){	//如果衰减后的速度小于DOWN_ZERO，就停止线程的执行，Math.adb求绝对值
						this.flag = false;
					}else{	//如果衰减后速度还够，就弹起
						father.startX = father.currentX;	//弹起时水平方向的起始位置
						father.timeX = System.nanoTime();	//记录弹起时的时间
						father.startY = father.currentY;
						father.timeY = System.nanoTime();
						father.startVY = father.currentVY;
					}
				}
			}else {	//当前位置加上小球半径的一半，大于这个值，小球就掉落了
				//BallView.WOODEDGE是木板的长度
				//开始处理水平方向的运动
				//获取水平方向走过的时间，当前时间减掉小球初始化时候的时间，就是时间差，除以3个1000就得到s，nanotime得到的是ns
				if(father.currentVY == 0 || father.currentVY > 0){
					father.timeY = System.nanoTime();	//记录竖直方向上的开始运动时间
					father.bFall = true;	//一定会先执行这个else，才执行上面这个if，即timeY会先有值。看判断条件
				}else{


					father.currentVY = (int)(father.startVY + g * timeSpanX);
					father.currentY = (int)(father.startVY + father.startVY * timeSpanX + timeSpanX * timeSpanX * g/2);

//					System.out.println("father.currentY:  "+father.currentY);
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
