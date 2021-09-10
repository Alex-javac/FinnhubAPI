package com.iTechArt.FinnhubAPI.repository;

import com.iTechArt.FinnhubAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
