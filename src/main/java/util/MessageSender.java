package util;

import net.dv8tion.jda.api.entities.MessageChannel;


public class MessageSender
{
    public static final String LISTENING_TO = "Слушаем: ";
    public static final String ADDED_TO_QUEUE = "Добавлено в очередь: ";
    public static final String NOTHING_TO_RESUME = "Нечего резюмить";
    public static final String NOTHING_TO_STOP = "Нечего стопать";
    public static final String NOTHING_TO_SKIP = "Нечего скипать";
    public static final String TRACK_NOT_FOUND = "Трак не найден";
    public static final String TRACK_LOADING_ERROR = "Что-то пошло не так при загрузке трака";
    public static final String ADDED_TO_QUEUE_PLAYLIST = "Плейлист добавлен в очередь: ";

    public static void sendMessage(MessageChannel channel, String message)
    {
        channel.sendMessage(message).queue();
    }
}
