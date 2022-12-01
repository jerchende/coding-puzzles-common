package net.erchen.codingpuzzlescommon.parser;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SeparatorParser {

    public static <T> List<T> parseInput(String input, String separatorRegex, Function<String, T> mapToEntry) {
        if (input == null || input.isBlank()) {
            return List.of();
        }
        return Stream.of(input.split(separatorRegex))
                .map(String::trim)
                .map(mapToEntry)
                .collect(toList());
    }

}
