package com.xiangzi.qq_list;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;


/**
 * @author 知乎：祥子，转载需联系
 * @date 2017年1月6日
 */

public class XiangZi_QQ_Cricle_View extends View {

	private Paint mPaint;
	private float circle_A_Center = 200f;//A圆圆心位置
	private float circle_A_Radius = 15f;// A圆直径
	private float circle_B_Center = 200f;//B圆圆心位置
	private float circle_B_Radius = 20f;// B圆直径
	private float mMaxDistance = 120f;// 最大移动距离
	private boolean isOutRange;//是否超出范围
	private boolean isDisappear;//是否消失
	private PointF[] circle_A_PointFs = new PointF[2];// 存储A圆相对于B圆的两个点1和2坐标
	private PointF[] circle_B_PointFs = new PointF[2];// 存储B圆相对于A圆的两个点3和4坐标
	private PointF mControlPointF = new PointF();// 贝塞尔曲线中心控制点坐标
	private PointF circle_A_Center_PointF = new PointF(circle_A_Center, circle_A_Center);//A圆圆心坐标
	private PointF circle_B_Center_PointF = new PointF(circle_B_Center, circle_B_Center);// B圆圆心坐标


	public XiangZi_QQ_Cricle_View(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XiangZi_QQ_Cricle_View(Context context) {
		this(context, null);
	}

	public XiangZi_QQ_Cricle_View(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.RED);
	}


	@Override
	protected void onDraw(Canvas canvas) {

		// 获得两个圆心之间临时的距离distance
		float distance = (float) Math.sqrt(Math.pow(circle_B_Center_PointF.y - circle_A_Center_PointF.y, 2) + Math.pow(circle_B_Center_PointF.x - circle_A_Center_PointF.x, 2));
		distance = Math.min(distance, mMaxDistance);

		//A圆随着距离distance变大，大小逐渐缩小的计算
		float percent = distance / mMaxDistance;
		float circle_A_Radius_percentChange =((Number)circle_A_Radius).floatValue() + percent * (((Number)(circle_A_Radius * 0.2f)).floatValue() - ((Number)circle_A_Radius).floatValue());


		float yOffset = circle_A_Center_PointF.y - circle_B_Center_PointF.y;
		float xOffset = circle_A_Center_PointF.x - circle_B_Center_PointF.x;
		Double lineK = 0.0;
		if (xOffset != 0f) {
			lineK = (double) (yOffset / xOffset);
		}
		// 求两个点的集合
		circle_B_PointFs = getIntersectionPoints(circle_B_Center_PointF, circle_B_Radius, lineK);
		circle_A_PointFs = getIntersectionPoints(circle_A_Center_PointF, circle_A_Radius_percentChange, lineK);
		// 通过公式求得贝塞尔曲线控制点
		mControlPointF = new PointF((circle_A_Center_PointF.x + circle_B_Center_PointF.x) / 2.0f, (circle_A_Center_PointF.y + circle_B_Center_PointF.y) / 2.0f);


		canvas.save();
		if (!isDisappear) {
			if (!isOutRange) {

				Path path = new Path();
				path.moveTo(circle_A_PointFs[0].x, circle_A_PointFs[0].y);
				path.quadTo(mControlPointF.x, mControlPointF.y, circle_B_PointFs[0].x, circle_B_PointFs[0].y);
				path.lineTo(circle_B_PointFs[1].x, circle_B_PointFs[1].y);
				path.quadTo(mControlPointF.x, mControlPointF.y, circle_A_PointFs[1].x, circle_A_PointFs[1].y);
				path.close();

				canvas.drawPath(path, mPaint);
				canvas.drawCircle(circle_A_Center_PointF.x, circle_A_Center_PointF.y, circle_A_Radius_percentChange, mPaint);
			}

			// 画移动的大圆
			canvas.drawCircle(circle_B_Center_PointF.x, circle_B_Center_PointF.y, circle_B_Radius, mPaint);
		}
		canvas.restore();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float downX = 0.0f;
		float downY = 0.0f;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isOutRange = false;
				isDisappear = false;
				downX = event.getX();
				downY = event.getY();
				circle_B_Center_PointF.set(downX, downY);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				downX = event.getX();
				downY = event.getY();
				circle_B_Center_PointF.set(downX, downY);
				invalidate();

				// 当超过最大值时断开
				float distance = (float) Math.sqrt(Math.pow(circle_B_Center_PointF.y - circle_A_Center_PointF.y, 2)
						+ Math.pow(circle_B_Center_PointF.x - circle_A_Center_PointF.x, 2));
				if (distance > mMaxDistance) {
					isOutRange = true;
					invalidate();
				}

				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if (isOutRange) {
					distance = (float) Math.sqrt(Math.pow(circle_B_Center_PointF.y - circle_A_Center_PointF.y, 2) + Math.pow(circle_B_Center_PointF.x - circle_A_Center_PointF.x, 2));
					if (distance > mMaxDistance) {
						isDisappear = true;
						invalidate();
					}else {
						circle_B_Center_PointF.set(circle_A_Center_PointF.x, circle_A_Center_PointF.y);
						invalidate();
					}

				}else {

					final PointF tempMovePointF = new PointF(circle_B_Center_PointF.x, circle_B_Center_PointF.y);
					ValueAnimator vAnim = ValueAnimator.ofFloat(1.0f);
					vAnim.addUpdateListener(new AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							float percent = animation.getAnimatedFraction();
							PointF pointF =
									new PointF(((Number)(tempMovePointF.x)).floatValue() + (((Number)(circle_A_Center_PointF.x)).floatValue() - ((Number)(tempMovePointF.x)).floatValue()) * percent
											, ((Number)(tempMovePointF.y)).floatValue() + (((Number)(circle_A_Center_PointF.y)).floatValue() - ((Number)(tempMovePointF.y)).floatValue()) * percent );
							circle_B_Center_PointF.set(pointF.x, pointF.y);
							invalidate();
						}
					});
					vAnim.setInterpolator(new OvershootInterpolator(4));
					vAnim.setDuration(500);
					vAnim.start();
				}

				break;

		}
		return true;
	}





	public static PointF[] getIntersectionPoints(PointF pMiddle, float radius, Double lineK) {
		PointF[] points = new PointF[2];

		float radian, xOffset = 0, yOffset = 0;
		if (lineK != null) {
			radian = (float) Math.atan(lineK);
			xOffset = (float) (Math.sin(radian) * radius);
			yOffset = (float) (Math.cos(radian) * radius);
		} else {
			xOffset = radius;
			yOffset = 0;
		}
		points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
		points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);

		return points;
	}
}
