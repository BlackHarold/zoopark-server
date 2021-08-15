package home.blackharold.zoopark.controller;

import home.blackharold.zoopark.payload.request.LoginRequest;
import home.blackharold.zoopark.payload.request.SignupRequest;
import home.blackharold.zoopark.payload.response.JWTTokenSuccessResponse;
import home.blackharold.zoopark.payload.response.MessageResponse;
import home.blackharold.zoopark.security.JWTTokenProvider;
import home.blackharold.zoopark.security.SecurityConstants;
import home.blackharold.zoopark.service.UserService;
import home.blackharold.zoopark.validation.ResponseErrorValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final ResponseErrorValidation responseErrorValidation;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(ResponseErrorValidation responseErrorValidation,
                          UserService userService,
                          AuthenticationManager authenticationManager,
                          JWTTokenProvider jwtTokenProvider) {
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        LOG.debug("api/auth/login ");
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtPrefixAndToken = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        LOG.debug("jwtPrefixAndToken: " + jwtPrefixAndToken);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwtPrefixAndToken));
    }


    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        LOG.debug("api/auth/signup");
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}

