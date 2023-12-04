package net.erchen.codingpuzzlescommon.regexp;

import java.util.OptionalInt;
import java.util.regex.Matcher;

public class RegExpUtil {

    public static OptionalInt find(Matcher matcher, int group) {
        if (!matcher.find()) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(Integer.parseInt(matcher.group(group)));
    }

}
