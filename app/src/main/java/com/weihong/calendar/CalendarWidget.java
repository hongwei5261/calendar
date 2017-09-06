package com.weihong.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Date;

import static com.weihong.calendar.CalendarUtils.START_YEAR;

/**
 * Created by wei.hong on 2017/9/5.
 */

public class CalendarWidget extends FrameLayout implements OnSelectDayChangedListener {
    //    // 此日历从1900年1月1日开始计算
//    public static final int START_YEAR = 1900;
//    // 此日历显示多少年
//    public static int SHOW_YEAR = 200;
//    // 一年有多少个月份
//    public static int MONTH_COUNT = 12;
    float height;
    CalenderPagerAdapter adapter;

    public CalendarWidget(Context context) {
        super(context);
        initResource();
    }

    public CalendarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResource();
    }

    private void initResource() {
        Resources res = getResources();
        height = res.getDimension(R.dimen.month_view_height) +
                res.getDimension(R.dimen.month_view_title_height);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.calendar_page, this);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        adapter = new CalenderPagerAdapter(getContext());
        viewPager.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        viewPager.setCurrentItem(getPosition(calendar));
//        adapter.setOnSelectDayChangedListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) height);
    }

    public int getPosition(Calendar calendar) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        return (y - START_YEAR) * 12 + m - 1;
    }

    @Override
    public void selectDayChanged(Calendar calendar, int position) {
        int lastPosition = getPosition(lastClickCalendar);
        if (position != lastPosition) {
            adapter.updateView(lastPosition);
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            lastClickCalendar.set(y, m, d);
        }
    }

    Calendar lastClickCalendar = Calendar.getInstance();
}