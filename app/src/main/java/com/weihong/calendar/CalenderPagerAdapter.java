package com.weihong.calendar;

import android.content.Context;
import android.icu.util.Calendar;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.weihong.calendar.CalendarUtils.MONTH_COUNT;
import static com.weihong.calendar.CalendarUtils.SHOW_YEAR;
import static com.weihong.calendar.CalendarUtils.START_YEAR;


/**
 * Created by wei.hong on 2017/9/5.
 */
public class CalenderPagerAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    SparseArray<ViewHolder> mItem = new SparseArray<>();

    Context mContext;
    OnSelectDayChangedListener mOnSelectDayChangedListener;

    public CalenderPagerAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return SHOW_YEAR * MONTH_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.month_page, container, false);
        TextView txtYear = (TextView) itemView.findViewById(R.id.yearTxt);
        MonthView monthView = (MonthView) itemView.findViewById(R.id.monthView);
        monthView.setOnSelectDayChangedListener(mOnSelectDayChangedListener, position);
        int year = position / MONTH_COUNT + START_YEAR;
        int month = position % MONTH_COUNT;
        txtYear.setText(String.format(mContext.getString(R.string.month_title), year, month + 1));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        monthView.setCalendar(calendar);
        ViewHolder holder = new ViewHolder(position, itemView, monthView);
        container.addView(itemView);
        mItem.put(position, holder);
//        monthViews.add(monthView);
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewHolder holder = (ViewHolder) object;
        container.removeView(holder.itemView);
        mItem.remove(position);
//        monthViews.remove(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return ((ViewHolder) object).position;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ViewHolder) object).itemView;
    }

    public void updateView(int position) {
//        mItem.get(position).monthView.clearClick();
//        mItem.get(position).monthView.postInvalidate();

//        monthViews.get(position).clearClick();
//        monthViews.get(position).invalidate();
    }


    public void setOnSelectDayChangedListener(OnSelectDayChangedListener listener) {
        mOnSelectDayChangedListener = listener;
    }

    class ViewHolder {
        public int position;
        public View itemView;
        public MonthView monthView;

        public ViewHolder(int position, View itemView, MonthView monthView) {
            this.position = position;
            this.itemView = itemView;
            this.monthView = monthView;
        }
    }
}

