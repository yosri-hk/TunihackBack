package com.example.tunihack.services;


import com.example.tunihack.entities.ConfirmationToken;
import com.example.tunihack.repoitories.ConfirmationTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepo.save(token);
    }
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}

