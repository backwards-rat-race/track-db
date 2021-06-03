package io.rexby.trackdb;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.unix4j.unix.cut.CutOption;

import java.io.File;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.unix4j.Unix4j.echo;
import static org.unix4j.Unix4j.grep;

@RequiredArgsConstructor
class GetValue {
    private final File database;

    public Optional<String> forKey(String key) {
        String regex
                = String.format("^(update|delete)\\t%s(\\t.*?)?$", Pattern.quote(key));

        // tac <database> | grep -m1 -E <regex>
        // We use .tail instead of the tac reader to work around a bug in Unix4J
        String result = grep(regex, database).tail(1).toStringResult();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        // echo <result> | cut -f1
        String operation = echo(result).cut(CutOption.fields, 1).toStringResult();

        if ("delete".equals(operation)) {
            return Optional.empty();
        }

        // echo <result> | cut -f3
        String value = echo(result).cut(CutOption.fields, 3).toStringResult();

        return Optional
                .of(value)
                .map(GetValue::unescape);
    }

    private static String unescape(@NonNull String escaped) {
        return StringEscapeUtils.unescapeJava(escaped);
    }
}
