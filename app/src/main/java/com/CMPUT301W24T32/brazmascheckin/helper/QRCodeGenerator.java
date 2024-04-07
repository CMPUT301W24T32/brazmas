package com.CMPUT301W24T32.brazmascheckin.helper;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

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

    /**
     * Converts a bitmap representation of a QR code into a byte array.
     *
     * @param bitmap Bitmap representation of the QR code.
     * @return Byte array containing the bitmap data.
     */
    public static byte[] getQRCodeByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
