package yte.intern.project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.config.MyUserDetailsService;
import yte.intern.project.manageActivities.entity.Admin;
import yte.intern.project.manageActivities.repository.AdminRepository;
import yte.intern.project.util.AuthenticationResponse;
import yte.intern.project.util.JwtUtil;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class LoginController {
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Admin admin) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Hatalı kullanıcı adı ya da şifre!", e);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(admin.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}