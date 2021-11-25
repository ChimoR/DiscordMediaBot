package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader
{
    private final static String PROPS_FILE = "src/main/resources/bot.properties";

    public static String getBotToken()
    {
        String token = null;

        try (InputStream is = new FileInputStream(PROPS_FILE))
        {
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Properties pr = new Properties();
            pr.load(reader);

            token = pr.getProperty("botToken");
        }
        catch (IOException e)
        {
            System.out.println("Problem with bot properties");
        }

        return token;
    }
}
