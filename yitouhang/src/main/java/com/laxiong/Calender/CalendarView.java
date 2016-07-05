package com.laxiong.Calender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.laxiong.entity.CalendarMonthTrade;
import com.carfriend.mistCF.R;
import java.util.List;

/**
 * Created by admin on 2016/5/3.
 */
public class CalendarView extends View implements Comparable{

    public int month;
    @Override
    public int compareTo(Object another) {
        CalendarView view = (CalendarView) another;
        return this.month - view.month;
    }

    private static final String TAG = "CalendarView";
    /**
     *  两种模式 （月份和星期）
     */
    public static final int MONTH_STYLE = 0;
    public static final int WEEK_STYLE = 1;

    private static final int TOTAL_COL = 7;
    private static final int TOTAL_ROW = 6;

    private Paint mBlackCirclePaint;
    private Paint mRedCirclePaint;
    private Paint mTextPaint;
    private int mViewWidth;
    private int mViewHight;
    private int mCellSpace;
    private Row rows[] = new Row[TOTAL_ROW];
    private static CustomDate mShowDate;       //自定义的日期  包括year month day
    public static int style = MONTH_STYLE;
    private static final int WEEK = 7;
    private CallBack mCallBack;//回调
    private int touchSlop;
    private boolean callBackCellSpace;
    private int[] invest = new int[3], backary = new int[3], both = new int[2];//invest投资记录,  backary回款记录

    //**********************************************************************
    private List<String> keys ; // date list
    private List<Integer> values ; // 1,2,3  {1.收益的  2.有操作的  3.有股息的}
    //***********************************************************************

    public interface CallBack {

        void clickDate(CustomDate date);//回调点击的日期

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

        values = CalendarMonthTrade.getInstance().getValues();
        keys = CalendarMonthTrade.getInstance().getKeys();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedCirclePaint.setStyle(Paint.Style.FILL);

        mBlackCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlackCirclePaint.setStyle(Paint.Style.FILL);
        mBlackCirclePaint.setColor(Color.parseColor("#000000")); // 黑色的

        mRedCirclePaint.setColor(Color.parseColor("#F24949")); //  红色的
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initDate();

    }
    private void initDate() {
        if (style == MONTH_STYLE) {
            mShowDate = new CustomDate();
        } else if(style == WEEK_STYLE ) {
            mShowDate = DateUtil.getNextSunday();
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
     *  触摸事件为了确定点击的位置日期
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
        if (col >= TOTAL_COL || row >= TOTAL_ROW)
            return;
        if (mClickCell != null) {
            rows[mClickCell.j].cells[mClickCell.i] = mClickCell;
        }
        if (rows[row] != null) {
            mClickCell = new Cell(rows[row].cells[col].date,
                    rows[row].cells[col].state, rows[row].cells[col].i,
                    rows[row].cells[col].j);
            rows[row].cells[col].state = State.CLICK_DAY;
            CustomDate date = rows[row].cells[col].date;
            date.week = col;
            mCallBack.clickDate(date);
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
                    mTextPaint.setColor(Color.parseColor("#80000000"));   //默红色
                    break;
                case NEXT_MONTH_DAY:
                case PAST_MONTH_DAY:
                    mTextPaint.setColor(Color.parseColor("#40000000"));   // 黑色
                    break;
                case TODAY:
                    mTextPaint.setColor(Color.parseColor("#F24949"));  // 红色
                    break;
                case CLICK_DAY:
                    mTextPaint.setColor(Color.parseColor("#fffffe"));  // 白色
                    canvas.drawCircle((float) (mCellSpace * (i + 0.5)),
                            (float) ((j + 0.5) * mCellSpace), mCellSpace / 2,
                            mRedCirclePaint);
                    break;

                case INVEST_DAY:
                    mTextPaint.setColor(getResources().getColor(R.color.money));
                    break;
                case BACK_DAY:
                    mTextPaint.setColor(Color.parseColor("#0000CD")); //蓝色的
                    break;
                case BOTH_DAY:
                    mTextPaint.setColor(getResources().getColor(R.color.orangered));
                    break;
            }
            // 绘制文字
            String content = date.day+"";
            canvas.drawText(content,
                    (float) ((i+0.5) * mCellSpace - mTextPaint.measureText(content)/2),
                    (float) ((j + 0.7) * mCellSpace - mTextPaint.measureText(
                            content, 0, 1) / 2), mTextPaint);
        }
    }
    /**
     *
     *@author huang
     * cell的state
     *当前月日期，过去的月的日期，下个月的日期，今天，点击的日期 ,投资记录的日期 ，回款的日期 ，两者的日期
     *
     */
    enum State {
        CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, TODAY, CLICK_DAY,INVEST_DAY, BACK_DAY, BOTH_DAY;;
    }

    /**
     *  填充日期的数据
     */
    private void fillDate() {
        if (style == MONTH_STYLE) {
            fillMonthDate();
        } else if(style == WEEK_STYLE) {
            fillWeekDate();
        }
        mCallBack.changeDate(mShowDate);
    }

    /**
     *  填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
     */

    private void fillWeekDate() {
        if (mShowDate!=null) {
            Log.i("Calender", "==========周【1】星期模式mShowDate==========：" + mShowDate);
            int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
            rows[0] = new Row(0);
            int day = mShowDate.day;
            for (int i = TOTAL_COL - 1; i >= 0; i--) {
                day -= 1;
                if (day < 1) {
                    day = lastMonthDays;
                }
                CustomDate dateS = CustomDate.modifiDayForObject(mShowDate, day);

                if (DateUtil.isToday(dateS)) {
//                    mClickCell = new Cell(dateS, State.TODAY, i, 0);
//                    dateS.week = i;
                    rows[0].cells[i] =  new Cell(dateS, State.CLICK_DAY, i, 0);
                    continue;
                }
                if (setDateInvestAryColor(dateS)){
                    rows[0].cells[i] = new Cell(dateS,State.INVEST_DAY,i,0);
                }else if (setDateBackAryColor(dateS)){
                    rows[0].cells[i] = new Cell(dateS,State.BACK_DAY,i,0);
                }else if (setDateGxCollectAry(dateS)){
                    rows[0].cells[i] = new Cell(dateS,State.BOTH_DAY,i,0);
                }else {
                    rows[0].cells[i] = new Cell(dateS,State.CURRENT_MONTH_DAY,i,0);
                }
            }
            Log.i("Calender", "==========周【4】星期模式mShowDate.day==========：" + day);
        }
    }

    /**
     * 填充月份模式下数据 通过getWeekDayFromDate得到一个月第一天是星期几就可以算出所有的日期的位置 然后依次填充
     * 这里最好重构一下
     */
    private void fillMonthDate() {
        int monthDay = DateUtil.getCurrentMonthDay();
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {
            isCurrentMonth = true;
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {
            rows[j] = new Row(j);
            for (int i = 0; i < TOTAL_COL; i++) {
                int postion = i + j * TOTAL_COL;
                if (postion >= firstDayWeek
                        && postion < firstDayWeek + currentMonthDays) {
                    day++;
                    if (isCurrentMonth && day == monthDay) {
                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
                        mClickCell = new Cell(date,State.TODAY, i,j);
                        date.week = i;
                        Log.i("Calender","==========月模式CustomDate==========："+date);
                        mCallBack.clickDate(date);
                        rows[j].cells[i] = new Cell(date,State.CLICK_DAY, i,j);
                        continue;
                    }

                    if (day == monthDay && !isCurrentMonth) {
                        mClickCell = null;
                    }
                    //TODO 对月中的日期进行颜色变化
                    CustomDate dateS = CustomDate.modifiDayForObject(mShowDate, day);
                    if (setDateInvestAryColor(dateS)){
                        rows[j].cells[i] = new Cell(dateS,State.INVEST_DAY,i,j);
                    }else if (setDateBackAryColor(dateS)){
                        rows[j].cells[i] = new Cell(dateS,State.BACK_DAY,i,j);
                    }else if (setDateGxCollectAry(dateS)){
                        rows[j].cells[i] = new Cell(dateS,State.BOTH_DAY,i,j);
                    }else {
                        rows[j].cells[i] = new Cell(dateS,State.CURRENT_MONTH_DAY,i,j);
                    }
//                  根据key的值来判断  所得日期的颜色变化设置
//                    String dateStr = String.valueOf(dateS);
//                    String Caldate =spriltDateStr(dateStr);
//                    if (keys!=null&&keys.size()>0) {
////                        Log.i("WK", "======kkkkkkk=====:  日历的日期： " + Caldate + "      获取的日期" + keys.get(0));
//                    }
//                    if (keys!=null&&keys.size()>0) {
//                        for (int z = 0; z < keys.size(); z++) {
//                            if (Caldate.equals(keys.get(z))) {
//                                Log.i("WK", "======kkkkkkk=====: "+"    相等的日历："+Caldate);
//                                if (values.get(z) == 1) {
//                                    rows[j].cells[i] = new Cell(dateS, State.INVEST_DAY, i, j);
//                                } else if (values.get(z) == 2) {
//                                    rows[j].cells[i] = new Cell(dateS, State.BACK_DAY, i, j);
//                                } else if (values.get(z) == 3) {
//                                    rows[j].cells[i] = new Cell(dateS, State.CURRENT_MONTH_DAY, i, j);
//                                }
//                            } else {
//                                rows[j].cells[i] = new Cell(dateS, State.CURRENT_MONTH_DAY, i, j);
//                            }
//                        }
//                    }else {
//                        rows[j].cells[i] = new Cell(dateS, State.CURRENT_MONTH_DAY, i, j);
//                    }

//                    rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
//                            State.CURRENT_MONTH_DAY, i, j);

                } else if (postion < firstDayWeek) {
                    rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year, mShowDate.month-1, lastMonthDays - (firstDayWeek- postion - 1)), State.PAST_MONTH_DAY, i, j);
                } else if (postion >= firstDayWeek + currentMonthDays) {
                    rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year, mShowDate.month+1, postion - firstDayWeek - currentMonthDays + 1)), State.NEXT_MONTH_DAY, i, j);
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

    public void backToday(){
        initDate();
        invalidate();
    }
    ///切换style
    public void switchStyle(int style) {
        CalendarView.style = style;
        if (style == MONTH_STYLE) {
            update();
        } else if (style == WEEK_STYLE) {
            int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                    mShowDate.month);
            int day =  1 + WEEK - firstDayWeek;
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
            }else{
                mShowDate.day += WEEK;

            }
        }
        update();
    }
    //向左滑动
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

            }else{
                mShowDate.day -= WEEK;
            }
            Log.i(TAG, "leftSilde" + mShowDate.toString());
        }
        update();
    }

    private boolean setDateInvestAryColor(CustomDate date){
        String dateStr = String.valueOf(date);
        String Caldate =spriltDateStr(dateStr);
        if (keys!=null&&keys.size()>0) {
            for (int z = 0; z < keys.size(); z++) {
                if (Caldate.equals(keys.get(z))) {
                    if (values.get(z) == 1) {
                        return true;
                    }
                }
            }
        }
        return false ;
    }
    private boolean setDateBackAryColor(CustomDate date){
        String dateStr = String.valueOf(date);
        String Caldate =spriltDateStr(dateStr);
        if (keys!=null&&keys.size()>0) {
            for (int z = 0; z < keys.size(); z++) {
                if (Caldate.equals(keys.get(z))) {
                    if (values.get(z) == 2) {
                        return true;
                    }
                }
            }
        }
        return false ;
    }
    private boolean setDateGxCollectAry(CustomDate date){
        String dateStr = String.valueOf(date);
        String Caldate =spriltDateStr(dateStr);
        if (keys!=null&&keys.size()>0) {
            for (int z = 0; z < keys.size(); z++) {
                if (Caldate.equals(keys.get(z))) {
                    if (values.get(z) == 3) {
                        return true;
                    }
                }
            }
        }
        return false ;
    }
    // 日期截取字段的方法
    private String spriltDateStr(String date){
        //2016-5-24
        String year = date.substring(0, 4);
        String month = date.substring(5, 6);
        String day = date.substring(7, date.length());
        if (month.length()==1&&day.length()==1){
            String dates = year+"-0"+month+"-0"+day;
            return dates;
        }else if (month.length()==1&&day.length()>1){
            String dates = year+"-0"+month+"-"+day;
            return dates;
        }else if (month.length()>1&&day.length()==1){
            String dates = year+month+"-0"+day;
            return dates;
        }else {
            return date;
        }

    }


}
