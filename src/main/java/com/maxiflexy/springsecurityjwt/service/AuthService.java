package com.maxiflexy.springsecurityjwt.service;

import com.maxiflexy.springsecurityjwt.dto.ReqRes;
import com.maxiflexy.springsecurityjwt.entity.UserModel;
import com.maxiflexy.springsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp =new ReqRes();
        try {
            UserModel userModel = new UserModel();
            userModel.setEmail(registrationRequest.getEmail());
            userModel.setPassword(registrationRequest.getPassword());
            userModel.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            userModel.setRole(registrationRequest.getRole());

            UserModel savedUserModel = userRepository.save(userModel);

            if(savedUserModel != null && savedUserModel.getId() > 0){
                resp.setOurUsers(savedUserModel);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }

    public ReqRes signIn(ReqRes signInRequest){

        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationDate("24Hrs");
            response.setMessage("Successfully Signed In");
        } catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }


    public ReqRes refreshToken(ReqRes refreshTokenRequest){

        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        UserModel users = userRepository.findByEmail(ourEmail).orElseThrow();

        if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)){

            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Refreshed Token");

        }
        response.setStatusCode(500);
        return response;
    }

}
