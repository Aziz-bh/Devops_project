package com.esprit.kaddem.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.esprit.kaddem.entities.Contrat;
import com.esprit.kaddem.restcontrollers.dtos.ContratDTO;
import com.esprit.kaddem.services.IContratService;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/contrat")
@CrossOrigin(origins = "http://localhost:4200")
public class ContratRestController {
    @Autowired
    IContratService contratService;

    // http://localhost:8089/Kaddem/contrat/retrieve-all-contrats
    @GetMapping("/retrieve-all-contrats")
    @ResponseBody
    public List<Contrat> getContrats() {
        return contratService.retrieveAllContrats();
    }

    // http://localhost:8089/Kaddem/contrat/retrieve-contrat/8
    @GetMapping("/retrieve-contrat/{contrat-id}")
    @ResponseBody
    public Contrat retrieveContrat(@PathVariable("contrat-id") Integer contratId) {
        return contratService.retrieveContrat(contratId);
    }

    // http://localhost:8089/Kaddem/contrat/add-contrat
    @PostMapping("/add-contrat")
    @ResponseBody
    public Contrat addContrat(@RequestBody ContratDTO c) {
        return contratService.addContrat(c);
    }

    // http://localhost:8089/Kaddem/contrat/update-contrat
    @PutMapping("/update-contrat")
    @ResponseBody
    public Contrat updateContrat(@RequestBody ContratDTO cont) {
        return contratService.updateContrat(cont);
    }


    // The most common ISO Date Format yyyy-MM-dd — for example, "2000-10-31".
    @GetMapping(value = "/getnbContratsValides/{startDate}/{endDate}")
    public Integer getnbContratsValides(
            @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return contratService.nbContratsValides(startDate, endDate);
    }

    // Only no-arg methods may be annotated with @Scheduled
    @Scheduled(cron = "0 0 13 * * *") // (cron="0 0 13 * * ?")(fixedRate =21600)
    // @Scheduled(cron="45 * * * * *")//(cron="0 0 13 * * ?")(fixedRate =21600)
    @PutMapping(value = "/majStatusContrat")
    public void majStatusContrat() {
        contratService.retrieveAndUpdateStatusContrat();
    }


}
