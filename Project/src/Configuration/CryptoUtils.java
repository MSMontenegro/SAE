package Configuration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class is responsible for the management package: Analysis
 * 
 * @author: María Eva Villarreal Guzmán. E-mail: villarrealguzman@gmail.com
 */
public class CryptoUtils {

	public static final String AES = "AES";

	/**
	 * encrypt a value and generate a keyfile
	 * 
	 */
	public static String encrypt(String value) {
		try {
			SecretKeySpec sks = getSecretKeySpec();
			Cipher cipher = Cipher.getInstance(CryptoUtils.AES);
			cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
			byte[] encrypted = cipher.doFinal(value.getBytes());
			return byteArrayToHexString(encrypted);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * decrypt a value
	 * 
	 */
	public static String decrypt(String message) {
		try {
			SecretKeySpec sks = getSecretKeySpec();
			Cipher cipher = Cipher.getInstance(CryptoUtils.AES);
			cipher.init(Cipher.DECRYPT_MODE, sks);
			byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
			return new String(decrypted);
		} catch (Exception e) {
			return null;
		}
	}

	private static SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException, IOException {
		byte[] key = new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
		SecretKeySpec sks = new SecretKeySpec(key, CryptoUtils.AES);
		return sks;
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	private static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

}