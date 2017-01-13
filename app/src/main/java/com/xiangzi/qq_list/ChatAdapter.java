package com.xiangzi.qq_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ChatAdapter extends BaseAdapter {
	private List<Chat> chatList = new ArrayList<Chat>();
	private Context context;
	private LayoutInflater mInflater = null;
	public ChatAdapter(Context context, List<Chat> chatList)
	{
		this.context=context;
		this.mInflater = LayoutInflater.from(context);
		this.chatList = chatList;

	}
	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public Object getItem(int i) {
		return chatList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		view =mInflater.inflate(R.layout.list_item, null);
		ViewHolder viewHolder = null;
		if(view!=null){
			viewHolder = new ViewHolder();
			viewHolder.leftPointView= view.findViewById(R.id.pointleft);
			viewHolder.leftTx = (TextView) view .findViewById(R.id.textl);
			viewHolder.leftRel = (RelativeLayout) view.findViewById(R.id.chatfrom);
			viewHolder.RightTx = (TextView) view .findViewById(R.id.textr);
			viewHolder.rightPointView = view.findViewById(R.id.pointright);
			viewHolder.rightRel = (RelativeLayout) view.findViewById(R.id.chatto);;
			if (chatList.get(i).chatfrom==0){
				viewHolder.leftPointView.setVisibility(View.VISIBLE);
				viewHolder.leftTx.setVisibility(View.VISIBLE);
				viewHolder.leftRel.setVisibility(View.VISIBLE);
				viewHolder.RightTx.setVisibility(View.GONE);
				viewHolder.rightPointView.setVisibility(View.GONE);
				viewHolder.rightRel.setVisibility(View.GONE);
				viewHolder.leftTx.setText(chatList.get(i).text);

			}else{
				viewHolder.leftPointView.setVisibility(View.GONE);
				viewHolder.leftTx.setVisibility(View.GONE);
				viewHolder.leftRel.setVisibility(View.GONE);
				viewHolder.RightTx.setVisibility(View.VISIBLE);
				viewHolder.rightPointView.setVisibility(View.VISIBLE);
				viewHolder.rightRel.setVisibility(View.VISIBLE);
				viewHolder.RightTx.setText(chatList.get(i).text);

			}

		}
		return view;
	}
	public static class ViewHolder{
		public TextView leftTx,RightTx;
		public View leftPointView,rightPointView;
		public RelativeLayout leftRel,rightRel;


	}
}
