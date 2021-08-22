package italo.siserp.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImagemUtil {
	
	public BufferedImage base64ToImage( String base64Image ) throws IOException {		
		String dados = base64Image.substring( base64Image.indexOf( "," )+1, base64Image.length() );
		byte[] bytes = Base64.getDecoder().decode( dados );
		return ImageIO.read( new ByteArrayInputStream( bytes ) );
	}
	
	public String imageToBase64( BufferedImage image ) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write( image, "png", out );
		return "data:image/png;base64,"+Base64.getEncoder().encodeToString( out.toByteArray() ); 
	}
	
	public BufferedImage ajustaImagem( BufferedImage imagem, int altura ) {
		int w = imagem.getWidth();
		int h = imagem.getHeight();
		
		int novoW = w;
		int novoH = h;
		
		if ( h > altura ) {
			double f = (double)altura / (double)h;
					
			novoW = (int)( w * f );
			novoH = (int)( h * f );
		}
				
		BufferedImage image = new BufferedImage( novoW, novoH, BufferedImage.TYPE_INT_ARGB );
		Graphics g2 = image.getGraphics();
		((Graphics2D)g2).setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		g2.drawImage( imagem, 0, 0, novoW, novoH, null );
		g2.dispose();
		return image;
	}
	
}
