package com.example.tunihack.controllers;


import com.example.tunihack.dto.DemandeSangRequest;
import com.example.tunihack.entities.DemandeSang;
import com.example.tunihack.entities.User;
import com.example.tunihack.enumerations.StatusDemande;
import com.example.tunihack.interfaces.IDemandeSangService;
import com.example.tunihack.repoitories.DemandeSangRepo;
import com.example.tunihack.repoitories.UserRepo;
import com.example.tunihack.services.AuthenticationService;
import com.example.tunihack.services.TwilioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/demande-sang")
@CrossOrigin(origins = "*")
public class DemandeSangController {
    private final IDemandeSangService service;
    private final TwilioService twilioService;
    private final DemandeSangRepo repository;
    private final UserRepo userRepo;
    private final AuthenticationService authenticationService;
    @PostMapping("/add")
    @ResponseBody
    public DemandeSang add(@RequestBody DemandeSangRequest demandeSangRequest){

        var demandeSang = DemandeSang.builder().ville(demandeSangRequest.getVille())
                .statusDemande(StatusDemande.valueOf(demandeSangRequest.getStatus()))
                .dateTime(LocalDateTime.now())
                .bloodType(demandeSangRequest.getBloodType())
                .hospital(demandeSangRequest.getHospital())
                .user(authenticationService.currentlyAuthenticatedUser())
                .closed(false)
                .build();
        if (demandeSang.getStatusDemande().equals(StatusDemande.URGENT)){
            //todo : TWILIO
            log.info("URGENTTTTTTTTTTTTTTTTTT");
            for (User user: userRepo.findAll()){
                if((
                        user.getBloodType().equals(demandeSangRequest.getBloodType())
                        )&& (user.getVille().equals(demandeSangRequest.getVille()))){
                    twilioService.sendAlert(String.valueOf(user.getPhoneNumber()),demandeSang.getHospital());
                }
            }
        }
        return service.addOrUpdate(demandeSang);
    }
    @GetMapping("/close")
    @ResponseBody
    public DemandeSang close(@RequestParam Integer id){
        DemandeSang demandeSang = service.getDemandeSang(id);
        demandeSang.setClosed(true);
        return repository.save(demandeSang);
    }
    @GetMapping("/get")
    @ResponseBody
    public DemandeSang findone(@RequestParam Integer id){
        return service.getDemandeSang(id);
    }
    @GetMapping("/getAll")
    @ResponseBody
    public List<DemandeSang> findAlll(){
        return service.retrieveAll();
    }
    @GetMapping("/find-by-user")
    @ResponseBody
    public List<DemandeSang> getByUser(@RequestParam Integer id){
        return service.retrieveByUser(id);
    }
    @GetMapping("/ref")
    @ResponseBody
    public DemandeSang clickLink(@RequestParam Integer idDemande, @RequestParam Integer idUser){
        User user = userRepo.getReferenceById(idUser);
        user.setPoints(user.getPoints()+50);
        userRepo.save(user);
        return repository.getReferenceById(idDemande);
    }
}
