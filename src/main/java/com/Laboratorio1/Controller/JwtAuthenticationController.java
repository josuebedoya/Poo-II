package com.Laboratorio1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.Laboratorio1.Config.JWTAuthtenticationConfig;
import com.Laboratorio1.Config.Model.Constans;
import com.Laboratorio1.Config.Model.JwtRequest;
import com.Laboratorio1.Config.Model.JwtResponse;
import com.Laboratorio1.Services.UsuarioServiceImpl;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    JWTAuthtenticationConfig jwtAuthtenticationConfig;
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;
    @Autowired
    private UsuarioServiceImpl usuarioService;

    @RequestMapping(
            value = "/authenticate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
            @RequestHeader(value = Constans.HEADER_APIKEY, required = false) String apiKey)
            throws Exception {
        System.out.println("**********************************************************************");
        System.out.println("authenticationRequest.getUsername():["+authenticationRequest.getUsername()+"]");
        System.out.println("authenticationRequest.getPassword():["+authenticationRequest.getPassword()+"]");
        System.out.println("**********************************************************************");
        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        //VALIDACION DE PASSWORD Y APIKEY (punto 6)
        if (!userDetails.getPassword().equals(authenticationRequest.getPassword())
                || !usuarioService.validarApiKey(authenticationRequest.getUsername(), apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales o APIKey invalidos");
        }
        final String token = jwtAuthtenticationConfig.getJWTToken(userDetails.getUsername());
        System.out.println("**********************************************************************");
        System.out.println("token:["+token+"]");
        System.out.println("**********************************************************************");
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
