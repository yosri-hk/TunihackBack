package com.example.tunihack.repoitories;



import com.example.tunihack.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail2(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);


}

