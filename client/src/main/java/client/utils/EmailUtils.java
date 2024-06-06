package client.utils;

import commons.Event;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class EmailUtils {
    Properties prop;
    Session session;
    AppConfig config;
    Boolean valid;

    /**
     * Email utils constructor
     */
    public EmailUtils() {
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        config = new AppConfig();
        createSession();
    }

    private void createSession() {
        String username = config.getUserEmail();
        String password = config.getUserPassword();

        try {
            session = Session.getInstance(prop,
                    new jakarta.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    }
            );
            session.getTransport().connect();
            valid = true;
        } catch (Exception e) {
            System.out.println("Email credentials not set correctly.");
            valid = false;
        }
    }

    /**
     * Constructor used for testing
     * @param session mock session object
     * @param config mock config object
     */
    public EmailUtils(Session session, AppConfig config) {
        this.session = session;
        this.config = config;
    }

    /**
     * Send an invite email to every recipient
     * @param event the event the invitation belongs to
     * @param recipients list of all recipients
     */
    public void sendInvites(Event event, List<String> recipients) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(config.getUserEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    getAddressesFromList(recipients));
            message.setRecipient(Message.RecipientType.CC,
                    new InternetAddress(config.getUserEmail()));

            message.setSubject("You were invited to Splitty!");
            String body = getInviteMessageBody(event);
            message.setText(body, "utf-8", "html");

            send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a test email to the configured credentials
     */
    public void sendTest() {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(config.getUserEmail()));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(config.getUserEmail()));
            message.setRecipient(Message.RecipientType.CC,
                    new InternetAddress(config.getUserEmail()));

            message.setSubject("Splitty test email!");
            String body = "This is a test email. Your credentials are working successfully!";
            message.setText(body, "utf-8", "html");

            send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets Address objects from list of emails
     * @param recipients list of emails
     * @return Addresses of recipients
     */
    public Address[] getAddressesFromList(List<String> recipients) {
        System.out.println(recipients);
        return recipients.stream()
                .map(email -> {
                    try {
                        return new InternetAddress(email.strip());
                    } catch (AddressException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList()
                .toArray(new Address[0]);
    }

    /**
     * Method to make sending email static call wrapped into a non-static once
     * @param message message to send
     * @throws MessagingException exception if error with message
     */
    public void send(Message message) throws MessagingException {
        Transport.send(message);
    }

    private String getInviteMessageBody(Event e) {
        String message = "<h1>You were invited to join {0} on Splitty!</h1>\n" +
                "<p>A user has invited you to join their event. You can join this event" +
                " by entering the invite code <strong>{1}</strong> via the following URL: {2} </p>";
        message = message.replace("{0}", e.getTitle());
        message = message.replace("{1}", e.getInviteCode());
        message = message.replace("{2}", config.getServerUrl());
        return message;
    }

    /**
     * Check whether email credentials are valid
     * @return if connection was successful
     */
    public boolean isEmailSetUp() {
        return valid;
    }
}
