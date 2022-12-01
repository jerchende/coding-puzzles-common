package net.erchen.codingpuzzlescommon.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeparatorParserTest {

    @Test
    void shouldParseMultiLine() {

        var parsed = SeparatorParser.parseInput("""
                a
                b
                                
                c
                d               
                """, "\n\n", s -> s.split("\n"));

        assertThat(parsed).containsExactly(new String[]{"a", "b"}, new String[]{"c", "d"});
    }
}