package tn.enicarthage.gestionspectacle.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class ReservationDTO {
    private Long spectacleDateLieuId; // Garder le même nom qu'Android

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String dateReservation;
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private int nbPlaces;
    private String paymentMethod;
    private String paymentStatus;


    public LocalDateTime getParsedDateReservation() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(this.dateReservation, formatter);
    }


}