package com.ine.development.common;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Clase de utilidad para realizar actualizaciones condicionales en objetos.
 */
public final class Updaters {
    private Updaters() {}

    /**
     * Establece un valor utilizando un setter si el valor no es nulo.
     *
     * @param <T>    El tipo del valor.
     * @param value  El valor a establecer.
     * @param setter El consumidor que establece el valor.
     */
    public static <T> void setIfNonNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    /**
     * Establece un valor utilizando un setter si el valor no es nulo.
     *
     * @param <F>    El tipo del valor.
     * @param value  El valor a establecer.
     * @param setter El consumidor que establece el valor.
     */
    public static <F> void setIfNotEmpty(Collection<F> value, Consumer<Collection<F>> setter) {
        if (value != null && !value.isEmpty()) setter.accept(value);
    }


    /**
     * Establece una referencia utilizando un setter si el identificador no es nulo.
     *
     * @param <ID>        El tipo del identificador.
     * @param <E>         El tipo de la entidad referenciada.
     * @param id          El identificador de la referencia.
     * @param refProvider La función que proporciona la referencia a partir del identificador.
     * @param setter      El consumidor que establece la referencia.
     */
    public static <ID, E> void setRefIfNonNull(
            ID id, Function<ID, E> refProvider, Consumer<E> setter) {
        if (id != null) setter.accept(refProvider.apply(id));
    }
}
