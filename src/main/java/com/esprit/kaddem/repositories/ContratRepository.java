package com.esprit.kaddem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.esprit.kaddem.entities.Contrat;
import java.util.Date;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Integer> {

    @Query("SELECT count(c) FROM Contrat c where ((c.archived=true) and  ((c.dateDebutContrat BETWEEN :startDate AND :endDate)) or(c.dateFinContrat BETWEEN :startDate AND :endDate))")
    public Integer getnbContratsValides(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
