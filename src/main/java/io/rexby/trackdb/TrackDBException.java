package io.rexby.trackdb;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TrackDBException extends RuntimeException {
    public TrackDBException(String message) {
        super(message);
    }

    public TrackDBException(Throwable cause) {
        super(cause);
    }
}
