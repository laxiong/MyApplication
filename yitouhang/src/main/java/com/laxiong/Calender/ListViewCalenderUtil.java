package com.laxiong.Calender;

import java.util.ArrayList;


public class ListViewCalenderUtil {
    /***
     * 获取12月的月份
     */
    private static ListViewCalenderUtil instance;

    private ListViewCalenderUtil() {
    }

    public static synchronized ListViewCalenderUtil getInstance() {
        if (instance == null) {
            instance = new ListViewCalenderUtil();
        }
        return instance;
    }


    private ArrayList<CalendarViewVer> allMonth;   //排序好的12个月

    public ArrayList<CalendarViewVer> allMonthCalenderView(CustomDate date, CalendarViewVer[] calendarViews) {
        allMonth = new ArrayList<CalendarViewVer>();
//        int currentMonth = date.getMonth();
//        Log.i("Calender", "====日历的当月====：" + currentMonth);
//        //   当月以下的
//        if (currentMonth >= 1) {
//            for (int j = currentMonth; j > 1; j--) {
//                calendarViews[currentMonth - j].leftSilde();
//                allMonth.add(calendarViews[currentMonth - j]);
//                Log.i("Calender", "====日历的第====：" + (currentMonth - j) + "月" + ";的对象==" + calendarViews[currentMonth - j]);
//            }
//        }
//        Collections.sort(allMonth);
//        allMonth.add(calendarViews[currentMonth - 1]);
//        Log.i("Calender", "====日历的第====：" + (currentMonth - 1) + "月" + ";的对象==" + calendarViews[currentMonth - 1]);
//        CalendarView.resetCustomDate();
//        // 当月 以上的
//        if (currentMonth < 12) {
//            for (int j = currentMonth; j < 12; j++) {
//                calendarViews[j].rightSilde();//从当前月的下个月开始
//                allMonth.add(calendarViews[j]);
//            }
//        }
//        Log.i("Calender", "====新日历的个数====：" + allMonth.size());
        for(int i=0;i<12;i++){
            allMonth.add(calendarViews[i]);
        }
        return allMonth;
    }

}
