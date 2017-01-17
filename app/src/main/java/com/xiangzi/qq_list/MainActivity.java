package com.xiangzi.qq_list;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
	static BallView list;
	ChatAdapter chatAdapter ;
	List<Chat> chatlist = new ArrayList<Chat>();
	List<Point> pointList = new ArrayList<Point>();
	Button btn ;
	String[] s = {"苍茫的天涯是我的爱" ,
			"绵绵的青山脚下花正开" ,
			"什么样的节奏是最呀最摇摆" ,
			"什么样的歌声才是最开怀",
			"弯弯的河水从天上来",
			"流向那万紫千红一片海" ,
			"火辣辣的歌谣是我们的期待",
			"一路边走边唱才是最自在",
			"我们要唱就要唱得最痛快",
			"你是我天边 最美的云彩",
			"让我用心把你留下来（留下来）",
			"悠悠的唱着 最炫的民族风",
			"让爱卷走所有的尘埃",
			"（我知道）"};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list=(BallView) findViewById(R.id.list);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int x= (int)(displayMetrics.widthPixels);
		pointList.add(new Point(120 , 50));
		pointList.add(new Point(x - 140 ,250));
		pointList.add(new Point(x - 180 ,450));
		pointList.add(new Point(100 , 650));
		pointList.add(new Point(x - 140 , 800));
		pointList.add(new Point(x - 160 , (int)(displayMetrics.heightPixels)+40));

//		for (int i = 0;i<14;i++){
//			Chat chat = new Chat();
//			chat.chatfrom = (int)(Math.random()*(2));
//			chat.text = s[i];
//			chatlist.add(chat);
//		}
//		chatAdapter = new ChatAdapter(this , chatlist );
//		list.setAdapter(chatAdapter);
		btn = (Button)findViewById(R.id.button);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				int firstPosition = list.getFirstVisiblePosition();
//				int lastPosition = list.getLastVisiblePosition();
//				System.out.println("position::"+firstPosition);
//				View view = list.getChildAt(lastPosition);
//				int height  = view.getTop();
//				System.out.println("height::"+height);

				list.initMovables(pointList);


			}
		});
	}


}
