package italo.siserp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.CaixaBalancoBuilder;
import italo.siserp.dao.CaixaDAO;
import italo.siserp.dao.bean.CaixaBalancoDAOTO;
import italo.siserp.exception.GeracaoRelatorioException;
import italo.siserp.model.Caixa;
import italo.siserp.relatorio.BalancoDiaRelatorio;
import italo.siserp.relatorio.to.BalancoDiaRelatorioTO;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.service.response.CaixaBalancoResponse;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Service
public class RelatorioService {
		
	@Autowired
	private CaixaRepository caixaRepository;
	
	@Autowired
	private CaixaDAO caixaDAO;
	
	@Autowired
	private CaixaBalancoBuilder caixaBalancoBuilder;
	
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
		
	@Autowired
	private BalancoDiaRelatorio balancoDiaRelatorio;
	
	public void geraRelatorioBalancoHoje( HttpServletResponse response ) throws GeracaoRelatorioException{
		Date hoje = dataUtil.apenasData( new Date() );
		List<Caixa> caixas = caixaRepository.listaPorDataDia( hoje );
		
		double debito = 0;
		double credito = 0;
		double saldo = 0;
			
		List<CaixaBalancoResponse> balancos = new ArrayList<>();
		for( Caixa c : caixas ) {
			CaixaBalancoDAOTO balanco = caixaDAO.geraCaixaBalanco( c );
			
			debito += balanco.getDebito();
			credito += balanco.getCredito();
			saldo += balanco.getSaldo();
			
			CaixaBalancoResponse resp = new CaixaBalancoResponse();
			caixaBalancoBuilder.carregaCaixaBalancoResponse( resp, balanco ); 
			
			resp.setDebito( numeroUtil.formatoReal( balanco.getDebito() ) );
			resp.setCredito( numeroUtil.formatoReal( balanco.getCredito() ) );
			resp.setSaldo( numeroUtil.formatoReal( balanco.getSaldo() ) );
			
			balancos.add( resp );
		}
									
		try {
			BalancoDiaRelatorioTO to = new BalancoDiaRelatorioTO();
			to.setBalancosPorCaixa( balancos );
			to.setDebitoTotal( numeroUtil.formatoReal( debito ) );
			to.setCreditoTotal( numeroUtil.formatoReal( credito ) );
			to.setSaldoTotal( numeroUtil.formatoReal( saldo ) );
			
			response.setContentType( "application/pdf");
			//response.setHeader( "Content-Disposition", "attachment; filename=\"relatorio.pdf\"" );
			
			balancoDiaRelatorio.constroiEEnvia( response.getOutputStream(), to );
			
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (GeracaoRelatorioException e) {
			throw e;
		} catch (IOException e) {
			throw new GeracaoRelatorioException();
		}
	}
	
}


/*	
public void geraRelatorio( HttpServletResponse response, JRDataSource jrds, String relatorioResourcePath ) 
		throws LeituraRecursoJasperException, 
			RelatorioRespostaOutputException,
			GeracaoRelatorioException {
	
	Resource r = resourceLoader.getResource( "classpath:"+relatorioResourcePath );
	
	InputStream in;
	try {
		in = r.getInputStream();
	} catch ( IOException e ) {
		throw new LeituraRecursoJasperException();
	}
	
	OutputStream out;
	try {
		out = response.getOutputStream();
	} catch ( IOException e ) {
		throw new RelatorioRespostaOutputException();
	}
				
	try {						
		response.setContentType( "application/pdf");
		//response.setHeader( "Content-Disposition", "attachment; filename=\"relatorio.pdf\"" );
					
		JasperPrint jp = JasperFillManager.fillReport( in, new HashMap<>(), jrds );
		JasperExportManager.exportReportToPdfStream( jp, out );					
	} catch (JRException e) {
		throw new GeracaoRelatorioException();
	}
}
*/

