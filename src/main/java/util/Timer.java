package util;

import music.MPEventProcessor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer
{
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduledFuture;
    public static final String MP_TIMER = "MP_TIMER";
    private static boolean isMpTimerNeedToStart = false;
    private static final Runnable task = MPEventProcessor::disconnectFromChannel;

    public static void startTimer(String timerType)
    {
        if (timerType.equals(MP_TIMER))
        {
            isMpTimerNeedToStart = true;
            processAction(MP_TIMER);
        }
    }

    public static void stopTimer(String timerType)
    {
        if (timerType.equals(MP_TIMER))
        {
            isMpTimerNeedToStart = false;
            processAction(MP_TIMER);
        }
    }

    public static void processAction(String timerType)
    {
        if (timerType.equals(MP_TIMER))
        {
            if (isMpTimerNeedToStart)
                scheduledFuture = executor.schedule(task, 5, TimeUnit.SECONDS);
            else
                scheduledFuture.cancel(false);
        }
    }
}
