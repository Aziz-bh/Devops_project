package com.esprit.kaddem;

import org.junit.jupiter.api.Test;

import com.esprit.kaddem.entities.Contrat;
import com.esprit.kaddem.entities.Specialite;

import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

public class ContratTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();
        Specialite specialite = Specialite.IA;
        Boolean archived = false;
        Integer montantContrat = 1000;

        // Act
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setDateDebutContrat(startDate);
        contrat.setDateFinContrat(endDate);
        contrat.setSpecialite(specialite);
        contrat.setArchived(archived);
        contrat.setMontantContrat(montantContrat);

        // Assert
        assertThat(contrat.getIdContrat()).isEqualTo(1);
        assertThat(contrat.getDateDebutContrat()).isEqualTo(startDate);
        assertThat(contrat.getDateFinContrat()).isEqualTo(endDate);
        assertThat(contrat.getSpecialite()).isEqualTo(specialite);
        assertThat(contrat.getArchived()).isEqualTo(archived);
        assertThat(contrat.getMontantContrat()).isEqualTo(montantContrat);
    }

    @Test
    public void testToString() {
        // Arrange
        Contrat contrat = new Contrat(1, new Date(), new Date(), Specialite.CLOUD, false, 1500);

        // Act
        String result = contrat.toString();

        // Assert
        assertThat(result).contains("idContrat=1");
        assertThat(result).contains("specialite=CLOUD");
        assertThat(result).contains("montantContrat=1500");
    }

    @Test
    public void testConstructor() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();
        Specialite specialite = Specialite.RESEAU;
        Boolean archived = true;
        Integer montantContrat = 2000;

        // Act
        Contrat contrat = new Contrat(2, startDate, endDate, specialite, archived, montantContrat);

        // Assert
        assertThat(contrat.getIdContrat()).isEqualTo(2);
        assertThat(contrat.getDateDebutContrat()).isEqualTo(startDate);
        assertThat(contrat.getDateFinContrat()).isEqualTo(endDate);
        assertThat(contrat.getSpecialite()).isEqualTo(specialite);
        assertThat(contrat.getArchived()).isEqualTo(archived);
        assertThat(contrat.getMontantContrat()).isEqualTo(montantContrat);
    }
}