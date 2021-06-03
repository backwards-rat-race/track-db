package io.rexby.trackdb;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;
import org.unix4j.unix.cut.CutOption;
import org.unix4j.unix.sort.SortOption;
import org.unix4j.unix.sort.SortOptions;

import java.io.File;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
class FindKey {
    private static final SortOptions SORT_OPTIONS
            = new SortOptions.Default(SortOption.unique, SortOption.reverse);

    private final File database;

    public List<String> forPattern(@NonNull String pattern) {
        // tac <database> | sort -u -k2,2 | grep -E '^update\t.*?$' | cut -f2 | grep <pattern>

        return Unix4j
                .sort(SORT_OPTIONS, Comparator.comparing(FindKey::getKey), new TacInput(database))
                .grep("^update\\t.*?$")
                .cut(CutOption.fields, 2)
                .grep(pattern)
                .toStringList();
    }

    private static String getKey(Line line) {
        return Unix4j
                .echo(line.getContent())
                .cut(CutOption.fields, 2)
                .toStringResult();
    }
}
