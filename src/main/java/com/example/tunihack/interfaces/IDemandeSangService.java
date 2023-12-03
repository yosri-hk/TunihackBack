package com.example.tunihack.interfaces;

import com.example.tunihack.entities.DemandeSang;

import java.util.List;

public interface IDemandeSangService {
    DemandeSang addOrUpdate (DemandeSang demandeSang);
    DemandeSang getDemandeSang(Integer id);
    List<DemandeSang> retrieveAll();
    List<DemandeSang>  retrieveByUser(Integer id);

}
