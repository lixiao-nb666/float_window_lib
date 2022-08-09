package com.newbee.floatwindowlibrary.view.circlemenu.view.adapter;

import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.newbee.floatwindowlibrary.R;
import com.newbee.floatwindowlibrary.view.circlemenu.view.bean.CircleItemInfo;


public class CircleMenuAdapter extends BaseAdapter {

	private List<CircleItemInfo> data;

	public CircleMenuAdapter(List<CircleItemInfo> itemInfos) {
		this.data = itemInfos;
	}

	@Override
	public int getCount() {
		if (data == null || data.isEmpty()) {
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(), R.layout.item_circle_menu, null);
			holder = new ViewHolder();
			holder.iv =  convertView.findViewById(R.id.iv_circle_menu_item);
			holder.iv.setScaleType(ImageView.ScaleType.CENTER);

			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		CircleItemInfo item = data.get(position);
		if (item != null) {
			holder.iv.setImageResource(item.getImgId());

		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv;

	}

}
