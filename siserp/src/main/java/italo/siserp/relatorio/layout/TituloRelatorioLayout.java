package italo.siserp.relatorio.layout;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

@Component
public class TituloRelatorioLayout {

	private final Font tituloFont = new Font( Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD );
		
	public void build( Document doc, String titulo ) throws DocumentException {
		Paragraph tituloP = new Paragraph();
		tituloP.setFont( tituloFont ); 
		tituloP.setAlignment( Paragraph.ALIGN_CENTER );
		tituloP.add( titulo );
		
		doc.add( new Paragraph("\n") );
		doc.add( tituloP );
		doc.add( new Paragraph("\n") );
	}
	
}
