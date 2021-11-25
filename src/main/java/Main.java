import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import util.EventListener;
import util.PropertiesLoader;

import javax.security.auth.login.LoginException;


public class Main {
    public static void main(String[] args) throws LoginException {
        JDA builder = JDABuilder.createDefault(PropertiesLoader.getBotToken()).build();
        builder.addEventListener(new EventListener());
    }
}