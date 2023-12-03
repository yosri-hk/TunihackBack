package com.example.tunihack.services;

import com.example.tunihack.entities.DemandeSang;
import com.example.tunihack.interfaces.IDemandeSangService;
import com.example.tunihack.repoitories.DemandeSangRepo;
import com.example.tunihack.repoitories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeSangService implements IDemandeSangService {
    private final DemandeSangRepo repository;
    private final UserRepo userRepo;
    @Override
    public DemandeSang addOrUpdate(DemandeSang demandeSang) {
        return repository.save(demandeSang);
    }

    @Override
    public DemandeSang getDemandeSang(Integer id) {
        return repository.getReferenceById(id);
    }

    @Override
    public List<DemandeSang> retrieveAll() {
        return repository.findAll();
    }

    @Override
    public List<DemandeSang> retrieveByUser(Integer id) {
        return repository.findByUser(userRepo.getReferenceById(id));
    }
}
