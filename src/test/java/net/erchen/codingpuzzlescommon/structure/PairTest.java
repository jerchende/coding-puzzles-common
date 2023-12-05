package net.erchen.codingpuzzlescommon.structure;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PairTest {

    @Test
    void createIntPair() {
        var integerPair = new Pair<>(1, 2);

        assertThat(integerPair.left()).isEqualTo(1);
        assertThat(integerPair.right()).isEqualTo(2);
    }

    @Test
    void createStringPair() {
        var integerPair = new Pair<>("left", "right");

        assertThat(integerPair.left()).isEqualTo("left");
        assertThat(integerPair.right()).isEqualTo("right");
    }
}