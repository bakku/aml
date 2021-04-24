package dev.bakku.aml.language;

import com.oracle.truffle.api.TruffleFile;

import java.io.IOException;
import java.nio.charset.Charset;

public class AMLFileDetector implements TruffleFile.FileTypeDetector {
    @Override
    public String findMimeType(TruffleFile file) {
        var name = file.getName();

        if (name != null && name.endsWith(".aml")) {
            return AMLLanguage.MIME_TYPE;
        }

        return null;
    }

    @Override
    public Charset findEncoding(TruffleFile file) {
        return null;
    }
}
