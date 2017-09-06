package com.weihong.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by wei.hong on 2017/9/4.
 */

public class MonthView extends View {
    private static final String TAG = "MonthView";
    // 列, 从1开始
    private static final int COLUMN = 7;
    // 行, 从1开始
    private static final int ROW = 7;
    private int mHeight = 600;
    private int mWidth = 500;
    private int colorWeek;
    private int colorDay;
    private int colorSelectDay;
    private int colorMoveDay;
    private float sizeTextDay;
    private Paint paintWeek = new Paint();
    private Paint paintDay = new Paint();
    private Paint paintCircle = new Paint();
    private Paint paintMoveCircle = new Paint();
    Rect rect = new Rect();
    // 移动经过的行和列
    int moveColumn = -1;
    int moveRow = -1;
    // 一格的宽高
    int cellWidth;
    int cellHeight;
    // 当前显示的日期
    Calendar calendar;
    // 移动经过的日历
    Calendar moveCalendar;

    // 现在的日期
    Calendar cCalendar;
    String[] weekStr;
    // 某个月1号为周几（指定年月），从周日到周六（1-7）
    int startDay = 1;
    OnSelectDayChangedListener mOnSelectDayChangedListener;
    int position;

    public MonthView(Context context) {
        super(context);
        initResource();
        initPaint();
        initData();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initResource();
        initPaint();
        initData();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResource();
        initPaint();
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initResource();
        initPaint();
        initData();
    }

    private void initResource() {
        Resources res = getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorWeek = res.getColor(R.color.color_week, getContext().getTheme());
            colorDay = res.getColor(R.color.color_day, getContext().getTheme());
            colorSelectDay = res.getColor(R.color.color_select_day, getContext().getTheme());
            colorMoveDay = res.getColor(R.color.color_move_day, getContext().getTheme());
        } else {
            colorWeek = res.getColor(R.color.color_week);
            colorDay = res.getColor(R.color.color_day);
            colorSelectDay = res.getColor(R.color.color_select_day);
            colorMoveDay = res.getColor(R.color.color_move_day);
        }
        sizeTextDay = res.getDimension(R.dimen.day_size);
        weekStr = res.getStringArray(R.array.week);
        mHeight = (int) res.getDimension(R.dimen.month_view_height);
    }

    private void initPaint() {
        paintWeek.setAntiAlias(true);
        paintWeek.setColor(colorWeek);
        paintWeek.setTextSize(sizeTextDay);

        paintDay.setAntiAlias(true);
        paintDay.setTextSize(sizeTextDay);
        paintDay.setColor(colorDay);

        paintCircle.setAntiAlias(true);
        paintCircle.setColor(colorSelectDay);

        paintMoveCircle.setAntiAlias(true);
        paintMoveCircle.setColor(colorMoveDay);
    }

    private void initData() {
        cCalendar = Calendar.getInstance();
        cCalendar.setTime(new Date());
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        startDay = calendar.get(Calendar.DAY_OF_WEEK);
        moveCalendar = CalendarUtils.getMinCalendar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWeek(canvas);
        drawMonth(canvas);
    }

    private void drawWeek(Canvas canvas) {
        for (int j = 1; j <= COLUMN; j++) {
            rect.left = (j - 1) * cellWidth;
            rect.top = 0;
            rect.right = j * cellWidth;
            rect.bottom = cellHeight;
            String week = weekStr[j - 1];
            paintWeek.getTextBounds(week, 0, week.length(), rect);
            float y = 0.5f * cellHeight + rect.height() / 2f - rect.bottom;
            paintWeek.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(week, (j - 0.5f) * cellWidth, y, paintWeek);
        }
    }

    private void drawMonth(Canvas canvas) {
        for (int i = 2; i <= ROW; i++) {
            for (int j = 1; j <= COLUMN; j++) {
                rect.left = (j - 1) * cellWidth;
                rect.top = (i - 1) * cellHeight;
                rect.right = j * cellWidth;
                rect.bottom = i * cellHeight;

                // 在这个月中第几天
                int dayIndex = 0;
                if (i == 2) {
                    if (j >= startDay) {
                        dayIndex = j + (i - 2) * COLUMN - (startDay - 1);
                    }
                } else {
                    dayIndex = j + (i - 2) * COLUMN - (startDay - 1);
                }

                if (dayIndex > 0 && dayIndex <= CalendarUtils.getDaysInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))) {
                    String s = String.valueOf(dayIndex);
                    paintDay.getTextBounds(s, 0, s.length(), rect);
                    float y = (i - 0.5f) * cellHeight + rect.height() / 2f - rect.bottom;
                    paintDay.setTextAlign(Paint.Align.CENTER);

                    if (CalendarUtils.isEquals(CalendarUtils.coordinateToCalendar(calendar, j, i), CalendarUtils.getClickCalendar())) {
                        drawCircle(canvas, paintCircle, CalendarUtils.calendarToCoordinate(calendar));
                    } else if (CalendarUtils.isEquals(CalendarUtils.coordinateToCalendar(calendar, j, i), moveCalendar)) {
                        drawCircle(canvas, paintMoveCircle, CalendarUtils.calendarToCoordinate(calendar));
                    }

                    if (CalendarUtils.isEquals(CalendarUtils.coordinateToCalendar(calendar, j, i), cCalendar)) {
                        if (CalendarUtils.isEquals(CalendarUtils.coordinateToCalendar(calendar, j, i), CalendarUtils.getClickCalendar())) {
                            paintDay.setColor(Color.WHITE);
                        } else {
                            paintDay.setColor(colorSelectDay);
                        }
                    } else {
                        if (CalendarUtils.isEquals(CalendarUtils.coordinateToCalendar(calendar, j, i), CalendarUtils.getClickCalendar())) {
                            paintDay.setColor(Color.WHITE);
                        } else {
                            paintDay.setColor(colorDay);
                        }
                    }

                    canvas.drawText(s, (j - 0.5f) * cellWidth, y, paintDay);
                }
            }
        }
    }

    private void drawCircle(Canvas canvas, Paint paint, int x, int y) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "drawCircle");
        }
        float cx = (x - 0.5f) * cellWidth;
        float cy = (y - 0.5f) * cellHeight;
        float r = Math.min(cellWidth, cellHeight) / 2;
        canvas.drawCircle(cx, cy, r, paint);
    }

    private void drawCircle(Canvas canvas, Paint paint, int[] coordinate) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "drawCircle");
        }
        float cx = (coordinate[0] - 0.5f) * cellWidth;
        float cy = (coordinate[1] - 0.5f) * cellHeight;
        float r = Math.min(cellWidth, cellHeight) / 2;
        canvas.drawCircle(cx, cy, r, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        cellWidth = mWidth / COLUMN;
        cellHeight = mHeight / ROW;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onTouch action:" + action);
        }
        float x = event.getX();
        float y = event.getY();
        int cMoveColumn;
        int cMoveRow;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                moveColumn = -1;
                moveRow = -1;
                moveCalendar = CalendarUtils.getMinCalendar();
                break;
            case MotionEvent.ACTION_MOVE:
                cMoveColumn = (int) (x * COLUMN / mWidth) + 1;
                cMoveRow = (int) (y * ROW / mHeight) + 1;
                if (moveColumn != cMoveColumn || moveRow != cMoveRow) {
                    moveColumn = cMoveColumn;
                    moveRow = cMoveRow;
                    moveCalendar = (Calendar) calendar.clone();
                    moveCalendar.set(Calendar.DAY_OF_WEEK, (int) (x * COLUMN / mWidth) + 1);
                    moveCalendar.set(Calendar.WEEK_OF_MONTH, (int) (y * ROW / mHeight) + 1 - 1);

                    postInvalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                moveColumn = -1;
                moveRow = -1;
                moveCalendar = CalendarUtils.getMinCalendar();
                int dayOfWeek = (int) (x * COLUMN / mWidth) + 1;
                int weekOfMonth = (int) (y * ROW / mHeight) + 1;
                int indexDay = dayOfWeek + (weekOfMonth - 2) * COLUMN - (startDay - 1);
                if (indexDay > 0 && indexDay <= CalendarUtils.getDaysInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))) {
                    calendar.set(Calendar.DAY_OF_WEEK, (int) (x * COLUMN / mWidth) + 1);
                    calendar.set(Calendar.WEEK_OF_MONTH, (int) (y * ROW / mHeight) + 1 - 1);
                    CalendarUtils.setClickCalendar(calendar);
                    int a[] = CalendarUtils.calendarToYMD(calendar);
                    Toast.makeText(getContext(), a[0] + "/" + a[1] + "/" + a[2], Toast.LENGTH_SHORT).show();
                    postInvalidate();
                }
                break;
        }
        return true;
    }

    public void setOnSelectDayChangedListener(OnSelectDayChangedListener listener, int position) {
        mOnSelectDayChangedListener = listener;
        this.position = position;
    }
}
