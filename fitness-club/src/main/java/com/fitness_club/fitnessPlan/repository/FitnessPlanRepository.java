package com.fitness_club.fitnessPlan.repository;

import com.fitness_club.fitnessPlan.model.FitnessPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FitnessPlanRepository extends JpaRepository<FitnessPlan, UUID> {


}
