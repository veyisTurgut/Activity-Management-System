package yte.intern.project.util;

import yte.intern.project.manageActivities.entity.Activity;
import yte.intern.project.manageActivities.entity.Users;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailSender {
    public static void emailSender(Activity activity, Users user) throws MessagingException {
        String myAccount = "***@gmail.com";
        String password = "*******";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccount, password);
            }
        });
        Message message = prepareMessage(session, myAccount, activity, user);
        Transport.send(message);
    }
    private static Message prepareMessage(Session session, String myAccount, Activity activity, Users user) {
        try {
            String link = "https://www.google.com/maps/search/?api=1&query=" + activity.getLatitude() + "," + activity.getLongitude();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccount));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject(activity.getTitle() + " Kaydı Bildirimi");
            String html = "<h1>Etkinlige kaydoldugunuz icin tesekkurler!</h1> " +
                    "<br/>" +
                    "<h3><b> Etkinlik tarihleri: </b>" + activity.getStartDate() + " / " + activity.getFinishDate() + "</h3>" +
                    "<a href=" + link + "><b> Google Haritalar Linki</b></a>" +
                    "<br/><br/>" +
                    "<h2><b> Katilimci bilgileri: </b>" + user.getName() + " " + user.getSurname() + " " + user.getTcKimlikNo() + "</h2>" +
                    "</br>" +
                    "<img src=\"cid:image\">";
            BodyPart messageBodyPart = new MimeBodyPart();
            MimeMultipart multipart = new MimeMultipart("related");
            messageBodyPart.setContent(html, "text/html");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("C:\\Users\\**\\Desktop\\project\\src\\main\\reactapp\\src\\MyQRCode.png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
            // put everything together
            message.setContent(multipart);
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}