
import java.awt.Font;
import java.io.ByteArrayInputStream;

public class ImgFontByte {
	public Font getFont(int fontHeight) {
		try {
			Font baseFont = Font.createFont(0, new ByteArrayInputStream(hex2byte(getFontByteStr())));
			return baseFont.deriveFont(0, fontHeight);
		} catch (Exception e) {
		}
		return new Font("Arial", 0, fontHeight);
	}

	private byte[] hex2byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if ((len == 0) || (len % 2 == 1)) {
			return null;
		}
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[(i / 2)] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}

			return b;
		} catch (Exception e) {
		}
		return null;
	}

	private String getFontByteStr() {
		return null;
	}
}