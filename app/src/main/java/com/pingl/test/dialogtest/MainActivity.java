package com.pingl.test.dialogtest;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pingl.test.dialogtest.bean.HeroBean;
import com.pingl.test.dialogtest.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout dialog_hero_ll;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout hero_button_ll;
    private ImageView hero_button;
    private ImageView heroView_exit;
    private ImageView heroBlink;
    private Dialog heroDialog;
    private GridView gridView;
    private List<HeroBean> heroItem_list;
    private HeroDialogAdapter mHeroDialogAdapter;
    private View dialogView;

    String[] heroIconName = null;
    TypedArray heroIcon = null;
    String[] heroUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_main);
        initView();
        long systemTime = System.currentTimeMillis();
        if (systemTime <= judgeTime("20161007 18:00:00")) {
            hero_button_ll.setVisibility(View.VISIBLE);
            if (systemTime <= judgeTime("20161001 00:00:00")) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(this.getResources(), 166));
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                hero_button_ll.setLayoutParams(params);
            } else {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(this.getResources(), 249));
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                hero_button_ll.setLayoutParams(params);
            }

            AlphaAnimation animation = new AlphaAnimation(1, 0);//可见到不可见
            animation.setDuration(1000);//持续时间
            animation.setStartOffset(500);//启动时间
            animation.setInterpolator(new LinearInterpolator());//不可变动画速度
            animation.setRepeatCount(Animation.INFINITE);//无限重复动画效果
            animation.setRepeatMode(Animation.RESTART);//结束后重新开始
            heroBlink = (ImageView) findViewById(R.id.hero_buttonBottom);
            heroBlink.startAnimation(animation);
        } else {
            hero_button_ll.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        hero_button_ll = (RelativeLayout) this.findViewById(R.id.hero_button_ll);
        hero_button = (ImageView) this.findViewById(R.id.hero_button);
        hero_button.setClickable(true);
        hero_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null == heroDialog || !heroDialog.isShowing()){
                    hero_button.setClickable(false);
                    showHeroDialog();
                    heroDialog.show();
                }
            }
        });
    }

    /**
     * 标准时间转换成时间戳
     *
     * @param showTime
     * @return
     */
    private long judgeTime(String showTime) {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = null;
        try {
            date = sdfTime.parse(showTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    private Dialog showHeroDialog(){
        long systemTime = System.currentTimeMillis();
        long longTime = judgeTime("20161001 00:00:00");
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_hero, null);
        gridView = (GridView) dialogView.findViewById(R.id.gv_item);

        heroDialog = new Dialog(this, R.style.share_dialog_style);
        heroDialog.setCanceledOnTouchOutside(true);
        heroDialog.setContentView(dialogView);
        Window window = heroDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        dialog_hero_ll = (LinearLayout) heroDialog.findViewById(R.id.dialog_hero_ll);
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (systemTime <= longTime) {
            heroIconName = getResources().getStringArray(R.array.heroIconName);
            heroIcon = getResources().obtainTypedArray(R.array.heroIcon);
            heroUrl = getResources().getStringArray(R.array.heroUrl);
            layoutParams.height = (int) Utils.dp2px(this.getResources(), 166);
            params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(this.getResources(), 166));
        } else {
            heroIconName = getResources().getStringArray(R.array.heroIconName1);
            heroIcon = getResources().obtainTypedArray(R.array.heroIcon1);
            heroUrl = getResources().getStringArray(R.array.heroUrl1);
            layoutParams.height = (int) Utils.dp2px(this.getResources(), 249);
            params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Utils.dp2px(this.getResources(), 249));
        }
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        layoutParams.alpha = 0.95f;
        hero_button.setLayoutParams(params);
        heroItem_list = getData(heroIcon, heroIconName, heroUrl);
        mHeroDialogAdapter = new HeroDialogAdapter(this, heroItem_list);
        gridView.setAdapter(mHeroDialogAdapter);
        heroView_exit = (ImageView) heroDialog.findViewById(R.id.hero_button1);
        heroView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != heroDialog && heroDialog.isShowing()){
                    heroDialog.dismiss();
                }
            }
        });
        heroDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // 释放资源
                hero_button.setClickable(true);
            }
        });
        return heroDialog;
    }


    public List<HeroBean> getData(TypedArray heroIcon, String[] heroIconName, String[] heroUrl) {
        HeroBean bean = null;
        List<HeroBean> list = new ArrayList<>();
        for (int i = 0; i < heroIconName.length; i++) {
            bean = new HeroBean();
            bean.setIcon(heroIcon.getResourceId(i, 0));
            bean.setText(heroIconName[i]);
            bean.setUrl(heroUrl[i]);
            list.add(bean);
        }
        return list;
    }


}
