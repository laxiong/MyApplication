package com.laxiong.Calender;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.laxiong.Adapter.CalenderBean;
import com.laxiong.yitouhang.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class CalendarView extends View implements Comparable {
    @Override
    public int compareTo(Object another) {
        CalendarView view = (CalendarView) another;
        return this.month - view.month;
    }

    private static final String TAG = "CalendarView";
    public int month;
    /**
     * 两种模式 （月份和星期）
     */
    public static final int MONTH_STYLE = 0;
    public static final int WEEK_STYLE = 1;

    private static final int TOTAL_COL = 7;
    private static final int TOTAL_ROW = 6;

    private Paint mRedCirclePaint;
    private Paint mBlackCirclePaint;
    private Paint mTextPaint;
    private int mViewWidth;
    private int mViewHight;
    private int mCellSpace;
    private int firstdayweek;
    private int currentMonthDays;
    private Row rows[] = new Row[TOTAL_ROW];
    private static CustomDate mShowDate;//自定义的日期  包括year month day
    private CustomDate clickdate;
    public static int style = MONTH_STYLE;
    private static final int WEEK = 7;
    private CallBack mCallBack;//回调
    private int touchSlop;
    private boolean callBackCellSpace;
    private int[] invest = new int[3], backary = new int[3], both = new int[2];//invest投资记录,backary回款记录

    public interface CallBack {
        void clickDate(CustomDate date, int row, int cell);//回调点击的日期

        void onMesureCellHeight(int cellSpace);//回调cell的高度确定slidingDrawer高度

        void changeDate(CustomDate date);//回调滑动viewPager改变的日期
    }

    private boolean find(int n, int... ary) {
        for (int num : ary) {
            if (num == n) {
                return true;
            }
        }
        return false;
    }

    // 点击的Type
    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, int style, CallBack mCallBack) {
        super(context);
        CalendarView.style = style;
        this.mCallBack = mCallBack;
        init(context);
    }

    public CalendarView(Context context, int style, CallBack mCallBack, CustomDate date) {
        super(context);
        CalendarView.style = style;
        this.mCallBack = mCallBack;
        mShowDate = date;
        init(context);
    }

    public static void resetCustomDate() {
        mShowDate = new CustomDate();
    }

    public void resetCustomDate(CustomDate date) {
        mShowDate = null;
        mShowDate = date;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null)
                rows[i].drawCells(canvas);
        }
    }

    private void init(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedCirclePaint.setStyle(Paint.Style.FILL);
        mRedCirclePaint.setColor(Color.parseColor("#F24949"));
        mBlackCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlackCirclePaint.setStyle(Paint.Style.FILL);
        mBlackCirclePaint.setColor(Color.parseColor("#000000"));
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initDate();

    }

    private void initDate() {
        if (style == MONTH_STYLE) {
            mShowDate = new CustomDate();
        } else if (style == WEEK_STYLE) {
            clickdate = DateUtil.getNextSunday(mShowDate);
        }
        fillDate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHight = h;
        mCellSpace = Math.min(mViewHight / TOTAL_ROW, mViewWidth / TOTAL_COL);
        if (!callBackCellSpace) {
            mCallBack.onMesureCellHeight(mCellSpace);
            callBackCellSpace = true;
        }
        mTextPaint.setTextSize(mCellSpace / 3);
    }

    private Cell mClickCell;
    private float mDownX;
    private float mDownY;

    /*
     *
     *触摸事件为了确定点击的位置日期
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mCellSpace);
                    int row = (int) (mDownY / mCellSpace);
                    measureClickCell(col, row);
                }
                break;
        }
        return true;
    }

    public void performclick(int clickrow, int clickcol) {
        if (clickrow != -1 && clickcol != -1) {
            measureClickCell(clickcol, clickrow);
        }
    }

    public void cancelClick() {
        mClickCell.state = State.CURRENT_MONTH_DAY;
        if (mClickCell != null) {
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
        }
        invalidate();
    }

    private void measureClickCell(int col, int row) {
        if (style == MONTH_STYLE && (col >= TOTAL_COL || row >= TOTAL_ROW || row * 7 + col < firstdayweek || row * 7 + col > firstdayweek + currentMonthDays - 1))
            return;
        else if (style == WEEK_STYLE && (col >= TOTAL_COL || row >= TOTAL_ROW))
            return;
        if (mClickCell != null) {
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
            if (style == WEEK_STYLE) {
                rows[mClickCell.j].cells[mClickCell.i].state = getState(mShowDate.getDay(), State.CURRENT_MONTH_DAY);
            }
        }
        if (rows[row] != null) {
            mClickCell = new Cell(rows[row].cells[col].date,
                    rows[row].cells[col].state, rows[row].cells[col].i,
                    rows[row].cells[col].j);
            rows[row].cells[col].state = State.CLICK_DAY;
            CustomDate date = rows[row].cells[col].date;
            date.week = col;
            mCallBack.clickDate(date, row, col);
            invalidate();
        }
    }

    // 组
    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public Cell[] cells = new Cell[TOTAL_COL];

        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null)
                    cells[i].drawSelf(canvas);
            }

        }
    }

    //  单元格
    class Cell {
        public CustomDate date;
        public State state;
        public int i;
        public int j;

        public Cell(CustomDate date, State state, int i, int j) {
            super();
            this.date = date;
            this.state = state;
            this.i = i;
            this.j = j;
        }

        //  绘制一个单元格 如果颜色需要自定义可以修改
        public void drawSelf(Canvas canvas) {
            switch (state) {
                case CURRENT_MONTH_DAY:
                    mTextPaint.setColor(Color.parseColor("#80000000"));
                    break;
                case NEXT_MONTH_DAY:
                case PAST_MONTH_DAY:
                    mTextPaint.setColor(Color.parseColor("#40000000"));
                    break;
                case CLICK_DAY:
                    mTextPaint.setColor(Color.parseColor("#fffffe"));
                    canvas.drawCircle((float) (mCellSpace * (i + 0.5)),
                            (float) ((j + 0.5) * mCellSpace), mCellSpace / 2,
                            mBlackCirclePaint);
                    break;
                case TODAY:
                    mTextPaint.setColor(Color.parseColor("#fffffe"));
                    canvas.drawCircle((float) (mCellSpace * (i + 0.5)),
                            (float) ((j + 0.5) * mCellSpace), mCellSpace / 2,
                            mRedCirclePaint);
                    break;
                case INVEST_DAY:
                    mTextPaint.setColor(getResources().getColor(R.color.money));
                    break;
                case BACK_DAY:
                    mTextPaint.setColor(Color.parseColor("#0000CD"));
                    break;
                case BOTH_DAY:
                    mTextPaint.setColor(getResources().getColor(R.color.orangered));
                    break;
            }
            // 绘制文字
            String content = date.day + "";
            canvas.drawText(content,
                    (float) ((i + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2),
                    (float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(
                            content, 0, 1) / 2), mTextPaint);
        }
    }

    /**
     * @author huang
     *         cell的state
     *         当前月日期，过去的月的日期，下个月的日期，今天，点击的日期
     */
    enum State {
        CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, TODAY, CLICK_DAY, INVEST_DAY, BACK_DAY, BOTH_DAY;
    }

    /**
     * 填充日期的数据
     */
    private void fillDate() {
        if (style == MONTH_STYLE) {
            fillMonthDate();
        } else if (style == WEEK_STYLE) {
            clickdate = DateUtil.getNextSunday(mShowDate);
            fillWeekDate();
        }
        mCallBack.changeDate(mShowDate);
    }

    /**
     * 填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
     */
    private void fillWeekDate() {
        int lastMonthDays = DateUtil.getMonthDays(clickdate.year, clickdate.month - 1);
        rows[0] = new Row(0);
        int day = clickdate.day;
        for (int i = TOTAL_COL - 1; i >= 0; i--) {
            day -= 1;
            if (day < 1) {
                day = lastMonthDays;
            }
            CustomDate date = CustomDate.modifiDayForObject(clickdate, day);
            Log.i("kk", "date:" + date.getDay() + ",mShow" + mShowDate.getDay() + ",there" + date.toString() + "==" + mShowDate.toString());
            if (DateUtil.isClickDay(date, mShowDate)) {
                Log.i("kk", "date:" + date.getDay() + ",mShow" + mShowDate.getDay() + ",here");
                if (DateUtil.isToday(date)) {
                    rows[0].cells[i] = new Cell(date, State.TODAY, i, 0);
                } else {
                    mClickCell = new Cell(date, State.CLICK_DAY, i, 0);
                    rows[0].cells[i] = new Cell(date, State.CLICK_DAY, i, 0);
                }
                continue;
            }
            rows[0].cells[i] = new Cell(date, getState(day, State.CURRENT_MONTH_DAY), i, 0);
        }
    }

    /**
     * 填充月份模式下数据 通过getWeekDayFromDate得到一个月第一天是星期几就可以算出所有的日期的位置 然后依次填充
     * 这里最好重构一下
     */
    private void fillMonthDate() {
        month = mShowDate.month;
        int monthDay = DateUtil.getCurrentMonthDay();
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
        currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
        firstdayweek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new Row(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int postion = i + j * TOTAL_COL;
                if (postion >= firstdayweek
                        && postion < firstdayweek + currentMonthDays) {
                    day++;
                    if (isCurrentMonth && day == monthDay) {
                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
//                        mClickCell = new Cell(date, State.TODAY, i, j);
                        date.week = i;
                        rows[j].cells[i] = new Cell(date, State.TODAY, i, j);
                        continue;
                    }
                    if (day == monthDay && !isCurrentMonth) {
                        mClickCell = null;
                    }
                    rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
                            getState(day, State.CURRENT_MONTH_DAY), i, j);
                } else if (postion < firstdayweek) {
                    rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year, mShowDate.month - 1, lastMonthDays - (firstdayweek - postion - 1)), State.PAST_MONTH_DAY, i, j);
                } else if (postion >= firstdayweek + currentMonthDays) {
                    rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year, mShowDate.month + 1, postion - firstdayweek - currentMonthDays + 1)), State.NEXT_MONTH_DAY, i, j);
                }
            }
        }
    }

    public State getState(int day, State state) {
        if (find(day, both)) {
            return State.BOTH_DAY;
        } else {
            if (find(day, invest))
                return State.INVEST_DAY;
            else if (find(day, backary))
                return State.BACK_DAY;
            else
                return state;
        }
    }

    public void setStyle(int styles) {
        style = styles;
    }

    public void update() {
        fillDate();
        invalidate();
    }

    public void backToday() {
        initDate();
        invalidate();
    }

    //切换style
    public void switchStyle(int style) {
        CalendarView.style = style;
        if (style == MONTH_STYLE) {
            update();
        } else if (style == WEEK_STYLE) {
            int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                    mShowDate.month);
            int day = 1 + WEEK - firstDayWeek;
            mShowDate.day = day;

            update();
        }

    }

    //向右滑动
    public void rightSilde() {
        if (style == MONTH_STYLE) {
            if (mShowDate.month == 12) {
                mShowDate.month = 1;
                mShowDate.year += 1;
            } else {
                mShowDate.month += 1;
            }

        } else if (style == WEEK_STYLE) {
            int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
            if (mShowDate.day + WEEK > currentMonthDays) {
                if (mShowDate.month == 12) {
                    mShowDate.month = 1;
                    mShowDate.year += 1;
                } else {
                    mShowDate.month += 1;
                }
                mShowDate.day = WEEK - currentMonthDays + mShowDate.day;
            } else {
                mShowDate.day += WEEK;

            }
        }
        Log.i("kk", "我看看" + mShowDate.toString() + "rightside");
        update();
    }

    ///向左滑动
    public void leftSilde() {
        if (style == MONTH_STYLE) {
            if (mShowDate.month == 1) {
                mShowDate.month = 12;
                mShowDate.year -= 1;
            } else {
                mShowDate.month -= 1;
            }

        } else if (style == WEEK_STYLE) {
            int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
            if (mShowDate.day - WEEK < 1) {
                if (mShowDate.month == 1) {
                    mShowDate.month = 12;
                    mShowDate.year -= 1;
                } else {
                    mShowDate.month -= 1;
                }
                mShowDate.day = lastMonthDays - WEEK + mShowDate.day;

            } else {
                mShowDate.day -= WEEK;
            }
            Log.i(TAG, "leftSilde" + mShowDate.toString());
        }
        Log.i("kk", "我看看" + mShowDate.toString() + "leftside");
        update();
    }

    // TODO  上滚动的 减月份
    public void sildeTop() {
        if (style == MONTH_STYLE) {
            if (mShowDate.month == 1) {
                mShowDate.month = 12;
                mShowDate.year -= 1;
            } else {
                mShowDate.year -= 1;
            }
        }
        Log.i("kk", "我看看" + mShowDate.toString() + "slidetop");
        update();
    }

    // TODO 下滚动的  加月份
    public void sildeBottom() {
        if (style == MONTH_STYLE) {
            if (mShowDate.month == 12) {
                mShowDate.month = 1;
                mShowDate.year += 1;
            } else {
                mShowDate.month += 1;
            }
        }
        update();
    }

    // TODO 设置指定的月份  暂时不考虑年份的事
    public void setCalenderMonth(int yue) {
        if (style == MONTH_STYLE) {
            if (yue >= 1 && yue <= 12) {
                mShowDate.month = yue;
            } else {

            }
        }
        update();
    }

}
