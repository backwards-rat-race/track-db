package io.rexby.trackdb;

import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TrackDB {
    private final File file;

    public TrackDB(@NonNull File file) {
        assertFileAccessible(file);
        this.file = file;
    }

    public void delete(@NonNull String key) {
        try {
            new DeleteValue(file).forKey(key);
        } catch (Throwable t) {
            throw new TrackDBException(t);
        }
    }

    public void update(@NonNull String key, @NonNull String value) {
        try {
            new SetValue(file).keyWithValue(key, value);
        } catch (Throwable e) {
            throw new TrackDBException(e);
        }
    }

    public List<String> findKey(@NonNull String pattern) {
        try {
            return new FindKey(file).forPattern(pattern);
        } catch (Throwable e) {
            throw new TrackDBException(e);
        }
    }

    public Optional<String> get(@NonNull String key) {
        try {
            return new GetValue(file).forKey(key);
        } catch (Throwable e) {
            throw new TrackDBException(e);
        }
    }

    private static void assertFileAccessible(@NonNull File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new TrackDBException(String.format("Cannot create file: %s", file));
        }

        if (!file.canRead()) {
            throw new TrackDBException(String.format("Cannot read file: %s", file));
        }

        if (!file.canWrite()) {
            throw new TrackDBException(String.format("Cannot write file: %s", file));
        }
    }
}
