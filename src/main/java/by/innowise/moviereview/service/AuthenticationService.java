package by.innowise.moviereview.service;


import by.innowise.moviereview.dto.AuthenticationRequest;
import by.innowise.moviereview.dto.AuthenticationResponse;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var userOptional = userRepository.findByEmail(request.getEmail());
            User user = userOptional.orElseThrow();
            if (user.getIsBlocked()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ваш аккаунт заблокирован. Пожалуйста, свяжитесь с поддержкой.");
            }

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .id(user.getId())
                    .role(user.getRole().name())
                    .build();

        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль", ex);
        }
    }

}