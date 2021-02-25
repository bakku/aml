package dev.bakku.aml.language.runtime.types;

import com.oracle.truffle.api.dsl.TypeSystem;

@TypeSystem({AMLBoolean.class, AMLFraction.class, AMLNumber.class, AMLSet.class})
public abstract class AMLTypes {
}
