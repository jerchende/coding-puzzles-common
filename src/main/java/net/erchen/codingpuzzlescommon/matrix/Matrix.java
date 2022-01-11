package net.erchen.codingpuzzlescommon.matrix;

import lombok.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@Builder(access = AccessLevel.PACKAGE)
public class Matrix<T> {

    private final List<List<T>> fields;

    private static <T> Matrix<T> fromStreams(Stream<Stream<T>> fields) {
        return Matrix.<T>builder()
                .fields(new LinkedList<>(fields.map(s -> new LinkedList<>(s.toList())).toList()))
                .build();
    }

    public static <T> Matrix<T> fromInitValue(int dimension, Supplier<T> initValue) {
        return fromStreams(Stream.generate(() -> Stream.generate(initValue).limit(dimension)).limit(dimension));
    }

    public static <T> Matrix<T> fromInput(String input, String horizontalSplitter, String vertialSplitter,
            Function<? super String, T> objectCreator) {
        return fromStreams(Arrays.stream(input.split(horizontalSplitter))
                .map(String::trim)
                .map(line -> Arrays.stream(line.split(vertialSplitter)).map(objectCreator)));
    }

    public T fieldValue(int x, int y) {
        return fields.get(y).get(x);
    }

    public void setFieldValue(int x, int y, T value) {
        fields.get(y).set(x, value);
    }

    public void swapValues(Field a, Field b) {
        var valueA = a.getValue();
        setFieldValue(a.getX(), a.getY(), b.getValue());
        setFieldValue(b.getX(), b.getY(), valueA);
    }

    public Field field(int x, int y) {
        return new Field(x, y);
    }

    public Stream<T> allFieldValues() {
        return fields.stream().flatMap(List::stream);
    }

    public Stream<Field> allFields() {
        List<Field> result = new LinkedList<>();
        for (int y = 0; y < fields.size(); y++) {
            for (int x = 0; x < fields.get(y).size(); x++) {
                result.add(field(x, y));
            }
        }
        return result.stream();
    }

    public Stream<T> row(int index) {
        return fields.get(index).stream();
    }

    public Stream<Stream<T>> rows() {
        return fields.stream().map(List::stream);
    }

    public Stream<T> column(int index) {
        return fields.stream().map(a -> a.get(index));
    }

    public Stream<Stream<T>> columns() {
        return IntStream.range(0, fields.size()).boxed().map(this::column);
    }

    public Stream<Stream<T>> rowsAndColumns() {
        return Stream.concat(rows(), columns());
    }

    public int dimension() {
        return fields.size();
    }

    @Override
    public String toString() {
        return this.rows().map(line -> line.map(Object::toString).collect(joining(" "))).collect(joining("\n"));
    }

    public synchronized void addBorder(int width, Supplier<T> supplier) {
        for (int i = 0; i < width; i++) {
            var oldDimension = dimension();

            fields.forEach(row -> {
                row.add(0, supplier.get());
                row.add(supplier.get());
            });

            fields.add(0, new LinkedList<>(Stream.generate(supplier).limit(oldDimension + 2).toList()));
            fields.add(new LinkedList<>(Stream.generate(supplier).limit(oldDimension + 2).toList()));
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode(of = { "x", "y" })
    public class Field {
        private final int x;
        private final int y;

        private Field(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public T getValue() {
            return fieldValue(x, y);
        }

        public Optional<Field> left() {
            return x - 1 >= 0 ? Optional.of(field(x - 1, y)) : Optional.empty();
        }

        public Optional<Field> top() {
            return y - 1 >= 0 ? Optional.of(field(x, y - 1)) : Optional.empty();
        }

        public Optional<Field> right() {
            return x + 1 < fields.get(0).size() ? Optional.of(field(x + 1, y)) : Optional.empty();
        }

        public Field rightWithOverflow() {
            return right().orElseGet(() -> field(0, y));
        }

        public Optional<Field> bottom() {
            return y + 1 < fields.size() ? Optional.of(field(x, y + 1)) : Optional.empty();
        }

        public Field bottomWithOverflow() {
            return bottom().orElseGet(() -> field(x, 0));
        }

        public Optional<Field> topLeft() {
            return x - 1 >= 0 && y - 1 >= 0 ? Optional.of(field(x - 1, y - 1)) : Optional.empty();
        }

        public Optional<Field> topRight() {
            return x + 1 < fields.get(0).size() && y - 1 >= 0 ? Optional.of(field(x + 1, y - 1)) : Optional.empty();
        }

        public Optional<Field> bottomLeft() {
            return x - 1 >= 0 && y + 1 < fields.size() ? Optional.of(field(x - 1, y + 1)) : Optional.empty();
        }

        public Optional<Field> bottomRight() {
            return x + 1 < fields.get(0).size() && y + 1 < fields.size() ? Optional.of(field(x + 1, y + 1)) : Optional.empty();
        }

        public Stream<Field> getAdjacents() {
            return Stream.of(top(), right(), bottom(), left()).flatMap(Optional::stream);
        }

        public Stream<Field> getAdjacentsWithDiagonals() {
            return Stream.of(top(), topRight(), right(), bottomRight(), bottom(), bottomLeft(), left(), topLeft()).flatMap(Optional::stream);
        }
    }

}
