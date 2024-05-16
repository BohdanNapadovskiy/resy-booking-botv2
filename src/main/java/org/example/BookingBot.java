package org.example;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
public class BookingBot {

    private static final Logger logger = Logger.getLogger(BookingBot.class.getName());

    public static void main(String[] args) {

        int hour = 21; // For example, snipeTime.hours
        int minute = 0; // For example, snipeTime.minutes

        LocalDateTime dateTimeNow = LocalDateTime.now();
        LocalDateTime todaysSnipeTime = dateTimeNow
                .withHour(hour)
                .withMinute(minute)
                .withSecond(0)
                .withNano(0);

        LocalDateTime nextSnipeTime = todaysSnipeTime.isAfter(dateTimeNow) ? todaysSnipeTime : todaysSnipeTime.plusDays(1);
        long millisUntilNextSnipe = Duration.between(LocalDateTime.now(), nextSnipeTime).minusSeconds(2).toMillis();

        long hoursRemaining = TimeUnit.MILLISECONDS.toHours(millisUntilNextSnipe);
        long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(millisUntilNextSnipe) - TimeUnit.HOURS.toMinutes(hoursRemaining);
        long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilNextSnipe) - TimeUnit.HOURS.toSeconds(hoursRemaining) - TimeUnit.MINUTES.toSeconds(minutesRemaining);

        logger.info("Next snipe time: " + nextSnipeTime);
        logger.info("Sleeping for " + hoursRemaining + " hours, " + minutesRemaining + " minutes, and " + secondsRemaining + " seconds");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            runResyBookingWorkflow();
            logger.info("Shutting down Resy Booking Bot");
            System.exit(0);
        }, millisUntilNextSnipe, TimeUnit.MILLISECONDS);
    }

    private static void runResyBookingWorkflow() {

    }
}