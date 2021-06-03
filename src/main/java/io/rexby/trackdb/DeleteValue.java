package io.rexby.trackdb;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.unix4j.Unix4j.echo;

@RequiredArgsConstructor
class DeleteValue {
    private final File database;

    @SneakyThrows(IOException.class)
    public void forKey(@NonNull String key) {
        // echo "delete	$key" >> "$db"
        echo("delete\t" + key).toWriter(new FileWriter(database, true));
    }
}
