package tn.enicarthage.gestionspectacle.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tn.enicarthage.gestionspectacle.model.Reservation;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

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

            // Préparation du message MIME
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Configuration du contexte Thymeleaf
            Context context = new Context();
            context.setVariable("reservation", reservation);

            // Génération du contenu HTML à partir du template
            String htmlContent = templateEngine.process("email-reservation", context);

            // Configuration de l'email
            helper.setTo(emailDestinataire);
            helper.setSubject("Confirmation de réservation" );
            helper.setText(htmlContent, true); // true pour indiquer que c'est du HTML
            helper.setFrom("projetpfemailer@gmail.com\n", "");

            // Envoi de l'email
            mailSender.send(mimeMessage);

            System.out.println("Email HTML envoyé avec succès à " + emailDestinataire);

        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email HTML: " + e.getMessage());
            // Vous pourriez logger cette erreur dans un système de logging
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'envoi d'email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}