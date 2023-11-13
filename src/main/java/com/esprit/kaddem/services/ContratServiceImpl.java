package com.esprit.kaddem.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.esprit.kaddem.entities.Contrat;
import com.esprit.kaddem.entities.Departement;
import com.esprit.kaddem.entities.Etudiant;
import com.esprit.kaddem.entities.Specialite;
import com.esprit.kaddem.repositories.ContratRepository;
import com.esprit.kaddem.repositories.EtudiantRepository;
import com.esprit.kaddem.restcontrollers.dtos.ContratDTO;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ContratServiceImpl implements IContratService {

    ContratRepository contratRepository;
    EtudiantRepository etudiantRepository;

    @Override
    public List<Contrat> retrieveAllContrats() {
        log.info("debut methode retrieveAllContrats");
        return contratRepository.findAll();
    }

@Override
public Contrat updateContrat(ContratDTO c) {
    log.info("debut methode updateContrat");

    Optional<Contrat> optionalContrat = contratRepository.findById(c.getId());

        Contrat existingContrat = optionalContrat.get();

        existingContrat.setDateDebutContrat(c.getDateDebutContrat());
        existingContrat.setDateFinContrat(c.getDateFinContrat());
        existingContrat.setSpecialite(c.getSpecialite());
        contratRepository.save(existingContrat);

        return existingContrat;
 
}


    @Override
    public Contrat retrieveContrat(Integer idContrat) {
        log.info("debut methode retrieveContrat");
        Optional<Contrat> optionalContrat = contratRepository.findById(idContrat);
        
        return optionalContrat.orElse(new Contrat());
    }
    

    @Override
    public void removeContrat(Integer idContrat) {
        log.info("debut methode removeContrat");
        contratRepository.deleteById(idContrat);
    }

    @Override
    public Contrat addContrat(ContratDTO c) {
        // start date t1

        Contrat contrat = new Contrat();
        contrat.setDateDebutContrat(c.getDateDebutContrat());
        contrat.setDateFinContrat(c.getDateFinContrat());
        contrat.setSpecialite(c.getSpecialite());
        contratRepository.save(contrat);

        // te =t2-t1;
        return contrat;
    }

    @Transactional
    public Contrat addAndAffectContratToEtudiant(Contrat ce, String nomE, String prenomE) {
        Long startDate = new Date().getTime();
        log.info("startDate: " + startDate);
        log.info("debut methode addAndAffectContratToEtudiant");
        Etudiant etudiant = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
        log.info("etudiant: " + etudiant.getNomE() + " " + etudiant.getPrenomE());
        // nb contrats actifs
        Integer nbContratsActifs = etudiant.getContrats().size();
        if (nbContratsActifs > 5) {
            log.info("nombre de contrats autorisés est atteint");
            Long endDate = new Date().getTime();
            Long executionTime = endDate - startDate;
            log.info("endDate: " + startDate);
            log.info("executionTime: " + executionTime + " ms");
            return ce;
        }
        log.info("nb Contrats en cours: " + nbContratsActifs);
        contratRepository.save(ce);
        ce.setEtudiant(etudiant);
        log.info("fin methode addAndAffectContratToEtudiant");
        Long endDate = new Date().getTime();
        Long executionTime = endDate - startDate;

        log.info("endDate: " + startDate);
        log.info("executionTime: " + executionTime + " ms");

        return ce;
    }

    public Integer nbContratsValides(Date startDate, Date endDate) {
        return contratRepository.getnbContratsValides(startDate, endDate);
    }

    public void retrieveAndUpdateStatusContrat() {
        log.info("debut methode retrieveAndUpdateStatusContrat");
        List<Contrat> contrats = contratRepository.findAll();
        log.info("total contrats :" + contrats.size());

        for (Contrat contrat : contrats) {
            log.info("id: " + contrat.getIdContrat());
            log.info("date fin" + contrat.getDateFinContrat());
            log.info("archived " + contrat.getArchived());

            Date dateSysteme = new Date();

            if (contrat.getArchived() == null || contrat.getArchived() == false) {
                long difference_In_Time = contrat.getDateFinContrat().getTime() - dateSysteme.getTime();
                long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
                // il est préférable d'utiliser des méthodes prédéfinis de comparaison
                log.info("difference in days : " + difference_In_Days);
                // if (difference_In_Days>=0 && difference_In_Days<=15){ // entre 0 et 15 jours
                // exactement
                if (difference_In_Days == 15) { // pour 15 jours exactement
                    log.info(" Contrat Commencant le : " + contrat.getDateDebutContrat() + "pour l'etudiant "
                            + contrat.getEtudiant().getNomE() +
                            " " + contrat.getEtudiant().getPrenomE() + "  va bientot s achever le "
                            + contrat.getDateFinContrat());
                }
                if (difference_In_Days == 0) {
                    log.info("jour j: " + contrat.getIdContrat());
                    contrat.setArchived(true);
                    contratRepository.save(contrat);
                }
            }

            log.info("debut methode retrieveAndUpdateStatusContrat");
        }
    }

    public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate) {
        float difference_In_Time = (float) (endDate.getTime() - startDate.getTime());
        float difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
        float difference_In_months = difference_In_Days / 30;
        List<Contrat> contrats = contratRepository.findAll();
        float chiffreAffaireEntreDeuxDates = 0;
        float chiffreAffaireEntreDeuxDatesIA = 0;
        float chiffreAffaireEntreDeuxDatesCloud = 0;
        float chiffreAffaireEntreDeuxDatesReseau = 0;
        float chiffreAffaireEntreDeuxDatesSecurite = 0;

        for (Contrat contrat : contrats) {
            if (contrat.getSpecialite() == Specialite.IA) {
                chiffreAffaireEntreDeuxDates += (difference_In_months * contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesIA += (difference_In_months * contrat.getMontantContrat());

            } else if (contrat.getSpecialite() == Specialite.CLOUD) {
                chiffreAffaireEntreDeuxDates += (difference_In_months * contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesCloud += (difference_In_months * contrat.getMontantContrat());
            } else if (contrat.getSpecialite() == Specialite.RESEAU) {
                chiffreAffaireEntreDeuxDates += (difference_In_months * contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesReseau += (difference_In_months * contrat.getMontantContrat());

            } else if (contrat.getSpecialite() == Specialite.SECURITE) {
                chiffreAffaireEntreDeuxDates += (difference_In_months * contrat.getMontantContrat());
                chiffreAffaireEntreDeuxDatesSecurite += (difference_In_months * contrat.getMontantContrat());

            }
        }
        log.info("chiffreAffaireEntreDeuxDates: " + chiffreAffaireEntreDeuxDates);
        log.info("chiffreAffaireEntreDeuxDatesIA:" + chiffreAffaireEntreDeuxDatesIA);
        log.info("chiffreAffaireEntreDeuxDatesCloud " + chiffreAffaireEntreDeuxDatesCloud);
        log.info("chiffreAffaireEntreDeuxDatesReseau " + chiffreAffaireEntreDeuxDatesReseau);
        log.info("chiffreAffaireEntreDeuxDatesSecurite " + chiffreAffaireEntreDeuxDatesSecurite);
        return chiffreAffaireEntreDeuxDates;

    }

}