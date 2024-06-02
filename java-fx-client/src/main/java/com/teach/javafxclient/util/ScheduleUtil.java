package com.teach.javafxclient.util;

import java.util.List;

public class ScheduleUtil {
    public static String getScheduleString(Long schedule){
        StringBuilder scheduleString = new StringBuilder();
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                if ((schedule & 1) ==1){
                    scheduleString.append(convertNumberToWeekday(i)+ " 第" + (2*j-1) + "-" + (2*j) + "节，");
                }
                schedule >>= 1;
            }
        }
        if (!scheduleString.isEmpty()){
            scheduleString.deleteCharAt(scheduleString.length() - 1);
        }
        return scheduleString.toString();
    }

    public static String convertNumberToWeekday(int number) {
        String weekday = "";
        switch (number) {
            case 1:
                weekday = "星期一";
                break;
            case 2:
                weekday = "星期二";
                break;
            case 3:
                weekday = "星期三";
                break;
            case 4:
                weekday = "星期四";
                break;
            case 5:
                weekday = "星期五";
                break;
            case 6:
                weekday = "星期六";
                break;
            case 7:
                weekday = "星期日";
                break;
            default:
                weekday = "UNDEFINED";
                break;
        }
        return weekday;
    }

    public static long checkSchedule (List<Long> scheduleList){
        long a = 0L,result = 0L;
        for (long schedule :
                scheduleList) {
            a |= schedule;
        }
        for (long schedule :
                scheduleList) {
            long ans = solve(a,schedule);
            result|=ans;
            a-=schedule-ans;
        }
        return result;
    }

    private static long solve (long a, long b){
        long ans = 0L;
        for (int i = 0; i<35 ; i++){
            if ((a&1)==0&&(b&1)==1){
                ans += 1L <<i;
            }
            a>>=1;b>>=1;
            if (b==0){
                return ans;
            }
        }
        return ans;
    }

    public static  String checkSchedule (Long scheduleA, Long scheduleB){
        return getScheduleString(scheduleA&scheduleB);
    }
}
