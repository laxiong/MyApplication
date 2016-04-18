package com.laxiong.Calender;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
/**
 *  自定义的日期类
 * @author huang
 *
 */
public class  CustomDate implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	public int year;
	public int month;
	public int day;
	public int week;
	
	public CustomDate(int year,int month,int day){
		if(month > 12){
			month = 1;
			year++;
		}else if(month <1){
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public CustomDate(){
		this.year = DateUtil.getYear();
		this.month = DateUtil.getMonth();
		this.day = DateUtil.getCurrentMonthDay();
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof CustomDate){
			CustomDate other=(CustomDate)o;
			if(other.getMonth()==this.getMonth()&&other.getYear()==this.getYear()&&other.getDay()==this.getDay())
				return true;
			return false;
		}
		return false;
	}
	public boolean monthequal(Object o){
		if(o instanceof CustomDate){
			CustomDate other=(CustomDate)o;
			if(other.getMonth()==this.getMonth()&&other.getYear()==this.getYear())
				return true;
			return false;
		}
		return false;
	}
	public static CustomDate modifiDayForObject(CustomDate date,int day){
		CustomDate modifiDate = new CustomDate(date.year,date.month,day);
		return modifiDate;
	}
	@Override
	public String toString() {
		return year+"-"+month+"-"+day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

}