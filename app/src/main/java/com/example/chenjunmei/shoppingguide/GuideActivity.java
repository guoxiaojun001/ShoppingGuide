package com.example.chenjunmei.shoppingguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends Activity {

    @Bind(R.id.viewpage)
    ViewPager viewpage;
    @Bind(R.id.iv_skip)
    ImageView ivSkip;
    @Bind(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @Bind(R.id.iv_white_point)
    ImageView ivWhitePoint;
    @Bind(R.id.activity_guild)
    RelativeLayout activityGuild;

    private View splash5;
    private ArrayList<ImageView> imageViews;
    private int leftmax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.初始化视图
        initView();

        //2.初始化数据
        initData();

        //3.初始化监听
        initListener();
    }

    /**
     * 开启MainActivity
     * 并销毁当前的activity
     */
    private void startMainActivity() {
        startActivity(new Intent(GuideActivity.this,MainActivity.class));
        finish();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        setContentView(R.layout.activity_guild);
        ButterKnife.bind(this);
        splash5 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.fragment_entry, null);

    }


    /**
     * 初始化数据
     */
    private void initData() {
        //引导页图片数据的id的数组
        int[] ids = new int[]{
                R.drawable.splash_1,
                R.drawable.splash_2,
                R.drawable.splash_3,
                R.drawable.splash_4};

        //放引导页图片的集合
        imageViews = new ArrayList<>();

        int widthdpi = DensityUtil.dip2px(this, 10);

        for (int i = 0; i < ids.length + 1; i++) {

            //创建小圆点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi, widthdpi);

            //除了第一个点，其他点都要给它设置左边距
            if (i != 0) {
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            //添加小圆点
            llPointGroup.addView(point);

            if (i == ids.length) {

            } else {
                //将图片封装成imageview的对象
                ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(ids[i]);

                //将iamgeview添加到集合中
                imageViews.add(imageView);
            }

        }
        //给viewpager设置适配器
        viewpage.setAdapter(new MyPagerAdapter());
    }


    //适配器
    class MyPagerAdapter extends PagerAdapter {
        //返回数据总个数
        @Override
        public int getCount() {
            return imageViews.size() + 1;
        }

        //当前视图
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //销毁视图
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        //显示视图
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position < imageViews.size()) {
                view = imageViews.get(position);
                container.addView(view);
            } else {
                view = splash5;
                container.addView(view);
            }
            return view;
        }
    }

    /**
     * 点击右上角的skip图标时进入主页面
     */
    @OnClick(R.id.iv_skip)
    public void onClick() {
//        CacheUtils.setBoolean(GuideActivity.this,Splash_init.START_MAIN,true);
//        SnackbarUtil.showMessage(viewpage,"即将体验");
        startMainActivity();
    }


    /**
     * 绑定监听
     */
    private void initListener() {
        //获取树形视图，每次页面布局完成时会调用，获取点间的距离
        ivWhitePoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //页面改变时
        viewpage.addOnPageChangeListener(new MyOnPageChangeListener());

        splash5.findViewById(R.id.iv_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CacheUtils.setBoolean(GuideActivity.this,Splash_init.START_MAIN,true);
//                SnackbarUtil.showMessage(viewpage,"直接进入");
                startMainActivity();

            }
        });
    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //默认会调用俩次，只需要一次，第一次进入就移除
            ivWhitePoint.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);
            //间距 = 第1个点距离左边距离 - 第0个点距离左边距离
            leftmax = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滑动回调会调用此方法
         *
         * @param position             当前页面位置
         * @param positionOffset       当前页面滑动百分比
         * @param positionOffsetPixels 滑动的像素数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离 = ViewPager页面的百分比* 间距
            //坐标 = 起始位置 + 红点移动的距离；
            int leftmagin = (int) (position * leftmax + (positionOffset * leftmax));
            //获取控件iv_red_point
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivWhitePoint.getLayoutParams();
            //设置iv_red_point属性
            params.leftMargin = leftmagin;
            //绑定到控件上
            ivWhitePoint.setLayoutParams(params);

        }

        /**
         * 页面被选中，回调此方法
         *
         * @param position 被选中的页面位置
         *                 此作用是最后一张图片显示button
         */
        @Override
        public void onPageSelected(int position) {

        }

        /**
         * 当页面滑动状态改变时候
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    }
