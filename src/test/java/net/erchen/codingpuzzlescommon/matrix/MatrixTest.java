package net.erchen.codingpuzzlescommon.matrix;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class MatrixTest {

    @Test
    void shouldCreateFromInit() {

        var matrix = Matrix.fromInitValue(3, () -> "x");

        assertThat(matrix.toString()).isEqualTo("""
                x x x
                x x x
                x x x""");
    }

    @Test
    void shouldParseInput() {
        var bingoDeck = Matrix.fromInput("""
                22 13 17 11  0
                 8  2 23  4 24
                21  9 14 16  7
                 6 10  3 18  5
                 1 12 20 15 19
                """, "\n", " +", Integer::parseInt);

        assertThat(bingoDeck.toString()).isEqualTo("""
                22 13 17 11 0
                8 2 23 4 24
                21 9 14 16 7
                6 10 3 18 5
                1 12 20 15 19""");
    }

    @Test
    void shouldReturnFieldValue() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.fieldValue(0, 0)).isEqualTo(1);
        assertThat(matrix.fieldValue(1, 1)).isEqualTo(5);
        assertThat(matrix.fieldValue(1, 2)).isEqualTo(8);
    }

    @Test
    void shouldSetFieldValue() {

        var matrix = Matrix.fromInitValue(3, () -> 5);

        assertThat(matrix.fieldValue(1, 1)).isEqualTo(5);
        matrix.setFieldValue(1, 1, 99);
        assertThat(matrix.fieldValue(1, 1)).isEqualTo(99);
    }

    @Test
    void shouldSwapFields() {
        AtomicInteger init = new AtomicInteger(0);
        var matrix = Matrix.fromInitValue(3, init::incrementAndGet);
        matrix.swapValues(matrix.field(0, 0), matrix.field(2, 2));

        assertThat(matrix.toString()).isEqualTo("""
                9 2 3
                4 5 6
                7 8 1""");
    }

    @Test
    void shouldAddBorder() {

        var matrix = Matrix.fromInitValue(2, () -> "x");

        matrix.addBorder(1, () -> "0");

        assertThat(matrix.toString()).isEqualTo("""
                0 0 0 0
                0 x x 0
                0 x x 0
                0 0 0 0""");
    }

    @Test
    void shouldAddThickBorder() {

        var matrix = Matrix.fromInitValue(2, () -> "x");

        matrix.addBorder(2, () -> "0");

        assertThat(matrix.toString()).isEqualTo("""
                0 0 0 0 0 0
                0 0 0 0 0 0
                0 0 x x 0 0
                0 0 x x 0 0
                0 0 0 0 0 0
                0 0 0 0 0 0""");
    }

    @Test
    void shouldReturnFieldsWithAdjacents() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.field(0, 0).getAdjacents()).extracting(Matrix.Field::getValue).containsExactly(2, 4);
        assertThat(matrix.field(1, 1).getAdjacents()).extracting(Matrix.Field::getValue).containsExactly(2, 6, 8, 4);
        assertThat(matrix.field(1, 2).getAdjacents()).extracting(Matrix.Field::getValue).containsExactly(5, 9, 7);
    }

    @Test
    void shouldReturnFieldsWithAdjacentsWithDiagonals() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.field(0, 0).getAdjacentsWithDiagonals()).extracting(Matrix.Field::getValue).containsExactly(2, 5, 4);
        assertThat(matrix.field(1, 1).getAdjacentsWithDiagonals()).extracting(Matrix.Field::getValue).containsExactly(2, 3, 6, 9, 8, 7, 4, 1);
        assertThat(matrix.field(1, 2).getAdjacentsWithDiagonals()).extracting(Matrix.Field::getValue).containsExactly(5, 6, 9, 7, 4);
    }

    @Test
    void shouldReturnDirectAdjacents() {
        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.field(1, 1).top()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(2));
        assertThat(matrix.field(1, 1).topRight()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(3));
        assertThat(matrix.field(1, 1).right()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(6));
        assertThat(matrix.field(1, 1).bottomRight()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(9));
        assertThat(matrix.field(1, 1).bottom()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(8));
        assertThat(matrix.field(1, 1).bottomLeft()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(7));
        assertThat(matrix.field(1, 1).left()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(4));
        assertThat(matrix.field(1, 1).topLeft()).hasValueSatisfying(adjacent -> assertThat(adjacent.getValue()).isEqualTo(1));
    }

    @Test
    void shouldReturnAdjacentsWithOverflow() {
        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.field(2, 1).rightWithOverflow()).satisfies(adjacent -> assertThat(adjacent.getValue()).isEqualTo(4));
        assertThat(matrix.field(1, 2).bottomWithOverflow()).satisfies(adjacent -> assertThat(adjacent.getValue()).isEqualTo(2));
    }

    @Test
    void shouldReturnFieldsAdjacents() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.field(1, 1).left()).hasValueSatisfying(adjacent -> {
            assertThat(adjacent.getX()).isEqualTo(0);
            assertThat(adjacent.getY()).isEqualTo(1);
            assertThat(adjacent.getValue()).isEqualTo(4);
        });
        assertThat(matrix.field(1, 1).right()).hasValueSatisfying(adjacent -> {
            assertThat(adjacent.getX()).isEqualTo(2);
            assertThat(adjacent.getY()).isEqualTo(1);
            assertThat(adjacent.getValue()).isEqualTo(6);
        });
        assertThat(matrix.field(1, 1).top()).hasValueSatisfying(adjacent -> {
            assertThat(adjacent.getX()).isEqualTo(1);
            assertThat(adjacent.getY()).isEqualTo(0);
            assertThat(adjacent.getValue()).isEqualTo(2);
        });
        assertThat(matrix.field(1, 1).bottom()).hasValueSatisfying(adjacent -> {
            assertThat(adjacent.getX()).isEqualTo(1);
            assertThat(adjacent.getY()).isEqualTo(2);
            assertThat(adjacent.getValue()).isEqualTo(8);
        });
    }

    @Test
    void shouldReturnAllFields() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.allFieldValues()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    void shouldReturnAllFieldsWithAdjacents() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.allFields()).hasSize(9);
    }

    @Test
    void shouldReturnAllColumn() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.columns()).extracting(s -> s.collect(toList())).containsExactly(List.of(1, 4, 7), List.of(2, 5, 8), List.of(3, 6, 9));
    }

    @Test
    void shouldReturnAllRows() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.rows()).extracting(s -> s.collect(toList())).containsExactly(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
    }

    @Test
    void shouldReturnRow() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.row(0)).containsExactly(1, 2, 3);
    }

    @Test
    void shouldReturnColumn() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.column(0)).containsExactly(1, 4, 7);
    }

    @Test
    void shouldReturnRowsAndColumns() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.rowsAndColumns()).extracting(s -> s.collect(toList()))
                .containsExactly(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9), List.of(1, 4, 7), List.of(2, 5, 8), List.of(3, 6, 9));
    }

    @Test
    void shouldPrintFields() {

        var matrix = Matrix.builder().fields(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9))).build();

        assertThat(matrix.toString()).isEqualTo("""
                1 2 3
                4 5 6
                7 8 9""");
    }

}