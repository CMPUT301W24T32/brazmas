package com.CMPUT301W24T32.brazmascheckin.helper;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * This class provides utility methods for QR code generation
 */
public class QRCodeGenerator {
    /**
     * This method generates a QR code based on a seed
     * @param eventID the seed
     * @return bitmap of the QR code
     */
    public static Bitmap generateQRCode(String eventID) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(eventID, BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = new BarcodeEncoder().createBitmap(bitMatrix);
            return bitmap;
        } catch(WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
