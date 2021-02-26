package dev.bakku.aml.language;

import dev.bakku.aml.language.parser.AMLParserException;
import org.junit.Test;

public class AMLParserErrorTests {
    @Test(expected = AMLParserException.class)
    public void shouldResultInAnError() {
        var code = "a";
        TestHelper.evalCode(code);
    }
}
