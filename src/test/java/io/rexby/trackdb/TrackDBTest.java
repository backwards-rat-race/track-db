package io.rexby.trackdb;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TrackDBTest {
    @Test
    void overview() throws IOException {
        File file = newFile();

        TrackDB trackDB = new TrackDB(file);

        assertEquals(Collections.emptyList(), trackDB.findKey(".*?"));
        assertEquals(Optional.empty(), trackDB.get("key_1"));
        trackDB.update("key_1", "test_1");
        assertEquals(Optional.of("test_1"), trackDB.get("key_1"));
        trackDB.update("key_1", "test_2");
        assertEquals(Optional.of("test_2"), trackDB.get("key_1"));
        trackDB.update("key_2", "test_1");
        trackDB.update("key_1", "test_3");
        assertEquals(Optional.of("test_1"), trackDB.get("key_2"));
        assertEquals(Optional.of("test_3"), trackDB.get("key_1"));

        // New database instance, pointing at same file
        trackDB = new TrackDB(file);

        assertEquals(Optional.of("test_1"), trackDB.get("key_2"));
        assertEquals(Optional.of("test_3"), trackDB.get("key_1"));
        // Newest keys first
        assertEquals(Arrays.asList("key_2", "key_1"), trackDB.findKey(".*?"));
        assertEquals(Collections.singletonList("key_1"), trackDB.findKey("1"));
        trackDB.delete("key_1");
        assertEquals(Optional.empty(), trackDB.get("key_1"));
        trackDB.delete("key_2");
        assertEquals(Optional.empty(), trackDB.get("key_2"));
        assertEquals(Collections.emptyList(), trackDB.findKey(".*?"));
    }

    @Test
    void escapeNewLines() throws IOException {
        File file = newFile();

        TrackDB trackDB = new TrackDB(file);
        trackDB.update("key", "value\nacross\nnewlines");
        assertEquals(Optional.of("value\nacross\nnewlines"), trackDB.get("key"));
        trackDB.update("key_2", "valu\nacros\nnewline");
        assertEquals(Optional.of("valu\nacros\nnewline"), trackDB.get("key_2"));

        trackDB.update("key_2", "value\\none\\nline");
        assertEquals(Optional.of("value\\none\\nline"), trackDB.get("key_2"));
    }

    @Test
    void escapeTabs() throws IOException {
        File file = newFile();

        TrackDB trackDB = new TrackDB(file);
        trackDB.update("key", "value\tacross\tnewlines");
        assertEquals(Optional.of("value\tacross\tnewlines"), trackDB.get("key"));
        trackDB.update("key_2", "valu\tacros\tnewline");
        assertEquals(Optional.of("valu\tacros\tnewline"), trackDB.get("key_2"));

        trackDB.update("key_2", "value\\tone\\tline");
        assertEquals(Optional.of("value\\tone\\tline"), trackDB.get("key_2"));
    }

    @Test
    void unicode() throws IOException {
        File file = newFile();

        TrackDB trackDB = new TrackDB(file);
        trackDB.update("key", "\uD83D\uDE00");
        assertEquals(Optional.of("\uD83D\uDE00"), trackDB.get("key"));

        trackDB.update("key", "\uD83D\uDC71\uD83D\uDC71\uD83C\uDFFB\uD83D\uDC71\uD83C\uDFFC\uD83D\uDC71\uD83C\uDFFD\uD83D\uDC71\uD83C\uDFFE\uD83D\uDC71\uD83C\uDFFF");
        assertEquals(Optional.of("\uD83D\uDC71\uD83D\uDC71\uD83C\uDFFB\uD83D\uDC71\uD83C\uDFFC\uD83D\uDC71\uD83C\uDFFD\uD83D\uDC71\uD83C\uDFFE\uD83D\uDC71\uD83C\uDFFF"), trackDB.get("key"));
    }

    private File newFile() throws IOException {
        File file = File.createTempFile("trackdb-test-", UUID.randomUUID().toString());
        file.deleteOnExit();
        return file;
    }
}