package com.esprit.kaddem.services;

import java.util.Date;
import java.util.List;

import com.esprit.kaddem.entities.Contrat;
import com.esprit.kaddem.restcontrollers.dtos.ContratDTO;

public interface IContratService {
    List<Contrat> retrieveAllContrats();

    Contrat updateContrat(ContratDTO ce);

    Contrat retrieveContrat(Integer idContrat);

    void removeContrat(Integer idContrat);

    Contrat addContrat(ContratDTO c);

    public Integer nbContratsValides(Date startDate, Date endDate);


    public void retrieveAndUpdateStatusContrat();
}
