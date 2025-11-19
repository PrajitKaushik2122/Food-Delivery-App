package com.prajit.foodiesDelivery.controller;


import com.prajit.foodiesDelivery.io.AuthenticationRequest;
import com.prajit.foodiesDelivery.io.AuthenticationResponse;
import com.prajit.foodiesDelivery.service.AppUserDetailsService;
import com.prajit.foodiesDelivery.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authreq){
        System.out.println(authreq.getEmail()+" "+authreq.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authreq.getEmail(),authreq.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authreq.getEmail());
        final String token = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(token,authreq.getEmail());
    }
}
