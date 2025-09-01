package com.ine.development.config;

import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Implementación de PasswordEncoder que añade un valor secreto (pepper)
 * a las contraseñas antes de codificarlas o verificarlas.
 * Esto mejora la seguridad al dificultar los ataques de fuerza bruta.
 */
public class PepperingConfig implements PasswordEncoder {

    private final PasswordEncoder delegate;
    private final String pepper;

    public PepperingConfig(PasswordEncoder delegate, String pepper) {
        this.delegate = delegate;
        this.pepper = pepper;
    }

    private CharSequence withPepper(CharSequence raw) {
        return raw + pepper;
    }

    @Override
    public String encode(CharSequence raw) {
        return delegate.encode(withPepper(raw));
    }

    @Override
    public boolean matches(CharSequence raw, String encoded) {
        return delegate.matches(withPepper(raw), encoded);
    }

    @Override
    public boolean upgradeEncoding(String encoded) {
        return delegate.upgradeEncoding(encoded);
    }
}
