package com.example.tunihack.services;



import com.example.tunihack.repoitories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepo userRepo;
    public int enableAppUser(String email) {
        return userRepo.enableUser(email);
    }

}
