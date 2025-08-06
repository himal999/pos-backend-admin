/**
 * User: Himal_J
 * Date: 2/4/2025
 * Time: 5:41 PM
 * <p>
 */

package com.dtech.admin.util;

import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Log4j2
public class DateTimeUtil {

    public static Date getCurrentDateTime() {
        log.info("get Current DateTime");
        Instant instant = Instant.now();
        return Date.from(instant);
    }

    public static int getCurrentYear() {
        log.info("get Current Year");
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getYear();
    }

    public static int getCurrentMonth() {
        log.info("get Current month");
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.getMonthValue();
    }

    public static int getYear(Date date) {
        log.info("get  Year");
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    public static int getMonth(Date date) {
        log.info("get  month");
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    public static Date get30FutureDate() {
        log.info("get 30 future DateTime");
        LocalDateTime futureDate = LocalDateTime.now().plusDays(28);
        return Date.from(futureDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date get1HourFutureDate() {
        log.info("Get 1 hour future DateTime");
        LocalDateTime futureDate = LocalDateTime.now().plusHours(1);
        return Date.from(futureDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getSeconds(Date date, Integer amount) {
        log.info("get 60s DateTime");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, amount);
        return calendar.getTime();
    }
    public static Date getMinuesDate(int dayCount) {
        log.info("get minus date future DateTime {}", dayCount);
        LocalDateTime minuseDays = LocalDateTime.now().minusDays(dayCount);
        return Date.from(minuseDays.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static String getYyyyMMddHHMmSsTimeFormatter(Date date) {
        log.info("get time formatter");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static long getMinutes(String time) {
        log.info("get minutes");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime givenDate = LocalDateTime.parse(time, formatter);
        Duration duration = Duration.between(LocalDateTime.now(), givenDate);
        return duration.toMinutes();
    }

    public static int getAge(String date) {
        log.info("get age");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate givenDate = LocalDate.parse(date, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(givenDate, currentDate).getYears();
    }

    public static Date getStartOfDay(String dateStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.parse(dateStr);
    }

    public static Date getEndOfDay(String dateStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = sdf.parse(dateStr);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

}
