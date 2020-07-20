package com.example.service.impl;

import com.example.adserv.vo.MarketingAccount;
import com.example.exception.ServiceException;
import com.example.messaging.NotificationSender;
import com.example.model.UserEntity;
import com.example.repository.UserRepository;
import com.example.security.JWTProvider;
import com.example.service.UserService;
import com.example.vo.*;
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
public class UserServiceImpl implements UserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JWTProvider jwtProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected NotificationSender notificationSender;

    @Override
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

    @Override
    public RegistrationResponse register(User user) {

        if (!userRepository.existsByUsername(user.getUsername())) {

            // Save in Repository
            UserEntity userEntity = modelMapper.map(user, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity savedUserEntity = userRepository.save(userEntity);
            System.out.println("Saved User: " + savedUserEntity);

            // Call AdServ
            MarketingAccount reqMarketingAccount = new MarketingAccount(savedUserEntity.getId());
            System.out.println("Requested MarketingAccount: " + reqMarketingAccount);
            MarketingAccount marketingAccount = restTemplate.postForObject(
                    "http://localhost:8081/v1/marketing/activate", reqMarketingAccount, MarketingAccount.class);
            System.out.println("Activated MarketingAccount: " + marketingAccount);


            // Create Token
            String token = jwtProvider.createToken(userEntity.getUsername(), userEntity.getRoles());

            // Send Notification
            if(userEntity.getEmail() != null) {
                notificationSender.send(new Notification(NotificationType.EMAIL, userEntity.getEmail(), "Account Created!"));
            }
            if(userEntity.getMobile() != null) {
                notificationSender.send(new Notification(NotificationType.SMS, userEntity.getMobile(), "Account Created!"));
            }

            System.out.println("-------------------------------------------------");
            System.out.println("User Registration completed and response sent!");
            System.out.println("Notification will be sent asynchronously");
            System.out.println("-------------------------------------------------");

            return new RegistrationResponse(token, marketingAccount);

        } else {
            throw new ServiceException("Username already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    public User search(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new ServiceException("User Not Found", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(userEntity, User.class);
    }

    @Override
    public String refresh(String username) {
        return jwtProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }

}
