package yte.intern.project.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import yte.intern.project.manageActivities.entity.Activity;
import yte.intern.project.manageActivities.entity.Users;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class qrCode {

    private static final String QR_CODE_IMAGE_PATH = "C:\\Users\\VeyisTurgut\\Desktop\\project\\src\\main\\reactapp\\src\\MyQRCode.png";

    public static void generateQRCodeImage(Activity activityDTO, Users user) throws IOException, WriterException {
        String text = "Etkinlik ismi : " + activityDTO.getTitle()
                + "\nEtkinlik tarihi : " + activityDTO.getStartDate() + " / " + activityDTO.getFinishDate()
                + "\nGoogle Haritalar linki : "
                + "https://www.google.com/maps/search/?api=1&query=" + activityDTO.getLatitude() + "," + activityDTO.getLongitude()
                + "\nKatilimci Ismi : " + user.getName() + " " + user.getSurname()
                + "\nKatilimci e-maili: " + user.getEmail()
                + "\nKatilimci Tcsi: " + user.getTcKimlikNo();
        generateQRCodeImage(text, 350, 350);
    }

    private static void generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", Paths.get(QR_CODE_IMAGE_PATH));
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }

}

