package tn.enicarthage.gestionspectacle.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tn.enicarthage.gestionspectacle.model.Reservation;

import javax.naming.Context;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmailConfirmation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null");
        }

        try {
            String emailDestinataire = reservation.isAvecCompte()
                    ? reservation.getUtilisateur().getEmail()
                    : reservation.getEmail();

            if (emailDestinataire == null || emailDestinataire.isBlank()) {
                throw new IllegalStateException("Aucun email valide trouvé pour la réservation");
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDestinataire);

            message.setSubject("Confirmation de réservation #" + reservation.getId());

            String text = "Bonjour " + reservation.getPrenom() + ",\n\n" +
                    "Votre réservation pour " + reservation.getSpectacleDateLieu().getSpectacle().getTitre() +
                    " a été confirmée.\n\n" +
                    "Détails :\n" +
                    "- Date: " + reservation.getSpectacleDateLieu().getDate() + "\n" +
                    "- Lieu: " + reservation.getSpectacleDateLieu().getLieu() + "\n" +
                    "- Places: " + reservation.getNbPlaces() + "\n\n" +
                    "Merci !";

            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            // Loggez l'erreur plutôt que de la lancer pour ne pas interrompre le flux
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }
}