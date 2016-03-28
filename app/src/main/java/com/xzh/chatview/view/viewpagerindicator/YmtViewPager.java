package com.xzh.chatview.view.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
public class YmtViewPager extends ViewPager {
    private int downX;
    private int downY;

    public YmtViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YmtViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int temp = child.getMeasuredHeight();
            height = Math.max(temp, height);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if ((((int) Math.abs(ev.getX() - downX)) < 5)
                        && (((int) Math.abs(ev.getY() - downY)) < 5)) {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }
}
