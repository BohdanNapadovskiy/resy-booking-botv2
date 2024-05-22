package org.example;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
public class BookingBot {

    private static final Logger logger = Logger.getLogger(BookingBot.class.getName());

    public static void main(String[] args) {
        ClientConfig _config = new ClientConfig().createConfig();


        int hour = _config.getSnipeTimeHours();
        int minute = _config.getSnipeTimeMinutes();

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
        ReservationDetails details = new ReservationDetails();

//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.schedule(() -> {
            runResyBookingWorkflow(_config, details);
//            logger.info("Shutting down Resy Booking Bot");
//            System.exit(0);
//        }, millisUntilNextSnipe, TimeUnit.MILLISECONDS);
    }


    private static void runResyBookingWorkflow(ClientConfig _config, ReservationDetails details) {
        new ReservationClientImpl(_config).bookReservation();

    }

}