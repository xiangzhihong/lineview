package com.xzh.chatview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzh.chatview.adapter.DsrAdapter;
import com.xzh.chatview.view.navibar.NavigationBar;
import com.xzh.chatview.view.navibar.TabEntity;
import com.xzh.chatview.view.viewpagerindicator.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.dsr_trend_view)
    LinearLayout dsrTrendView;//dsr趋势图
    @InjectView(R.id.dsr_explain)
    ImageView dsrExplain;//dsr描述
    @InjectView(R.id.dsr_score)
    TextView dsrScore;//dsr综合评分
    @InjectView(R.id.dsr_sort_des)
    TextView dsrSortDes;//综合说明
    @InjectView(R.id.delivery_score)
    TextView deliveryScore;//物流评分
    @InjectView(R.id.delivery_sort_des)
    TextView deliverySortDes;//物流说明
    @InjectView(R.id.service_score)
    TextView serviceScore;//服务评分
    @InjectView(R.id.service_sort_des)
    TextView serviceSortDes;//物流说明
    @InjectView(R.id.rating_score)
    TextView ratingScore;//买家评分
    @InjectView(R.id.rating_sort_des)
    TextView ratingSortDes;//买家说明
    @InjectView(R.id.dsr_navigationbar)
    NavigationBar dsrNavigationbar;

    private ViewPagerIndicator vpi;
    private DsrAdapter dsrAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        initTabbar();
        initClick(dsrNavigationbar);
        initViewPagerIndicator();
    }

    private void initViewPagerIndicator() {
        vpi = new ViewPagerIndicator(this);
//        vpi.getViewPager().setOffscreenPageLimit(1);
        vpi.setVisibility(View.VISIBLE);
        dsrTrendView.addView(vpi);
        dsrAdapter = new DsrAdapter(this);
        vpi.setAdapter(dsrAdapter);

    }

    public void initTabbar() {
        List<TabEntity> tabs = new ArrayList<>();
        tabs.add(new TabEntity(0, "最近一个月"));
        tabs.add(new TabEntity(1, "最近三个月"));
        tabs.add(new TabEntity(2, "最近半年"));
        tabs.add(new TabEntity(3, "最近一年"));
        dsrNavigationbar.addTabViews(tabs);
    }

    private void initClick(NavigationBar dsrTabbar) {
        dsrTabbar.setOnClickTabListener(new NavigationBar.OnClickTabListener() {
            @Override
            public void onClickTab(int type) {
                int rangeTyp=30;
                if (type == 0) {
                    rangeTyp = 30;
                } else if (type == 1) {
                    rangeTyp = 90;
                } else if (type == 2) {
                    rangeTyp = 180;
                } else {
                    rangeTyp = 360;
                }
                dsrAdapter.setRangeType(rangeTyp);
            }
        });
    }
}
