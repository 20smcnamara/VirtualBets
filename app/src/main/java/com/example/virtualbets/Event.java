package com.example.virtualbets;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class Event {

    private boolean notificationSent = false;

    private final String description;
    private final String name;
    private final String notificationText;

    private final LocalDateTime dateTime;
    private final LocalDateTime notificationTime;
    private final LocalDateTime notificationTimeEnd;

    private final Rect rect;
    private final Rect textRect;
    private final WeakButton info;
    private final WeakButton hide;

    public Event(String Name, String Description, int year, int month, int day, int hour, int minute) {
        description = Description;
        name = Name;

        notificationText = name;

        LocalDate localDate = LocalDate.of(year, month, day);
        LocalTime localTime = LocalTime.of(hour, minute);

        dateTime = LocalDateTime.of(localDate, localTime);
        notificationTime = dateTime.minusDays(1);
        notificationTimeEnd = notificationTime.plusSeconds(10);

        rect = new Rect( (int) (Constants.width * 0.02f), (int) (Constants.height * 0.02f), (int) (Constants.width * 0.98f), (int) (Constants.height * 0.22f) );

        textRect = new Rect( (int) rect.left,  (int) (Constants.height * 0.04f),  (int) rect.right,  (int) (Constants.height * 0.16f) );

        Rect infoButtonRect = new Rect( (int) rect.left,  (int) (Constants.height * 0.18f),  (int) (rect.right - rect.width() / 2),  (int) (Constants.height * 0.20f));
        HashMap<String, Object> args = new HashMap<>();
        args.put("str", "more info");
        args.put("strColor", Constants.SECONDARY_THEME_COLOR);
        info = new WeakButton(infoButtonRect, 0, args);


        Rect hideButtonRect = new Rect( (int) (rect.left + rect.width() / 2),  (int) (Constants.height * 0.18f),  (int) (rect.right),  (int) (Constants.height * 0.20f));
        args = new HashMap<>();
        args.put("str", "hide");
        args.put("strColor", Constants.SECONDARY_THEME_COLOR);
        hide = new WeakButton(hideButtonRect, 1, args);
    }

    public void delayHours(int hours) {
        dateTime.plusHours(hours);
    }

    public void delayDays(int days) {
        dateTime.plusDays(days);
    }

    public void moveTime(int hour, int minute) {
        dateTime.withHour(hour);
        dateTime.withMinute(minute);
    }

    public void moveDay(int year, int month, int day) {
        dateTime.withDayOfYear(year);
        dateTime.withDayOfMonth(month);
        dateTime.withDayOfMonth(day);
    }

    public boolean isNotificationSent() {
        LocalDateTime time = LocalDateTime.now();

        if (!notificationSent && notificationTime.isBefore(time)) {
            if (notificationTimeEnd.isAfter(time)) {
                hideNotification();
            }

            Constants.focusedEvent = this;
        }

        return notificationSent;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String timeTill() {
        LocalDateTime now = LocalDateTime.now();

        String toReturn;

        long dayTo = now.until(dateTime, ChronoUnit.DAYS);

        if (dayTo > 1.5) {
            toReturn = "" + (int) dayTo + " days";
            return toReturn;
        }

        long hoursTo = now.until(dateTime, ChronoUnit.HOURS);

        if (hoursTo > 1){
            toReturn = "" + (int) hoursTo + " hours";
            return toReturn;
        }

        long minutesTo = now.until(dateTime, ChronoUnit.MINUTES);

        toReturn = "" + (int) minutesTo + " minutes";

        return toReturn;
    }

    public int update() {
        boolean before = notificationSent;

        isNotificationSent();

        if (before != notificationSent) {
            return 1;
        }

        return 0;
    }

    public void draw(Canvas canvas) {
        Draw.drawRoundRect(canvas, rect, Constants.PRIMARY_THEME_COLOR);
        info.draw(canvas);
        hide.draw(canvas);

        Draw.drawText(canvas, textRect, Constants.SECONDARY_THEME_COLOR, notificationText);
    }

    public void hideNotification() {
        Constants.focusedEvent = null;
        notificationSent = true;
    }

    public void receiveTouch(MotionEvent event) {
        if (event.getY() > rect.bottom) {
            hideNotification();
            return;
        }

        info.receiveTouch(event);
        hide.receiveTouch(event);
    }
}
