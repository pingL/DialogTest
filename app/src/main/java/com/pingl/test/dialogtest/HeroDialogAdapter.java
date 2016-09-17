package com.pingl.test.dialogtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pingl.test.dialogtest.R;
import com.pingl.test.dialogtest.bean.HeroBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeroDialogAdapter extends BaseAdapter {

    private Context mContext;
    private List<HeroBean> heroItem_list;


    public HeroDialogAdapter(Context mContext, List<HeroBean> heroItem_list) {
        this.heroItem_list = heroItem_list;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return heroItem_list.size();
    }

    @Override
    public Object getItem(int position) {
        return heroItem_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hero_item, parent, false);
            holder.mIcon = (ImageView) convertView.findViewById(R.id.hero_icon);
            holder.mText = (TextView) convertView.findViewById(R.id.hero_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mIcon.setImageResource(heroItem_list.get(position).getIcon());
        holder.mText.setText(heroItem_list.get(position).getText());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, position + "Balabala", Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView mIcon;
        TextView mText;


    }
}
