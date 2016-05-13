package com.ds.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Random;

import javax.imageio.ImageIO;
/** 
 * 验证码生成器 
 * @author dsna 
 * 
 */  
public class ValidateCode implements Serializable{  
    // 图片的宽度�??  
    private int width = 160;  
    // 图片的高度�??  
    private int height = 40;  
    // 验证码字符个�?  
    private int codeCount = 5;  
    // 验证码干扰线�?  
    private int lineCount = 150;  
  
    // 验证码图片Buffer  
    private BufferedImage buffImg=null;  
    
    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public char[] getCodeSequence() {
		return codeSequence;
	}

	public void setCodeSequence(char[] codeSequence) {
		this.codeSequence = codeSequence;
	}

	public void setBuffImg(BufferedImage buffImg) {
		this.buffImg = buffImg;
	}
	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
            'K', 'L', 'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
            'X', 'Y', 'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
	
	private char[] sampleCodeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  
    
    /** 
     *  
     * @param width 图片�? 
     * @param height 图片�? 
     * @param codeCount 字符个数 
     * @param lineCount 干扰线条�? 
     */  
    public  ValidateCode(int width,int height,int codeCount,int lineCount) {  
        this.width=width;  
        this.height=height;  
        this.codeCount=codeCount;  
        this.lineCount=lineCount;  
    }
    
    public ValidateCode(){}
    
    public String randomCode(){
    	Random random = new Random();  
    	StringBuffer randomCode = new StringBuffer();
    	for(int i=0; i<codeCount; ++i)
    		randomCode.append(String.valueOf(codeSequence[random.nextInt(codeSequence.length)]));
    	return randomCode.toString();
    }
    
    public String randomSampleCode(){
    	final int sampleCodeCount = 6;
    	Random random = new Random();  
    	StringBuffer randomCode = new StringBuffer();
    	for(int i=0; i<sampleCodeCount; ++i)
    		randomCode.append(String.valueOf(sampleCodeSequence[random.nextInt(sampleCodeSequence.length)]));
    	return randomCode.toString();
    }
      
    public void createCode(String toImageCode) {  
        int x = 0,fontHeight=0,codeY=0;  
        int red = 0, green = 0, blue = 0;  
          
        x = width / (codeCount +2);//每个字符的宽�?  
        fontHeight = height - 2;//字体的高�?  
        codeY = height - 4;  
          
        // 图像buffer  
        buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
        Graphics2D g = buffImg.createGraphics();  
        // 生成随机�?  
        Random random = new Random();  
        // 将图像填充为白色  
        g.setColor(Color.WHITE);  
        g.fillRect(0, 0, width, height);  
        // 创建字体  
        ImgFontByte imgFont=new ImgFontByte();  
        Font font =imgFont.getFont(fontHeight);  
        g.setFont(font);  
          
        for (int i = 0; i < lineCount; i++) {  
            int xs = random.nextInt(width);  
            int ys = random.nextInt(height);  
            int xe = xs+random.nextInt(width/8);  
            int ye = ys+random.nextInt(height/8);  
            red = random.nextInt(255);  
            green = random.nextInt(255);  
            blue = random.nextInt(255);  
            g.setColor(new Color(red, green, blue));  
            g.drawLine(xs, ys, xe, ye);  
        }  
          
        // 随机产生codeCount个字符的验证码�??  
        for (int i = 0; i < toImageCode.length(); i++) {  
            String strRand = String.valueOf(toImageCode.charAt(i));  
            // 产生随机的颜色�?�，让输出的每个字符的颜色�?�都将不同�??  
            red = random.nextInt(255);  
            green = random.nextInt(255);  
            blue = random.nextInt(255);  
            g.setColor(new Color(red, green, blue));  
            g.drawString(strRand, (i + 1) * x, codeY);  
        }  
       
    }  
      
    public void write(String path) throws IOException {  
        OutputStream sos = new FileOutputStream(path);  
            this.write(sos);  
    }  
      
    public void write(OutputStream sos) throws IOException {  
            ImageIO.write(buffImg, "png", sos);  
            sos.close();  
    }  
}  
