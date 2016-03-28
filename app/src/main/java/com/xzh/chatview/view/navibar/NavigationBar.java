package com.xzh.chatview.view.navibar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;


import com.xzh.chatview.R;
import com.xzh.chatview.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NavigationBar extends LinearLayout implements View.OnClickListener {
    private List<TabEntity> tabs = new ArrayList<>();
    private int currentItem = 0;

    public NavigationBar(Context context) {
        super(context);
        init();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.red_white_rectangle_shape);
        setPadding(1,1,1,1);
    }

    public void addTabView(TabEntity tabEntity) {
        tabs.add(tabEntity);
        applyForeground();
    }

    public void addTabViews(List<TabEntity> tabs) {
        this.tabs.addAll(tabs);
        applyForeground();
    }

    private void applyForeground() {
        removeAllViews();
        for (int i = 0; i < tabs.size(); i++) {
            TabEntity tabEntity = tabs.get(i);
            RadioButton tabView = (RadioButton) inflate(getContext(), R.layout.navigation_item_tab_layout, null);
            tabView.setText(tabEntity.tabName);
            tabView.setOnClickListener(this);
            tabView.setTag(tabEntity.type);
            addView(tabView);
            if (i == 0) {
                tabView.setBackgroundResource(R.drawable.left_round_bg);
                insertSeparator();
            } else if (i == tabs.size() - 1) {
                tabView.setBackgroundResource(R.drawable.right_round_bg);
            } else {
                tabView.setBackgroundResource(R.drawable.mid_rectangle_bg);
                insertSeparator();
            }
        }
        setCurrentItem(currentItem);
    }

    private void insertSeparator() {
        View lineView = new View(getContext());
        lineView.setBackgroundColor(getResources().getColor(R.color.c9));
        lineView.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
        addView(lineView);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            checkView(v);
            if(onClickTabListener != null){
                onClickTabListener.onClickTab(Utils.toInteger(v.getTag()));
            }
        }
    }

    private void checkView(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView instanceof RadioButton) {
                ((RadioButton) childView).setChecked(childView == v);
            }
        }
    }

    public void reset(){
        tabs.clear();
        removeAllViews();
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
        View view = getChildAt(currentItem);
        checkView(view);
        view.performClick();
    }

  public int getCurrentId(){
    return currentItem;
 }

    private OnClickTabListener onClickTabListener = null;

    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }

    public interface OnClickTabListener {
        void onClickTab(int type);
    }
}
