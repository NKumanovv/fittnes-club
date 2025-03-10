package com.fitness_club.medal.repository;

import com.fitness_club.medal.model.Medal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedalRepository extends JpaRepository<Medal, UUID> {


}
