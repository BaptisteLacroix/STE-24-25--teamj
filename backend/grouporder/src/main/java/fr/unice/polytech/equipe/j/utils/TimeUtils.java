package fr.unice.polytech.equipe.j.utils;

import java.time.Clock;
import java.time.LocalDateTime;

public class TimeUtils {
    private static Clock clock = Clock.systemUTC();
    public static void setClock(Clock clock) {
        TimeUtils.clock = clock;
    }
    public static LocalDateTime getNow() {
        return LocalDateTime.now(clock);
    }
}
