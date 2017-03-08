
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.imageio.ImageIO;

public class ValidateCode {
	private int width = 160;

	private int height = 40;

	private int codeCount = 5;

	private int lineCount = 150;

	private String code = null;

	private BufferedImage buffImg = null;

	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'N', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'M', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

	public ValidateCode() throws NoSuchAlgorithmException {
		createCode();
	}

	public ValidateCode(int width, int height) throws NoSuchAlgorithmException {
		this.width = width;
		this.height = height;
		createCode();
	}

	public ValidateCode(int width, int height, int codeCount, int lineCount) throws NoSuchAlgorithmException {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		createCode();
	}

	public void createCode() throws NoSuchAlgorithmException {
		int x = 0;
		int fontHeight = 0;
		int codeY = 0;
		int red = 0;
		int green = 0;
		int blue = 0;

		x = this.width / (this.codeCount + 1);
		fontHeight = this.height - 10;
		codeY = this.height - 4;

		this.buffImg = new BufferedImage(this.width, this.height, 1);
		Graphics2D g = this.buffImg.createGraphics();

		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.width, this.height);

		ImgFontByte imgFont = new ImgFontByte();
		Font font = imgFont.getFont(fontHeight);
		g.setFont(font);

		for (int i = 0; i < this.lineCount; ++i) {
			int xs = random.nextInt(this.width);
			int ys = random.nextInt(this.height);
			int xe = xs + random.nextInt(this.width / 8);
			int ye = ys + random.nextInt(this.height / 8);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawLine(xs, ys, xe, ye);
		}

		StringBuffer randomCode = new StringBuffer();

		for (int i = 0; i < this.codeCount; ++i) {
			String strRand = String.valueOf(this.codeSequence[random.nextInt(this.codeSequence.length)]);

			red = random.nextInt(150);
			green = random.nextInt(150);
			blue = random.nextInt(150);
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * x, codeY);

			randomCode.append(strRand);
		}

		this.code = randomCode.toString();
	}

	public void write(String path) throws IOException {
		OutputStream sos = new FileOutputStream(path);
		write(sos);
	}

	public void write(OutputStream sos) throws IOException {
		ImageIO.write(this.buffImg, "png", sos);
		sos.close();
	}

	public BufferedImage getBuffImg() {
		return this.buffImg;
	}

	public String getCode() {
		return this.code;
	}
}