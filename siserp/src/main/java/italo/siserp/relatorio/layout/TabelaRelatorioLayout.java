package italo.siserp.relatorio.layout;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;

@Component
public class TabelaRelatorioLayout {

	private final BaseColor tabelaHeaderColor = new BaseColor( 200, 200, 120 );
	private final Font tabelaHeaderFont = new Font( Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL );
			
	public void build( Document doc, String[] cols, String[][] dados, float[] colsLargs, BaseColor[] colunasCores ) throws DocumentException {				
		int ncols = cols.length;
		
		PdfPTable table = new PdfPTable( ncols );
		table.setWidthPercentage( 100 );
		
		table.setWidths( colsLargs );		
						
		for( String col : cols ) {
			PdfPCell celula = new PdfPCell( new Phrase( col, tabelaHeaderFont ) );
			celula.setPaddingBottom( 4 ); 
			celula.setBorder( PdfPCell.NO_BORDER );
			celula.setBackgroundColor( tabelaHeaderColor );
			table.addCell( celula );
		}
						
		for( String[] linha : dados ) {
			int j = 0;
			for( String dado : linha ) {			
				Font f = new Font( Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, colunasCores[ j ] );

				PdfPCell celula = new PdfPCell( new Phrase( dado, f ) );
				celula.setBorder( PdfPCell.NO_BORDER );
				table.addCell( celula );
				
				j++;
			}
		}
					
		doc.add( table );
		doc.add( new LineSeparator() );
	}
	
}
