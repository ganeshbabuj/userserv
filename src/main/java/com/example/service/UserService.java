package com.example.service;

import com.example.adserv.vo.MarketingAccount;
import com.example.exception.ServiceException;
import com.example.model.UserEntity;
import com.example.repository.UserRepository;
import com.example.security.JWTProvider;
import com.example.vo.LoginRequest;
import com.example.vo.LoginResponse;
import com.example.vo.RegistrationResponse;
import com.example.vo.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtProvider.createToken(username, userRepository.findByUsername(username).getRoles());
            return new LoginResponse(token);
        } catch (AuthenticationException e) {
            throw new ServiceException("Invalid username/password", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public RegistrationResponse register(User user) {

        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        if (!userRepository.existsByUsername(userEntity.getUsername())) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity savedUserEntity = userRepository.save(userEntity);
            System.out.println("Saved User: " + savedUserEntity);

            // Call AdServ
            MarketingAccount reqMarketingAccount = new MarketingAccount(savedUserEntity.getId());
            System.out.println("Requested MarketingAccount: " + reqMarketingAccount);

            MarketingAccount marketingAccount = restTemplate.postForObject(
                    "http://localhost:8081/v1/marketing/activate", reqMarketingAccount, MarketingAccount.class);

            System.out.println("Activated MarketingAccount: " + marketingAccount);


            String token = jwtProvider.createToken(userEntity.getUsername(), userEntity.getRoles());

            return new RegistrationResponse(token, marketingAccount);

        } else {
            throw new ServiceException("Username already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User search(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new ServiceException("User Not Found", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(userEntity, User.class);
    }

    public String refresh(String username) {
        return jwtProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }

}
