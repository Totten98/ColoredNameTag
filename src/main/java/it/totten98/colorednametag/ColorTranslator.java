package it.totten98.colorednametag;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {

    private String coloredText;

    private void setTranslatedColor(String m) {

        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(m);

        while (matcher.find()) {
            String color = m.substring(matcher.start(), matcher.end());
            m = m.replace(color, ChatColor.of(color.replaceAll("&", "")) + "");
            matcher = pattern.matcher(m);
        }

        coloredText = ChatColor.translateAlternateColorCodes('&', m);
    }

    public String colorText(String message) {
        setTranslatedColor(message);
        return coloredText;
    }

}
