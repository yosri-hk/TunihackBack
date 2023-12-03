package com.example.tunihack.controllers;



import com.example.tunihack.dto.AuthenticationRequest;
import com.example.tunihack.dto.RegistrationRequest;
import com.example.tunihack.entities.User;
import com.example.tunihack.repoitories.UserRepo;
import com.example.tunihack.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws IOException {
        if (authenticationService.register(request) == null)
        {
            return ResponseEntity.ok("email exists");
        }

        else
        {
            return ResponseEntity.ok("account created");
        }

    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) throws IOException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping( "/mail-verif")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(authenticationService.confirmEmailToken(token));
    }
    @GetMapping("/set-ville")
    @ResponseBody
    public String setVillef(@RequestParam String ville){
        User user = authenticationService.currentlyAuthenticatedUser();
        user.setVille(ville);
        userRepo.save(user);
        return "ville updated";
    }
    @GetMapping("/set-blood-type")
    @ResponseBody
    public String setBloodTypee(@RequestParam String bloodType){
        User user = authenticationService.currentlyAuthenticatedUser();
        user.setBloodType(bloodType);
        userRepo.save(user);
        return "blood type updated";
    }


    @GetMapping("/get")
    public User getbymail(@RequestParam String email)
    {
        return userRepo.findByEmail2(email);
    }
}
