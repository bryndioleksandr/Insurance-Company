    package org.company.insurance.controller;

    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.company.insurance.dto.JwtAuthenticationResponse;
    import org.company.insurance.dto.SignInRequest;
    import org.company.insurance.dto.SignUpRequest;
    import org.company.insurance.service.AuthenticationService;
    import org.company.insurance.service.JwtService;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {
        private final AuthenticationService authenticationService;

        @PostMapping("/sign-up")
        public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
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