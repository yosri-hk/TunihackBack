package com.example.tunihack.repoitories;

import com.example.tunihack.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepo extends JpaRepository<Media, Integer> {
}
