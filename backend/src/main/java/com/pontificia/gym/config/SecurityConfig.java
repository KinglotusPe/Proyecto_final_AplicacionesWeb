package com.pontificia.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // Ruta de login
                .requestMatchers("/login").permitAll()
                // Portal del Socio / Cliente
                .requestMatchers("/portal/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN")
                // Módulos de Entrenadores y Evaluaciones Físicas
                .requestMatchers("/entrenadores/**", "/seguimientos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPCIONISTA", "ROLE_ENTRENADOR")
                // Módulos Administrativos y Operativos
                .requestMatchers("/clientes/**", "/membresias/**", "/pagos/**", "/asistencias/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPCIONISTA")
                // Panel Principal / Dashboard
                .requestMatchers("/").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPCIONISTA")
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
