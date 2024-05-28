package org.example;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
    long millisUntilNextSnipe = Duration.between(LocalDateTime.now(), nextSnipeTime).toMillis();

    long millisUntilPreSnipe = millisUntilNextSnipe - TimeUnit.SECONDS.toMillis(_config.getBeforeSnipeTime());

    logger.info("Next snipe time: " + nextSnipeTime);
    logger.info("Sleeping until 30 seconds before snipe time: " + convertMillisToReadableTime(millisUntilPreSnipe));

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // Task to start 30 seconds before the snipe time
    scheduler.schedule(() -> {
      logger.info("Starting pre-snipe task");
      // Schedule the task to run every 10 seconds until the snipe time
      scheduler.scheduleAtFixedRate(() -> {
        logger.info("Running pre-snipe workflow");
        runResyBookingWorkflow(_config);
      }, 0, _config.getRepeatSnipeTime(), TimeUnit.SECONDS);

    }, millisUntilPreSnipe, TimeUnit.MILLISECONDS);

    // Task to run exactly at the snipe time
    scheduler.schedule(() -> {
      logger.info("Running snipe workflow");
      runResyBookingWorkflow(_config);
      logger.info("Shutting down Resy Booking Bot");
      System.exit(0);
    }, millisUntilNextSnipe, TimeUnit.MILLISECONDS);

  }

  private static void runResyBookingWorkflow(ClientConfig _config) {
    new ReservationClientImpl(_config).bookReservations();

  }

  public static String convertMillisToReadableTime(long millis) {
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
    long milliseconds = millis % 1000;

    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
  }

}