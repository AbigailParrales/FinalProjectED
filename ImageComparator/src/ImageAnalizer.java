import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageAnalizer {
	/**
	 * This method reads the image provided by the given filename and returns the Buffered version of it
	 * @param fileName
	 * @return BufferedImage of fileName if successful and NIL if there was an error
	 * @author Jairo Salazar
	 */
	final public static BufferedImage readImage(String fileName ){
		try	{
			return ImageIO.read(new File(fileName));
		}
		catch (IOException e){
			return null;
		}
	}

	/**
	 * Saves the image given into a PNG format
	 * @param fileName
	 * @param internImage
	 * @return returns a 1 if the process was successful and returns a 0 if there was an error
	 * @author Jairo Salazar
	 */
	public int savePNG(String fileName, BufferedImage internImage){
		try{
			ImageIO.write(internImage, "PNG", new File(fileName));
		}
		catch (IOException e){
			return 0;
		}
		return 1;
	}

	/**
	 * Converts the image to GrayScale
	 * @param image
	 * @param newType
	 * @return BufferedImage of the received image in GrayScale
	 * @author Jairo Salazar
	 */
	final public static BufferedImage convertToGrayScale(BufferedImage image,int newType){
		try{
			BufferedImage raw_image = image;
			image =new BufferedImage(raw_image.getWidth(),raw_image.getHeight(),newType);
			ColorConvertOp xformOp = new ColorConvertOp(null);
			xformOp.filter(raw_image, image);
		}
		catch (Exception e){}
		return image;
	}

	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * @param srcImg - source image to scale
	 * @param w - desired width
	 * @param h - desired height
	 * @return - the new resized image
	 * https://stackoverflow.com/questions/16497853/scale-a-bufferedimage-the-fastest-and-easiest-way
	 */
	final public static BufferedImage resizeImage(BufferedImage src, int w, int h){
		int finalw = w;
		int finalh = h;
		double factor = 1.0d;
		if(src.getWidth() > src.getHeight()){
			factor = ((double)src.getHeight()/(double)src.getWidth());
			finalh = (int)(finalw * factor);                
		}
		else{
			factor = ((double)src.getWidth()/(double)src.getHeight());
			finalw = (int)(finalh * factor);
		}   

		BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(src, 0, 0, finalw, finalh, null);
		g2.dispose();
		return resizedImg;
	}
	
	/**
	 * Gets the RGB version of the pixel contained by the received image in the given coordinates
	 * @param rgbImage
	 * @param x
	 * @param y
	 * @return An array with three integers R,G,B
	 * @author Jairo Salazar
	 */
	public int[] getRGBPixel(BufferedImage rgbImage, int x, int y){
		int rgb = rgbImage.getRGB(x, y);
		return new int[] {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF,(rgb & 0xFF)};
	}

	/**
	 * Returns the integer value of the pixel contained by the received image in the given coordinates 
	 * @param grayImage
	 * @param x
	 * @param y
	 * @return An integer with the value of the pixel
	 * @author Jairo Salazar
	 */
	public int getGrayPixel(BufferedImage grayImage, int x, int y){		
		try	{
			WritableRaster wr = grayImage.getRaster();
			return wr.getSample(x, y, 0);
		}
		catch(Exception e){
			return 255;
		}
	}

	public static void main(String[] args) {
				//Crea el objeto de Computer Vision
				ImageAnalizer IA = new ImageAnalizer();
				
				//Lee una imagen
				BufferedImage rgbImage = IA.readImage("Gong Yoo.png");
				
				//Guarda una imagen a un archivo
				IA.savePNG( "copyGoong Yoo.png", rgbImage );
				
				//Obtiene e imprime las dimensiones de la imagen leída
				int W, H;
				W = rgbImage.getWidth();
				H = rgbImage.getHeight();
				System.out.println("W: " + W);
				System.out.println("H: " + H);
				
				//Transforma de RGB a escala de grises
				BufferedImage grayImage = IA.convertToGrayScale( rgbImage, BufferedImage.TYPE_BYTE_GRAY );
				IA.savePNG( "grayGoong Yoo.png", grayImage );
				System.out.println("grayGoong Yoo.png saved");
				
				//Cambia el tamaño de la imagen a la mitad
				BufferedImage resizedImage = IA.resizeImage(rgbImage,(int)(W*0.5),(int)(H*0.5));
				IA.savePNG( "resizedGoong Yoo.png", resizedImage );
				System.out.println("resizedGoong Yoo.png saved");
				
				//Tomar un pixel RGB
				int[] rgbVector = IA.getRGBPixel( rgbImage, 100, 100 );
				System.out.println( "rgbPixel-> r: " + rgbVector[0] + " g: " + rgbVector[1] + " b: " + rgbVector[2] );
				
				//Toma un pixel Gray
				int grayPixel = IA.getGrayPixel(grayImage, 150, 100);
				System.out.println( "grayPixel: " + grayPixel );
	}

}
