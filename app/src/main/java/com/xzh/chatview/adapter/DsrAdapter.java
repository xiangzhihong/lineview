package com.xzh.chatview.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.xzh.chatview.R;
import com.xzh.chatview.view.graph.Line;
import com.xzh.chatview.view.graph.LineGraph;
import com.xzh.chatview.view.graph.LinePoint;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DsrAdapter extends PagerAdapter {

    private Context mContext = null;
    private int mChildCount = 0;
    private int rangeType = 30,dataType = 1;

    public DsrAdapter(Context context) {
        super();
        this.mContext = context;

    }

    public void setRangeType(int rangeType) {
        this.rangeType = rangeType;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
    //写死，设计的效果
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_dsr_pager_item, null);
        ViewHolder viewHolder = null;
        if (null == viewHolder) {
            viewHolder = new ViewHolder(itemView);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        setTabName(viewHolder, position);
        initGraphView(viewHolder);
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return itemView;
    }

    private void initGraphView(ViewHolder viewHolder) {
        Line line = new Line();
        for (int i = 0; i <= 5; i++) {
            line.addPoint(new LinePoint("11/0"+i, (int) (Math.random()*5)));
        }
        viewHolder.dsrTrendLine.setTipPrefix("DSR");
        viewHolder.dsrTrendLine.setLine(line);
    }

    private void setTabName( ViewHolder viewHolder,int position) {
        switch (position) {
            case 0:
                viewHolder. dsrTrendName.setText("综合评分(DSR)");
                break;
            case 1:
                viewHolder.dsrTrendName.setText("物流服务(Delivery)");
                break;
            case 2:
                viewHolder.dsrTrendName.setText("交易服务(Service)");
                break;
            case 3:
                viewHolder.dsrTrendName.setText("买家评价(Rating)");
                break;
        }
    }

    public class ViewHolder {

        @InjectView(R.id.dsr_trend_line)
        LineGraph dsrTrendLine;
        @InjectView(R.id.dsr_trend_name)
        TextView dsrTrendName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);
        }

        public void reset() {

        }
    }


}
