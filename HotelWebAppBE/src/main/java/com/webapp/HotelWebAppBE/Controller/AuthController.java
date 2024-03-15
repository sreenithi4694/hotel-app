package com.webapp.HotelWebAppBE.Controller;

import com.webapp.HotelWebAppBE.Models.Customer;
import com.webapp.HotelWebAppBE.Models.ELoginProvider;
import com.webapp.HotelWebAppBE.Models.ERole;
import com.webapp.HotelWebAppBE.Models.Role;
import com.webapp.HotelWebAppBE.Repository.CustomerRepository;
import com.webapp.HotelWebAppBE.Repository.RoleRepository;
import com.webapp.HotelWebAppBE.Security.jwt.JwtUtils;
import com.webapp.HotelWebAppBE.Service.CustomerDetailsImpl;
import com.webapp.HotelWebAppBE.payload.Request.LoginRequest;
import com.webapp.HotelWebAppBE.payload.Request.SignupRequest;
import com.webapp.HotelWebAppBE.payload.Request.SocialLoginRequest;
import com.webapp.HotelWebAppBE.payload.Response.MessageResponse;
import com.webapp.HotelWebAppBE.payload.Response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // CHECK IF USER REGISTERED AS SOCIAL LOGIN
        if (customerRepository.existsByUsername(loginRequest.getUsername())) {
            Customer customer = customerRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + loginRequest.getUsername()));
            if(!"LOCAL".equals(customer.getLoginProvider())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Please use "+customer.getLoginProvider().toLowerCase()+" to login"));
            }
        }

        // CHECK IF USER EXISTS
        if (!customerRepository.existsByUsername(loginRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error User not found please register!"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomerDetailsImpl userDetails = (CustomerDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,jwtCookie.toString()));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (customerRepository.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (customerRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Customer customer = new Customer(signUpRequest.getEmail(),
                signUpRequest.getEmail(),
                signUpRequest.getName(),
                encoder.encode(signUpRequest.getPassword()),
                String.valueOf(ELoginProvider.LOCAL));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        customer.setRoles(roles);
        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/handleGoogleSign")
    public ResponseEntity<?> handleGoogleSign(@Valid @RequestBody SocialLoginRequest socialLoginRequest){

        if(!customerRepository.existsByEmail(socialLoginRequest.getEmail())){

            // Create new user's account for google with sample password
            Customer customer = new Customer(socialLoginRequest.getEmail(),
                    socialLoginRequest.getEmail(),
                    socialLoginRequest.getName(),
                    "googlelogin",
                    String.valueOf(ELoginProvider.GOOGLE));

            // ADD DEFAULT ROLE AS he will be USER
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            customer.setRoles(roles);
            customerRepository.save(customer);
        }


        return ResponseEntity.ok(new MessageResponse("Google User Data have been validated "));
    }
}
