package com.weihong.calendar;

import android.icu.util.Calendar;

/**
 * Created by wei.hong on 2017/9/6.
 */

public interface OnSelectDayChangedListener {
    public void selectDayChanged(Calendar calendar, int position);
}
