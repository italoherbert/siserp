package italo.siserp.relatorio.layout;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

@Component
public class BordaRelatorioLayout {

	public Rectangle buildBordaLayout( int largura, BaseColor cor ) {
		Rectangle layout = new Rectangle( PageSize.A4 );
		layout.setBorderWidth( largura );
		layout.setBorder( Rectangle.BOX );
		layout.setBorderColor( cor ); 
		layout.setUseVariableBorders( false );
		
		return layout;
	}
	
}
