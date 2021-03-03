package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLError;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLFunctionTests {
    @Test
    public void defineAndCallFunction() {
        var code = "f: (a, b) → a + b;" +
            "f(1, 2) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(3), result);
    }

    @Test
    public void functionWithGlobalVars() {
        var code = "diameter: (r) → π · r ^ 2; " +
            "diameter(5) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(79), ((AMLNumber) result).ceil());
    }

    @Test
    public void functionCallAsArgument() {
        var code = "addOne: (a) → a + 1; " +
            "addOne(addOne(1));";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(3), result);
    }

    @Test
    public void nestedFunctionCalls() {
        var code = "addOne: (a) → a + 1;" +
            "addTwo: (a) → addOne(addOne(a)); " +
            "addTwo(2);";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(4), result);
    }

    @Test
    public void functionsMustBeFirstClassCitizens() {
        var code =
            "f: (a) → a + 1; " +
            "twice: (op, x) → op(op(x)); " +
            "twice(f, 1);";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(3), result);
    }

    @Test
    public void iteratedFunctions() {
        var code =
            "inc: (a) → a + 1; " +
            "tenInc ← inc ^ 10; " +
            "tenInc(0);";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(10), result);
    }

    @Test
    public void composedFunctions() {
        var code =
            "inc: (a) → a + 1; " +
            "dec: (a) → a - 1; " +
            "incAndDec ← inc ∘ dec; " +
            "incAndDec(1);";

        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(1), result);
    }

    @Test
    public void callingUndefinedFunctions() {
        var code = "f(1, 2);";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void callingNonFunction() {
        var code = "a ← 1 + 1; a();";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void redefiningFunctionsMustBeForbidden() {
        var code = "f: (a, b) → a + b; " +
            "f: (a, b) → a + b;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void duplicateArgumentNames() {
        var code = "f: (a, a) → a + b;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void readingNonExistentLocalVals() {
        // must not return 1 + 1 and ignore the error
        var code = "f: (a) → b; f(1); 1 + 1;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }
}
