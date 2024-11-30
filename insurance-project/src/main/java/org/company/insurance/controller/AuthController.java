    package org.company.insurance.controller;

    import jakarta.mail.MessagingException;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.company.insurance.dto.JwtAuthenticationResponse;
    import org.company.insurance.dto.SignInRequest;
    import org.company.insurance.dto.SignUpRequest;
    import org.company.insurance.entity.User;
    import org.company.insurance.repository.UserRepository;
    import org.company.insurance.service.AuthenticationService;
    import org.company.insurance.service.EmailService;
    import org.company.insurance.service.JwtService;
    import org.company.insurance.service.UserService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.web.bind.annotation.*;

    import java.io.UnsupportedEncodingException;
    import java.time.LocalDateTime;
    import java.util.Optional;

    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {
        private final AuthenticationService authenticationService;
        private final UserRepository userRepository;
        private final EmailService emailService;
        private final JwtService jwtService;


        @PostMapping("/resend-verification-code")
        public ResponseEntity<String> resendVerificationCode(@RequestParam(name = "username") String username) throws MessagingException, UnsupportedEncodingException {
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found.");
            }

            User user = optionalUser.get();

            if (user.isEmailVerified()) {
                return ResponseEntity.badRequest().body("Email is already verified.");
            }

            user.setEmailVerificationCode(authenticationService.generateVerificationCode());
            user.setEmailVerificationExpiry(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);

            emailService.sendEmailCode(
                    user.getEmail(),
                    "Verify Your Email",
                    "Your new verification code is: " + user.getEmailVerificationCode()
            );

            return ResponseEntity.ok("Verification code resent to email: " + user.getEmail());
        }

        @PostMapping("/verify-email")
        public JwtAuthenticationResponse  verifyEmail(@RequestParam(name = "username") String username, @RequestParam(name = "code") String code) {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            if (user.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Verification code has expired");
            }

            if (!user.getEmailVerificationCode().equals(code)) {
                throw new IllegalArgumentException("Invalid verification code");
            }

            user.setEmailVerified(true);
            user.setEmailVerificationCode(null);
            user.setEmailVerificationExpiry(null);
            userRepository.save(user);

            var jwt = jwtService.generateToken(user);

            return new JwtAuthenticationResponse(jwt);
        }

        @PostMapping("/sign-up")
        public String signUp(@RequestBody @Valid SignUpRequest request) throws MessagingException, UnsupportedEncodingException {
            return authenticationService.signUp(request);
        }

        @PostMapping("/sign-in")
        public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
            return authenticationService.signIn(request);
        }

        @PostMapping("/refresh-token")
        public JwtAuthenticationResponse refreshToken() {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            JwtService jwtService = new JwtService();
            String jwt = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(jwt);
        }

        @GetMapping("/current-user")
        public String getCurrentUser(Authentication authentication) {
            return "Current user: " + authentication.getName() + ", roles: " + authentication.getAuthorities();
        }
    }