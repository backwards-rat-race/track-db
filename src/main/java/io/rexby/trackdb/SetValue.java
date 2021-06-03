package io.rexby.trackdb;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.unix4j.Unix4j.echo;

@RequiredArgsConstructor
class SetValue {
    private final File database;

    @SneakyThrows(IOException.class)
    public void keyWithValue(@NonNull String key, @NonNull String value) {
        // echo "update	$key	$value" >> "$db"
        echo("update\t" + key + "\t" + escape(value))
            .toWriter(new FileWriter(database, true));
    }

    private static String escape(@NonNull String value) {
        return StringEscapeUtils.escapeJava(value);
    }
}
