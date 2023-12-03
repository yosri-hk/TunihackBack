package com.example.tunihack.repoitories;

import com.example.tunihack.entities.DemandeSang;
import com.example.tunihack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeSangRepo extends JpaRepository<DemandeSang, Integer> {
    List<DemandeSang> findByUser(User user);
}
