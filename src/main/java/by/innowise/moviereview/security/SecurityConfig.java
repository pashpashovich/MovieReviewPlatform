package by.innowise.moviereview.security;

import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/css/**", "/static/**", "/WEB-INF/**", "/register").permitAll()
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                        .requestMatchers("/user/**").hasAuthority("USER")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/")
//                        .loginProcessingUrl("/login")
//                        .successHandler(myAuthenticationSuccessHandler())
//                        .failureHandler((request, response, exception) -> {
//                            response.sendRedirect("/?error=" + URLEncoder.encode("Неверный логин или пароль", StandardCharsets.UTF_8));
//                        })
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            HttpSession session = request.getSession(false);
//                            if (session != null) {
//                                session.invalidate();
//                            }
//                            response.sendRedirect("/");
//                        })
//                        .permitAll()
//                )
//                .authenticationProvider(authenticationProvider());

        return http.build();
    }

//    @Bean
//    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
//        return new MyAuthenticationSuccessHandler(userRepository);
//    }
//
//    public static class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//        private final UserRepository userRepository;
//
//        public MyAuthenticationSuccessHandler(UserRepository userRepository) {
//            this.userRepository = userRepository;
//        }
//
//        @Override
//        public void onAuthenticationSuccess(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            Authentication authentication) throws IOException, ServletException {
//            var userDetails = (OurUserDetails) authentication.getPrincipal();
//            Long userId = userDetails.getId();
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//
//            if (Boolean.TRUE.equals(user.getIsBlocked())) {
//                response.sendRedirect("/?error=" + URLEncoder.encode("Вы заблокированы", StandardCharsets.UTF_8));
//                return;
//            }
//
//            String redirectUrl = "/user/profile/" + userId;
//            for (var authority : authentication.getAuthorities()) {
//                if (authority.getAuthority().equals("ADMIN")) {
//                    redirectUrl = "/admin/movies";
//                    break;
//                }
//            }
//            response.sendRedirect(redirectUrl);
//        }
//    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new OurUserInfoUserDetailsService(userRepository);
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return daoAuthenticationProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}