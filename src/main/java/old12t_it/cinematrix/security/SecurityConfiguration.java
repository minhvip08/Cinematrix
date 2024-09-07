package old12t_it.cinematrix.security;

import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.security.jwt.AuthEntryPointJwt;
import old12t_it.cinematrix.security.jwt.AuthTokenFilter;
import old12t_it.cinematrix.security.service.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserDetailsServiceImp userDtService;
    private final AuthEntryPointJwt unAuthorHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean // indicate that a method instantiates, configures and initialize a new
    // object by Spring IoC Container
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .authorizeHttpRequests(
                        authReq -> authReq
                                .requestMatchers("/login", "/register", "/reset-password", "/verify-otp", "/favicon.ico", "robots.txt", "firebase-messaging-sw.js", "/assets/**", "/.vite/manifest.json", "/api/auth/login",
                                        "/api/content/**", "/api/upload/**", "/api/notify/**", "/api/auth/register", "/api/auth/init_password_reset", "api/auth/reset_password", "/api/content/**", "/api/users/**", "/api/comment/**")
                                .permitAll()
                                .requestMatchers("/api/users/all", "/api/users/ban", "/api/users/unban", "/admin", "/admin/dashboard", "/admin/ban-user", "/api/content/all")
                                .hasAuthority("ADMIN")
                                .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.deleteCookies("accessToken"))
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(unAuthorHandler)
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder().username("email")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDtService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationManager) throws Exception {
        return authenticationManager.getAuthenticationManager();
    }
}
