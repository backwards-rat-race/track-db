package io.rexby.trackdb;

import org.unix4j.io.ReaderInput;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class TacInput extends ReaderInput  {
    protected TacInput(File file) {
        super(reverseReader(file));
    }

    private static Reader reverseReader(File file) {
        // TODO. Not great as we're reading the whole file in.
        //  Ideally we wouldn't

        List<String> lines;

        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new TrackDBException(e);
        }

        Collections.reverse(lines);
        String reversed = lines
                .stream()
                .collect(Collectors.joining(System.lineSeparator()));

        return new StringReader(reversed);
    }
}
