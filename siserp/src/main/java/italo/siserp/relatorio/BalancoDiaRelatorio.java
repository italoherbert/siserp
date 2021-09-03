package italo.siserp.relatorio;

import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import italo.siserp.exception.GeracaoRelatorioException;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.relatorio.layout.AtributosRelatorioLayout;
import italo.siserp.relatorio.layout.BordaRelatorioLayout;
import italo.siserp.relatorio.layout.TabelaRelatorioLayout;
import italo.siserp.relatorio.layout.TituloRelatorioLayout;
import italo.siserp.relatorio.to.BalancoDiaRelatorioTO;

@Component
public class BalancoDiaRelatorio {
	
	private final String[] tabelaColunas = {
		"Funcionario", "Data de abertura", "Crédito", "Débito", "Saldo" 	
	};
	
	private final float[] tabelaColunasLarguras = {
		30, 25, 15, 15, 15	
	};
	
	private final BaseColor[] tabelaColunasDadosCores = {
		BaseColor.BLACK, BaseColor.DARK_GRAY, BaseColor.BLUE, BaseColor.RED, 
		new BaseColor( 100, 100, 255 )
	};
	
	private final String[] atributos = {
		"Crédito Total: ", "Débito Total: ", "Saldo Total: "	
	};
	
	private final BaseColor[] valoresCores = {
		BaseColor.BLUE, BaseColor.RED, new BaseColor( 100, 100, 255 )
	};
	
	private final String titulo = "Relatório do dia";
	
	@Autowired
	private BordaRelatorioLayout bordaRelatorioLayout;
	
	@Autowired
	private TituloRelatorioLayout tituloRelatorioLayout;
	
	@Autowired
	private TabelaRelatorioLayout tabelaRelatorioLayout;
	
	@Autowired
	private AtributosRelatorioLayout atributoRelatorioLayout;
	
	public void constroiEEnvia( OutputStream out, BalancoDiaRelatorioTO to ) throws GeracaoRelatorioException {		
		List<CaixaBalancoResponse> balancos = to.getBalancosPorCaixa();

		String[][] dados = new String[ balancos.size() ][ tabelaColunas.length ]; 
		
		int i = 0;
		for( CaixaBalancoResponse resp : balancos ) {
			String[] nomes = resp.getFuncionarioNome().split( "\\s" );
			
			String nome = "";
			if ( nomes.length > 0 ) {
				nome += nomes[ 0 ];
				if ( nomes.length > 1 )
					nome += " "+nomes[ 1 ];
			}
			
			dados[ i ][ 0 ] = nome;
			dados[ i ][ 1 ] =  resp.getDataAbertura();
			dados[ i ][ 2 ] =  resp.getCredito();
			dados[ i ][ 3 ] =  resp.getDebito();
			dados[ i ][ 4 ] =  resp.getSaldo();				
			i++;
		}
		
		
		Rectangle layout = bordaRelatorioLayout.buildBordaLayout( 6, BaseColor.DARK_GRAY );
		
		Document doc = new Document( layout );
		try {
			PdfWriter.getInstance( doc, out );
			doc.open();
			
			tituloRelatorioLayout.build( doc, titulo );			 									
			doc.add( new Paragraph( "\n" ) );			
			tabelaRelatorioLayout.build( doc, tabelaColunas, dados, tabelaColunasLarguras, tabelaColunasDadosCores ); 
			doc.add( new Paragraph( "\n" ) );			
			
			String[] valores = {
				to.getCreditoTotal(), to.getDebitoTotal(), to.getSaldoTotal() 	
			};						
			atributoRelatorioLayout.build( doc, atributos, valores, valoresCores );
			
			doc.close();
		} catch ( DocumentException e) {
			throw new GeracaoRelatorioException();
		} 
	}		

}
