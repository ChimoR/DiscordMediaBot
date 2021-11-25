package util;

import misc.DurkaEventProcessor;
import music.MPEventProcessor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EventProcessor {
    public static EventProcessor instance = null;

    public void processEvent(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith("-p ") || message.startsWith("-play "))
            MPEventProcessor.getInstance().processPlayEvent(event);
        if (message.equalsIgnoreCase("-stop"))
            MPEventProcessor.getInstance().processPauseEvent(event);
        if (message.equalsIgnoreCase("-resume"))
            MPEventProcessor.getInstance().processResumeEvent(event);
        if (message.equalsIgnoreCase("-skip"))
            MPEventProcessor.getInstance().processSkipEvent(event);
        if (message.equalsIgnoreCase("-skipall"))
            MPEventProcessor.getInstance().processSkipAllEvent(event);
        if (message.equalsIgnoreCase("-Durka"))
            DurkaEventProcessor.getInstance().processEvent(event);
        //TODO Рефлексия
    }

    public static EventProcessor getInstance()
    {
        if (instance == null)
            instance = new EventProcessor();
        return instance;
    }
}
