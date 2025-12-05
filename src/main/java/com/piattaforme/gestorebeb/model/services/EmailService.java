package com.piattaforme.gestorebeb.model.services;

import com.piattaforme.gestorebeb.model.entities.Reservation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendSimpleEmail(String toAddress, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String senderEmail = "gestorebeb@libero.it";
            message.setFrom(senderEmail);
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(body);

            emailSender.send(message);
            System.out.println("Email inviata con successo a " + toAddress);
        } catch (Exception e) {
            System.err.println("Errore nell'invio della mail: " + e.getMessage());
        }
    }

    public void sendReservationConfirmation(Reservation reservation) {
        String to = reservation.getGuest().getEmail();
        String subject = "Conferma Prenotazione GestoreBeB";

        String body = String.format("""
                        Ciao %s,
                        
                        La tua prenotazione è stata confermata!
                        
                        Stanza: %s (%s)
                        Check-in: %s
                        Check-out: %s
                        Prezzo Totale: %.2f €
                        
                        Potrai pagare al momento del check in,
                        Grazie per averci scelto!
                        """,
                reservation.getGuest().getName(),
                reservation.getRoom().getName(),
                reservation.getRoom().getType(),
                reservation.getCheckin(),
                reservation.getCheckout(),
                reservation.getPrice()
        );

        sendSimpleEmail(to, subject, body);
    }

    public void sendCancellationConfirmation(Reservation reservation) {
        String to = reservation.getGuest().getEmail();
        String subject = "Conferma Prenotazione GestoreBeB";

        String body = String.format("""
                        Ciao %s,
                        
                        La tua prenotazione è stata cancellata con successo
                        """,
                reservation.getGuest().getName()
        );

        sendSimpleEmail(to, subject, body);
    }
}