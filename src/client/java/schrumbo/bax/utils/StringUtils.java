package schrumbo.bax.utils;

public class StringUtils {

    public static String noColorCodes(String text) {
        if (text == null) return "";
        return text.replaceAll("§[0-9a-fk-or]", "");
    }

    public static String removePrefix(String text, String prefix) {
        if (text.contains(prefix)) {
            return text.substring(text.indexOf(prefix) + prefix.length()).trim();
        }
        return text;
    }
}
