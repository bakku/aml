package dev.bakku.aml.language.runtime.types;

public interface AMLCallable {
    Object invoke(Object... arguments);
    int arity();
}
