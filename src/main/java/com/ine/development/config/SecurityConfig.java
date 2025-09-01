package com.ine.development.config;

import com.ine.development.components.CustomAccessDeniedHandler;
import com.ine.development.components.CustomAuthEntryPoint;
import com.ine.development.components.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Configura la codificación de contraseñas, la cadena de filtros de seguridad y las reglas de CORS.
 */
@Configuration
public class SecurityConfig {

    @Value("${security.password.pepper}")
    private String appSecret;

    @Value("${app.url.api.origins}")
    private String allowedOrigins;

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          CustomAuthEntryPoint authEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Define un bean para el codificador de contraseñas.
     * Utiliza una configuración delegada que soporta Argon2 y BCrypt, con Argon2 como predeterminado.
     *
     * @return una instancia de `PasswordEncoder` configurada.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        Argon2PasswordEncoder argon2 = new Argon2PasswordEncoder(16, 32, 1, 16384, 3);
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(12);
        String defaultId = "argon2id";
        DelegatingPasswordEncoder delegate = new DelegatingPasswordEncoder(defaultId, Map.of(defaultId, argon2, "bcrypt", bCrypt));
        return new PepperingConfig(delegate, appSecret);
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     * Desactiva CSRF, habilita CORS y permite todas las solicitudes sin restricciones.
     *
     * @param http el objeto `HttpSecurity` para configurar la seguridad web.
     * @return una instancia de `SecurityFilterChain` configurada.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configura las reglas de CORS para la aplicación.
     * Define los orígenes, métodos, encabezados permitidos y otras configuraciones relacionadas con CORS.
     *
     * @return una instancia de `CorsConfigurationSource` configurada.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}