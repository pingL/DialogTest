> 你是开发，他是产品，你和他PK了一天需求，他需求有了，你的代码呢？

[Image link](http://a3.qpic.cn/psb?/V12m5UAf3uSsPC/Nvqj6JCQLTXR0pdzrMhCmawhnBF5isElw7SgnBjMfXA!/b/dPgAAAAAAAAA&bo=zwIABc8CAAUFCSo!&rf=viewer_4)

昨天一朋友在朋友圈发了一张图，大概说这么一件事，程序员去找产品聊需求，他们聊了很长时间，最终确定了需求，花费了一天时间，需求是定下来了，可程序员一行代码都没有。这不同程度的暗示，这个程序员的工作没效率，用一天时间而没任何产出。
其实，作为我自己来说，还是很希望能够第一时间介入产品，要是产品懂技术的话就没什么问题，如果只是单纯的做产品的话就更需要及时想和产品部门的人去沟通，在了解需求走向的同时，把平台上实现的特性和缺陷告知产品，在最后确定产品后就能更主动、快速的选择适合自己的实现方案。


## 一、需求

公司需要做一批活动入口，首先这个活动入口是有时间周期的，比如说到了十一国庆活动就显示，过了活动时间就隐藏。
这批活动入口则是通过外部的一个图标控制显示隐藏。
外部的图标还有一个动画效果，类似水波纹，但要求不用那么高，只需要它能够闪烁。
点击这个外部图标后弹出一组活动图标，根据活动时间，节前是一组六个活动图标，节后是一组九个图标。
这两组的每个图标本地设定的链接点击跳转。

## 二、方案
依据需求，显示隐藏可以获取系统时间与活动时间进行比较。外部按钮可以使用Animation动画来实现闪烁效果。两组活动入口图标则相对比较复杂，因为有两组数量不一致的图标，则它的显示高度就不一样，这可以通过自定义Dialog，并且动态显示需要的高度。

## 三、流程图
  [Demo流程图](http://a2.qpic.cn/psb?/V12m5UAf3uSsPC/ffMoyiwa.*4t09YLZ0P958Rgo70.oln.ncoJwiB.*dw!/b/dKwAAAAAAAAA&bo=DAIfAgwCHwIDACU!&rf=viewer_4)

## 四、主要组件实现方法
* 标准时间转换成时间

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

* Animation简单动画

            AlphaAnimation animation = new AlphaAnimation(1, 0);//可见到不可见
            animation.setDuration(1000);//持续时间
            animation.setStartOffset(500);//启动时间
            animation.setInterpolator(new LinearInterpolator());//不可变动画速度
            animation.setRepeatCount(Animation.INFINITE);//无限重复动画效果
            animation.setRepeatMode(Animation.RESTART);//结束后重新开始
            heroBlink = (ImageView) findViewById(R.id.hero_buttonBottom);
            heroBlink.startAnimation(animation);
            
* Dialog style

        <style name="share_dialog_style" parent="@android:style/Theme.Dialog">
        <item name="android:windowFrame">@null</item>
        <item name="android:windowIsFloating">false</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:background">@android:color/transparent</item>
        //背景透明
        <item name="android:windowBackground">@android:color/transparent</item>
        //是否允许Dialog背景变暗
        <item name="android:backgroundDimEnabled">true</item>
        <item name="android:windowFullscreen">false</item>
        <item name="android:backgroundDimAmount">0.6</item>
        //进出动画
        <item name="android:windowAnimationStyle">@style/BottomDialogAnimation</item>
        </style>
        
* dip转换成px

        public class Utils {
          public Utils() {
          super();
         }
        public static float dp2px (Resources resources, float dp) {
            float scale = resources.getDisplayMetrics().density;
            return dp * scale + 0.5F;
        }
        public static float sp2dx (Resources resources, float sp) {
            float scale = resources.getDisplayMetrics().scaledDensity;
            return sp * scale;
        }
       }

* 外部按钮防多次点击控制

        private void initView() {
        hero_button_ll = (RelativeLayout) this.findViewById(R.id.hero_button_ll);
        hero_button = (ImageView) this.findViewById(R.id.hero_button);
        hero_button.setClickable(true);//点击前设置为true
        hero_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null == heroDialog || !heroDialog.isShowing()){
                    hero_button.setClickable(false);// 如果已经显示则设置为false
                    showHeroDialog();
                    heroDialog.show();
                }
            }
        });
        }
And 资源释放

      heroDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // 释放资源
                hero_button.setClickable(true);
            }
        });


* 图标、名称以及Url均以数组形式存入资源文件，以及代码如何对其引用

      <integer-array name="heroIcon">
            <item>@drawable/introduction</item>
            <item>@drawable/date</item>
            <item>@drawable/activity</item>
            <item>@drawable/route</item>
            <item>@drawable/play</item>
            <item>@drawable/topic</item>
      </integer-array>
      <string-array name="heroIconName">
            <item>简介</item>
            <item>日程</item>
            <item>活动</item>
            <item>路线</item>
            <item>玩转</item>
            <item>专题</item>
      </string-array>
      <string-array name="heroUrl">
            <item>http://www.fblife.com</item>
            <item>http://www.fblife.com</item>
            <item>http://www.fblife.com</item>
            <item>http://www.fblife.com</item>
            <item>http://www.fblife.com</item>
            <item>http://www.fblife.com</item>
      </string-array>
 
  在代码里对string类型和int类型的数组应用值得注意
      heroIconName = getResources().getStringArray(R.array.heroIconName);
      heroIcon = getResources().obtainTypedArray(R.array.heroIcon);//icon是int类型
      heroUrl = getResources().getStringArray(R.array.heroUrl);

## 五、文件目录及代码分析

#### 文件目录 
res\values:
>strings.xml (两组图标、名称、Url 用数组方式存入资源文件)

res\layout目录下：
>hero_main.xml (外部入口按钮的布局)
>dialog_hero.xml (dialog布局)
>hero_item.xml (dialog里面每个item布局)

main\java目录下：
>业务bean: HeroBean.java （分别有int icon, string text, string url）
>工具类utils: Utils.java (用于dip 转换成 px)
>数据绑定类HeroDialogAdapter.java (绑定视图对象，并对每个对象设置监听事件)
>主类MainActivity.java (业务流程的执行)

#### 主要代码分析

>完整的项目已经发到[Github](https://github.com/pingL/DialogTest) ,经过多次修改，尽可能的优化了，如果有什么疑问随时可以找我，我就坐在那里，我估计不认识我的人也不会看到这里 :）game over··

## 六、项目需要完善的地方

这个项目有个关键的地方，就是动态的显示外部按钮的高度和Dialog的高度，因为是使用的时间判断的，如果应用不重新启动，也就不重新走onCreate方法，这两个高度始终不会改变的。有个方法是设置广播监听，还在研究中。如果下周不加班的话就继续改进下。

1·广播监听： 定时任务之-Quartz 或者 BroadcastReceiver

