package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class AMLFraction implements TruffleObject {
    private final AMLNumber numerator;
    private final AMLNumber denominator;

    private AMLFraction(AMLNumber numerator, AMLNumber denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static AMLFraction of(AMLNumber numerator, AMLNumber denominator) {
        return (new AMLFraction(numerator, denominator)).simplify();
    }

    public AMLFraction add(AMLFraction other) {
        var thisNumerator = this.numerator.multiply(other.denominator);
        var otherNumerator = other.numerator.multiply(this.denominator);
        var denominator = this.denominator.multiply(other.denominator);

        return AMLFraction.of(thisNumerator.add(otherNumerator), denominator);
    }

    public AMLFraction subtract(AMLFraction other) {
        var thisNumerator = this.numerator.multiply(other.denominator);
        var otherNumerator = other.numerator.multiply(this.denominator);
        var denominator = this.denominator.multiply(other.denominator);

        return AMLFraction.of(thisNumerator.subtract(otherNumerator), denominator);
    }

    public AMLFraction multiply(AMLFraction other) {
        return AMLFraction.of(
            this.numerator.multiply(other.numerator),
            this.denominator.multiply(other.denominator)
        );
    }

    public AMLFraction divide(AMLFraction other) {
        return AMLFraction.of(
            this.numerator.multiply(other.denominator),
            this.denominator.multiply(other.numerator)
        );
    }

    public AMLNumber ceil() {
        return toNumber().ceil();
    }

    public AMLNumber floor() {
        return toNumber().floor();
    }

    public AMLNumber toNumber() {
        return this.numerator.divide(this.denominator);
    }

    @ExportMessage
    public Object toDisplayString(boolean allowSideEffects) {
        return this.numerator.toDisplayString(allowSideEffects) + "/" +
            this.denominator.toDisplayString(allowSideEffects);
    }

    // Simplify using Euclidean algorithm
    private AMLFraction simplify() {
        var a = this.numerator;
        var b = this.denominator;

        while(!b.equals(AMLNumber.of(0))) {
            var t = b;
            b = a.modulo(b);
            a = t;
        }

        return new AMLFraction(this.numerator.divide(a), this.denominator.divide(a));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AMLFraction other = (AMLFraction) o;
        return this.toNumber().equals(other.toNumber());
    }

    @Override
    public String toString() {
        return "AMLFraction{" +
            "numerator=" + numerator +
            ", denominator=" + denominator +
            '}';
    }
}
