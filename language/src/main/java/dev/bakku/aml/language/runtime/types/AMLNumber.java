package dev.bakku.aml.language.runtime.types;

import ch.obermuhlner.math.big.BigDecimalMath;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import dev.bakku.aml.language.runtime.AMLRuntimeException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public class AMLNumber implements TruffleObject, AMLObject {
    private static final int PRECISION = 100;
    public static final AMLNumber PI = AMLNumber.of(BigDecimalMath.pi(new MathContext(PRECISION)));
    public static final AMLNumber E = AMLNumber.of(BigDecimalMath.e(new MathContext(PRECISION)));

    private final BigDecimal number;

    private AMLNumber(BigDecimal number) {
        this.number = number;
    }

    public static AMLNumber of(String number) {
        return new AMLNumber(new BigDecimal(number));
    }

    public static AMLNumber of(int number) {
        return new AMLNumber(
            new BigDecimal(number)
        );
    }

    public static AMLNumber of(BigDecimal number) {
        return new AMLNumber(number);
    }

    public AMLNumber add(AMLNumber other) {
        return new AMLNumber(this.number.add(other.number));
    }

    public AMLNumber subtract(AMLNumber other) {
        return new AMLNumber(this.number.subtract(other.number));
    }

    public AMLNumber multiply(AMLNumber other) {
        return new AMLNumber(this.number.multiply(other.number));
    }

    public AMLNumber divide(AMLNumber other) {
        if (other.number.equals(BigDecimal.ZERO)) {
            throw new AMLRuntimeException("Division by zero");
        }

        return new AMLNumber(this.number.divide(other.number, new MathContext(PRECISION)));
    }

    public AMLNumber modulo(AMLNumber other) {
        return new AMLNumber(this.number.remainder(other.number));
    }

    public AMLNumber power(AMLNumber other) {
        return new AMLNumber(
            BigDecimalMath.pow(this.number, other.number, new MathContext(PRECISION))
        );
    }

    public AMLNumber factorial() {
        return new AMLNumber(
            BigDecimalMath.factorial(this.number, new MathContext(PRECISION))
        );
    }

    public AMLNumber floor() {
        return new AMLNumber(this.number.setScale(0, RoundingMode.FLOOR));
    }

    public AMLNumber ceil() {
        return new AMLNumber(this.number.setScale(0, RoundingMode.CEILING));
    }

    public AMLFraction toFraction() {
        return AMLFraction.of(this, AMLNumber.of(1));
    }

    public AMLBoolean isSmaller(AMLNumber other) {
        return AMLBoolean.of(this.number.compareTo(other.number) < 0);
    }

    public AMLBoolean isGreater(AMLNumber other) {
        return AMLBoolean.of(this.number.compareTo(other.number) > 0);
    }

    public AMLNumber sqrt() {
        return of(this.number.sqrt(new MathContext(PRECISION)));
    }

    public BigDecimal unwrap() {
        return number;
    }

    @ExportMessage
    public boolean isNumber() {
        return fitsInLong();
    }

    @ExportMessage
    public boolean fitsInByte() {
        return this.number.toBigInteger().bitLength() < 8;
    }

    @ExportMessage
    public boolean fitsInShort() {
        return this.number.toBigInteger().bitLength() < 16;
    }

    @ExportMessage
    public boolean fitsInInt() {
        return this.number.toBigInteger().bitLength() < 32;
    }

    @ExportMessage
    public boolean fitsInLong() {
        return this.number.toBigInteger().bitLength() < 64;
    }

    @ExportMessage
    public boolean fitsInFloat() {
        return fitsInInt();
    }

    @ExportMessage
    public boolean fitsInDouble() {
        return fitsInLong();
    }

    @ExportMessage
    public byte asByte() throws UnsupportedMessageException {
        if (fitsInByte()) {
            return this.number.byteValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    public short asShort() throws UnsupportedMessageException {
        if (fitsInShort()) {
            return this.number.shortValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    public int asInt() throws UnsupportedMessageException {
        if (fitsInInt()) {
            return this.number.intValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    public long asLong() throws UnsupportedMessageException {
        if (fitsInLong()) {
            return this.number.longValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    public float asFloat() throws UnsupportedMessageException {
        if (fitsInFloat()) {
            return this.number.floatValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    public double asDouble() throws UnsupportedMessageException {
        if (fitsInDouble()) {
            return this.number.doubleValue();
        } else {
            throw UnsupportedMessageException.create();
        }
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return number.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AMLNumber amlNumber = (AMLNumber) o;
        return this.number.compareTo(amlNumber.number) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "AMLNumber{" +
            "number=" + number +
            '}';
    }
}
