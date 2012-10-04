package no.hig.qrcontact.qr_writer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;

public class QRCodeGenerator {

	private String data = null;
	private int width = 320;
	private int height = 480;

	public QRCodeGenerator(String data) {
		this.data = data;
	}

	public QRCodeGenerator(String data, int width, int height) {
		this.data = data;
		this.width = width;
		this.height = height;
	}

	public Bitmap getQRCode() {

		Charset charset = Charset.forName("ISO-8859-1");
		CharsetEncoder encoder = charset.newEncoder();
		byte[] b = null;
		try {
			// Convert a string to ISO-8859-1 bytes in a ByteBuffer
			ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(this.data));
			b = bbuf.array();
		} catch (CharacterCodingException e) {
			// System.out.println(e.getMessage());
		}

		String encodeData = "";
		try {
			encodeData = new String(b, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// System.out.println(e.getMessage());
		}

		// get a byte matrix for the data
		BitMatrix matrix = null;

		Bitmap mBitmap = Bitmap.createBitmap(this.width, this.height,
				Config.ARGB_8888);
		Writer writer = new QRCodeWriter();
		try {
			matrix = writer.encode(encodeData,
					com.google.zxing.BarcodeFormat.QR_CODE, this.width,
					this.height);

			for (int i = 0; i < this.height; i++) {
				for (int j = 0; j < this.width; j++) {
					mBitmap.setPixel(j, i, matrix.get(j, i) ? Color.BLACK
							: Color.WHITE);
				}
			}
		} catch (com.google.zxing.WriterException e) {
			// System.out.println(e.getMessage());
		}

		return mBitmap;
	}

}
