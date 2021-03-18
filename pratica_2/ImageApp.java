
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
  Gabriel Simonetto
  Lucas Verdade
  Lucas Zacchi
*/

public class ImageApp   {

	// Leitura da imagem
	public static BufferedImage loadImage(String surl) {
		BufferedImage bimg = null;
		try {
			URL url = new URL(surl);
			bimg = ImageIO.read(url);
			//bimg = ImageIO.read(new File("D:/Temp/mundo.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimg;
	}

	public void apresentaImagem(JFrame frame, BufferedImage img) {
		frame.setBounds(0, 0, img.getWidth(), img.getHeight());
		JImagePanel panel = new JImagePanel(img, 0, 0);
		frame.add(panel);
		frame.setVisible(true);
	}

	public static BufferedImage criaImagemRGB() {
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

		WritableRaster raster = img.getRaster();

		for(int h=0;h<img.getHeight();h++) //Percorre a horizontal
			for(int w=0;w<img.getWidth();w++) {//Percorre a vertical
				raster.setSample(w,h,0,220); // Componente Vermelho
				raster.setSample(w,h,1,219); // Componente Verde
				raster.setSample(w,h,2,97);  // Componente Azul
			}
		return img;
	}

	public static BufferedImage criaImagemCinza() {
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = img.getRaster();
		for(int h=0;h<img.getHeight();h++) //Percorre a horizontal
			for(int w=0;w<img.getWidth();w++) {//Percorre a vertical
				raster.setSample(w,h,0,h);//como o h = 0 e vai aumentando, cada linha vai ficando mais clara
			}
		return img;
	}

	public static BufferedImage criaImagemBinaria() {
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = img.getRaster();
		for(int h=0;h<img.getHeight();h++) //Percorre a horizontal
			for(int w=0;w<img.getWidth();w++) {//Percorre a vertical
				if (((h/50)+(w/50)) % 2 == 0)
					raster.setSample(w,h,0,0); // checkerboard pattern.
				else raster.setSample(w,h,0,1);
			}
		return img;
	}

	// Imprime valores dos pixeis de imagem RGB
	public static void  imprimePixeis(BufferedImage bufferedImage) {
		for(int h=0;h<bufferedImage.getHeight();h++) //Percorre a horizontal
			for(int w=0;w<bufferedImage.getWidth();w++) {//Percorre a vertical
				int rgb = bufferedImage.getRGB(w,h);
				int r = (int)((rgb&0x00FF0000)>>>16); // componente vermelho
				int g = (int)((rgb&0x0000FF00)>>>8); // componente verde
				int b = (int)(rgb&0x000000FF); //componente azul
				System.out.print("at ("+w+","+h+"): ");
				System.out.println(r+","+g+","+b);
			}
	}

	// 1. Reduza de 1/2 a resolução da imagem RGB.png e apresente esta imagem.

	private static int[] avrgPixel(int pixels[]) {
		int r = 0;
		int g = 0;
		int b = 0;

		int c = 0;

		for (int i = 0; i < pixels.length; ++i) {
			r += ((pixels[i]&0x00FF0000)>>>16); // componente vermelho
			g += ((pixels[i]&0x0000FF00)>>>8); // componente verde
			b += (pixels[i]&0x000000FF); //componente azul
			c++;
		}
		r /= c;
		g /= c;
		b /= c;
		return new int[]{r,g,b};
	}

	public static BufferedImage halfResImage(BufferedImage bufferedImage) {
		int newHeight = bufferedImage.getHeight()/2;
		int newWidth = bufferedImage.getWidth()/2;

		BufferedImage halfResImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

		WritableRaster raster = halfResImg.getRaster();

		for(int h=0; h<bufferedImage.getHeight(); h+=2) {
			for(int w=0; w<bufferedImage.getWidth(); w+=2) {
				int pos = 0;
				int pixels[] = new int[4];
				for(int i=h; i<h+2; ++i){
					for(int j=w; j<w+2; ++j){
						pixels[pos] = bufferedImage.getRGB(j,i);
						++pos;
					}
				}
				int[] rgb = avrgPixel(pixels);
				int newH = h/2;
				int newW = w/2;
				raster.setSample(newW,newH,0,rgb[0]); // Componente Vermelho
				raster.setSample(newW,newH,1,rgb[1]); // Componente Verde
				raster.setSample(newW,newH,2,rgb[2]);  // Componente Azul
			}
		}

		return halfResImg;
	}

	// 	2. Transforme a imagem RGB.png em tons de cinza e a apresente.
	public static BufferedImage greyScale(BufferedImage bufferedImage) {
		final int height = bufferedImage.getHeight();
		final int width = bufferedImage.getWidth();

		BufferedImage img = new BufferedImage(height, width, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = img.getRaster();

		// get pixel info
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				final int rgb = bufferedImage.getRGB(i,j);

				final int red = ((rgb&0x00FF0000)>>>16);
				final int green = ((rgb&0x0000FF00)>>>8);
				final int blue = (rgb&0x000000FF);

				// set pixel to grey-scale using equation
				final double greyPixel = 0.3 * red + 0.59 * green + 0.11 * blue;

				raster.setSample(i, j, 0, greyPixel);

			}
		}
		return img;
	}

	// 3. Transforme a imagem RGB.png em imagem binária e a apresenta.
	public static BufferedImage rgbToBinaryImage(BufferedImage bufferedImage) {
		int height = bufferedImage.getHeight();
		int width = bufferedImage.getWidth();

		BufferedImage img = new BufferedImage(height, width, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = img.getRaster();

		// get pixel info
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				int rgb = bufferedImage.getRGB(i,j);

				final int red =   (rgb&0x00FF0000)>>>16;
				final int green = (rgb&0x0000FF00)>>>8;
				final int blue =  (rgb&0x000000FF);

				// transforming rgb to grayScale first
				final double greyPixel = 0.3*red + 0.59*green +0.11*blue;

				// talvez não seja fixo em 127, ver se tem algo como getScale
				if (greyPixel >= 127) {
					raster.setSample(i, j, 0, 1); // white pixel
				} else {
					raster.setSample(i, j, 0, 0); // black pixel
				}
			}
		}
		return img;
	}

	/*4. Gere e apresente 3 imagens fazendo a função de split RGB sobre a imagem RGB.png:
		       ImagemVermelho, ImagemVerde e ImagemAzul. Todas as 3 imagens deverão ser tons de cinza.
		       No caso da ImagemVermelho, cada pixel representará a intensidade luminosa proporcional
		       ao componente de cor vermelho do pixel original. Para ImagemVerde,  cada pixel representará
		       a intensidade luminosa proporcional ao componente de cor verde do pixel original.
		       Mesma coisa para ImagemAzul.
	*/
	public static BufferedImage[] splitRGBImage(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		BufferedImage[] img = new BufferedImage[3];

		// for percorrendo cada um dos 3 canais de cor RGB
		for (int i = 0; i < 3; i++) {
			img[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			WritableRaster raster = img[i].getRaster();

			for (int h = 0; h < height; h++) {
				for (int w = 0; w < width; w++) {
					int rgb = bufferedImage.getRGB(w,h);

					final int red =   (rgb&0x00FF0000)>>>16;
					final int green = (rgb&0x0000FF00)>>>8;
					final int blue =  (rgb&0x000000FF);

					// calculating light intensity
					final double lightIntesityPixel = 0.3*red + 0.59*green +0.11*blue;

					// setting the pixel value in layer 'i'
					raster.setSample(w, h, i, lightIntesityPixel);
				}
			}
		}
		return img;
	}

	public static void main(String[] args) {
		ImageApp ia = new ImageApp();
		BufferedImage imgJPEG = loadImage("http://www.inf.ufsc.br/~willrich/INE5431/RGB.jpg");
		BufferedImage imgRGB = criaImagemRGB();
		BufferedImage imgCinza = criaImagemCinza();
		BufferedImage imgBinaria = criaImagemBinaria();


		ia.apresentaImagem(new JFrame("imgJPEG"), imgJPEG);
		ia.apresentaImagem(new JFrame("imgRGB"), imgRGB);
		ia.apresentaImagem(new JFrame("imgCinza"), imgCinza);
		ia.apresentaImagem(new JFrame("imgBinaria"), imgBinaria);


		BufferedImage greyScale = greyScale(imgJPEG);
		ia.apresentaImagem(new JFrame("greyScale"), greyScale);

		BufferedImage binaryImage = rgbToBinaryImage(imgJPEG);
		ia.apresentaImagem(new JFrame("binaryImage"), binaryImage);


		BufferedImage lowRes = halfResImage(imgJPEG);
		ia.apresentaImagem(new JFrame("lowRes"), lowRes);

		BufferedImage[] splitImage = splitRGBImage(imgJPEG);
		ia.apresentaImagem(new JFrame("splitRed"), splitImage[0]);
		ia.apresentaImagem(new JFrame("splitGreen"), splitImage[1]);
		ia.apresentaImagem(new JFrame("splitBlue"), splitImage[2]);

		imprimePixeis(imgJPEG);
	}
}
