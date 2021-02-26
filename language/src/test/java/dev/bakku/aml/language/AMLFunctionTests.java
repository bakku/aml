package dev.bakku.aml.language;

import dev.bakku.aml.language.runtime.types.AMLError;
import dev.bakku.aml.language.runtime.types.AMLNumber;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AMLFunctionTests {
    @Test
    public void defineAndCallFunction() {
        var code = "function f(a, b) a + b ; end " +
            "f(1, 2) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(3), result);
    }

    @Test
    public void functionWithGlobalVars() {
        var code = "function diameter(r) π · r ^ 2 ; end " +
            "diameter(5) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(79), ((AMLNumber) result).ceil());
    }

    @Test
    public void functionCallAsArgument() {
        var code = "function addOne(a) a + 1 ; end " +
            "addOne(addOne(1)) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(3), result);
    }

    @Test
    public void nestedFunctionCalls() {
        var code = "function addOne(a) a + 1 ; end " +
            "function addTwo(a) addOne(addOne(a)); end " +
            "addTwo(2) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLNumber);
        assertEquals(AMLNumber.of(4), result);
    }

    @Test
    public void callingUndefinedFunctions() {
        var code = "f(1 , 2) ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void callingNonFunction() {
        var code = "a ← 1 + 1 ; a() ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void redefiningFunctionsMustBeForbidden() {
        var code = "function f(a, b) a + b ; end " +
            "function f(a, b) a + b ; end";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void duplicateArgumentNames() {
        var code = "function f(a, a) a + b ; end";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }

    @Test
    public void readingNonExistentLocalVals() {
        // must not return 1 + 1 and ignore the error
        var code = "function f(a) b ; end f(1) ; 1 + 1 ;";
        var result = TestHelper.evalCode(code);

        assertTrue(result instanceof AMLError);
    }
}
