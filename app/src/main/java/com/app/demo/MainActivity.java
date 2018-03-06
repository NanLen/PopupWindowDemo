package com.app.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * 示例 showAsDropDown 的兼容问题
 */
public class MainActivity extends AppCompatActivity {
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private PopupWindow popupWindowOne;
    private PopupWindow popupWindowTwo;
    private PopupWindow popupWindowThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void initView() {
        btnOne = (Button) findViewById(R.id.btn_one);
        btnTwo = (Button) findViewById(R.id.btn_two);
        btnThree = (Button) findViewById(R.id.btn_three);
    }

    private void setListener() {
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在7.0以上系统会有兼容问题,7.0以下显示正常
                if (popupWindowOne == null) {
                    initPopupWindowOne();
                }
                popupWindowOne.showAsDropDown(btnOne);
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示正常
                if (popupWindowTwo == null) {
                    initPopupWindowTwo();
                }
                popupWindowTwo.showAsDropDown(btnTwo);
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示正常
                if (popupWindowThree == null) {
                    initPopupWindowThree();
                }
                int[] location = new int[2];
                btnThree.getLocationInWindow(location);
                popupWindowThree.showAtLocation(btnThree, Gravity.START | Gravity.TOP | Gravity.DISPLAY_CLIP_VERTICAL, location[0], location[1] + btnThree.getHeight());
            }
        });
    }

    /**
     * 此种方式初始化调用 showAsDropDown 在7.0以上会有兼容问题
     * 原因在于 showAsDropDown 方法中调用的 findDropDownPosition 中的 tryFitVertical 和 tryFitHorizontal 两个方法
     * 这两个方法在7.0以下的源码中是没有的,在特定条件下会改变 window 的坐标,可参考源码分析
     */
    private void initPopupWindowOne() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindowOne = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindowOne.setOutsideTouchable(true);
        popupWindowOne.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 55)));
        View viewEmpty = contentView.findViewById(R.id.view_empty);
        viewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindowOne != null && popupWindowOne.isShowing()) {
                    popupWindowOne.dismiss();
                }
            }
        });
    }

    /**
     * 设置了固定的高度
     */
    private void initPopupWindowTwo() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        int[] location = new int[2];
        btnTwo.getLocationInWindow(location);
        int height = getScreenHeight(this) - location[1] - btnTwo.getHeight();
        popupWindowTwo = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        popupWindowTwo.setOutsideTouchable(true);
        popupWindowTwo.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 55)));
        View viewEmpty = contentView.findViewById(R.id.view_empty);
        viewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindowTwo != null && popupWindowTwo.isShowing()) {
                    popupWindowTwo.dismiss();
                }
            }
        });
    }

    /**
     * 使用 showAtLocation 代替 showAsDropDown
     */
    private void initPopupWindowThree() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindowThree = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindowThree.setOutsideTouchable(true);
        popupWindowThree.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 55)));
        View viewEmpty = contentView.findViewById(R.id.view_empty);
        viewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindowThree != null && popupWindowThree.isShowing()) {
                    popupWindowThree.dismiss();
                }
            }
        });
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
