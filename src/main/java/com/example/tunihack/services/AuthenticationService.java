package com.example.tunihack.services;

import com.example.tunihack.dto.AuthenticationRequest;
import com.example.tunihack.dto.RegistrationRequest;
import com.example.tunihack.entities.ConfirmationToken;
import com.example.tunihack.entities.JwtToken;
import com.example.tunihack.entities.Media;
import com.example.tunihack.entities.User;
import com.example.tunihack.enumerations.BanType;
import com.example.tunihack.enumerations.Role;
import com.example.tunihack.enumerations.TokenType;
import com.example.tunihack.interfaces.EmailSender;
import com.example.tunihack.repoitories.JwtTokenRepo;
import com.example.tunihack.repoitories.MediaRepo;
import com.example.tunihack.repoitories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    private final MediaRepo mediaRepo;
    private final EmailSender emailSender;
    private final UserRepo userRepo;
    private final UserService userService;
    private final JwtService jwtService;
    private final ConfirmationTokenService tokenService;
    private final JwtTokenRepo jwtTokenRepo;
    public String authenticate(AuthenticationRequest request) throws IOException {
        var user = userRepo.findByEmail(request.getEmail()).orElse(null);

        if (user == null )
        {
            return "there is no account associated with such email! ";
        }

        if ((user.getEnabled()==false)&&(user.getBanType().equals(BanType.LOCK)))
        {
            return "account is locked. Verification is needed! ";
        }




        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var jwtTokenString = "";
        jwtTokenString=jwtService.generateJwtToken(user);
        revokeAllUserTokens(user);
        saveJwtToken(user, jwtTokenString);
        return jwtTokenString;
    }
    public User register(RegistrationRequest request) throws IOException {

        User user2 = userRepo.findByEmail2(request.getEmail());
        if (user2!= null)
        {
            return null;
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .points(0)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .banType(BanType.LOCK)
                .phoneNumber(request.getPhoneNumber())
                .enabled(false)
                .build();
        Media media = new Media();
        media.setName("default image");
        media.setImagenUrl("http://localhost/default.png");
        mediaRepo.save(media);
        user.setPicture(media);
        userRepo.save(user);
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
        tokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:4200/mail-verif?token="+token;
        emailSender.send(request.getEmail(),link);
        var jwtTokenString = jwtService.generateJwtToken(user);

        //saveJwtToken(user,jwtTokenString);
        return user;
    }
    public User currentlyAuthenticatedUser()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).get();

    }
    public void revokeAllUserTokens(User user) {
        var validUserTokens = jwtTokenRepo.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepo.saveAll(validUserTokens);
    }

    private void saveJwtToken(User user, String jwtTokenString) {
        var jwtToken = JwtToken.builder()
                .token(jwtTokenString)
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepo.save(jwtToken);
    }
    @Transactional
    public String confirmEmailToken(String token) {
        ConfirmationToken confirmationToken = tokenService
                .getToken(token).get();
        if (confirmationToken == null)
        {
            return "token not found";
        }

        if (confirmationToken.getConfirmedAt() != null) {
            return ("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String token2 = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken2 = new ConfirmationToken(token2, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    confirmationToken.getUser());
            tokenService.saveConfirmationToken(confirmationToken2);
            String link = "http://localhost:4200/mail-verif?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),link);
            return "email expired a new Email is sent!";
        }

        tokenService.setConfirmedAt(token);
        //userService.enableAppUser(
        //      confirmationToken.getUser().getEmail());
        User user = confirmationToken.getUser();
        user.setEmailVerif(true);
        user.setBanType(BanType.NONE);
        userRepo.save(user);
        userService.enableAppUser(confirmationToken.getUser().getEmail());
        return "Email confirmed ";
    }
}
