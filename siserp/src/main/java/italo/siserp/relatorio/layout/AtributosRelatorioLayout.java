package italo.siserp.relatorio.layout;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

@Component
public class AtributosRelatorioLayout {

	private final Font atrfont = new Font( Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, BaseColor.BLACK ); 
		
	public void build( Document doc, String[] atrs, String[] valores, BaseColor[] vcor ) throws DocumentException {		
		PdfPTable table = new PdfPTable( 2 );
		table.setHorizontalAlignment( Element.ALIGN_LEFT );
		table.setWidthPercentage( 50 ); 
		
		for( int i = 0; i < atrs.length; i++ ) {
			Font valfont = new Font( Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL, vcor[ i ] );
			
			PdfPCell c1 = new PdfPCell( new Phrase( atrs[i], atrfont ) );
			c1.setBorder( PdfPCell.NO_BORDER );
			c1.setHorizontalAlignment( Element.ALIGN_RIGHT );
			table.addCell( c1 );
			
			PdfPCell c2 = new PdfPCell( new Phrase( valores[ i ], valfont ) ); 
			c2.setBorder( PdfPCell.NO_BORDER );
			c2.setHorizontalAlignment( Element.ALIGN_LEFT );
			table.addCell( c2 ); 
		}
		
		doc.add( table );
	}
	
}
