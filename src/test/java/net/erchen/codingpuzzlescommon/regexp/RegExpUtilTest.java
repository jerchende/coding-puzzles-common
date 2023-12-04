package net.erchen.codingpuzzlescommon.regexp;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class RegExpUtilTest {

    @Test
    void shouldReturnInt() {
        var matcher = Pattern.compile("ab([0-9]+)cd").matcher("ab123cd");

        var optionalInt = RegExpUtil.find(matcher, 1);

        assertThat(optionalInt).hasValue(123);
    }

    @Test
    void shouldReturnOptionalEmpty() {
        var matcher = Pattern.compile("ab([0-9]+)cd").matcher("xyz");

        var optionalInt = RegExpUtil.find(matcher, 1);

        assertThat(optionalInt).isEmpty();
    }
}