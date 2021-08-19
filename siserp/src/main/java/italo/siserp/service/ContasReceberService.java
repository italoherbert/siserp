package italo.siserp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ContasReceberBuilder;
import italo.siserp.exception.DataIniAposDataFimException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.model.Venda;
import italo.siserp.repository.VendaRepository;
import italo.siserp.service.request.BuscaContasReceberRequest;
import italo.siserp.service.response.ContasReceberResponse;
import italo.siserp.util.DataUtil;

@Service
public class ContasReceberService  {
	
	@Autowired
	private VendaRepository vendaRepository;
		
	@Autowired
	private ContasReceberBuilder contasReceberBuilder;
		
	@Autowired
	private DataUtil dataUtil;
				
	public ContasReceberResponse filtra( BuscaContasReceberRequest request ) 
				throws DataIniInvalidaException,
					DataFimInvalidaException, 
					DataIniAposDataFimException {
		
		Date dataIni, dataFim;
		try {
			dataIni = dataUtil.stringParaData( request.getDataIni() );
		} catch ( ParseException e ) {
			DataIniInvalidaException ex = new DataIniInvalidaException();
			ex.setParams( request.getDataIni() ); 
			throw ex;
		}
		try {
			dataFim = dataUtil.stringParaData( request.getDataFim() );
		} catch ( ParseException e ) {
			DataFimInvalidaException ex = new DataFimInvalidaException();
			ex.setParams( request.getDataFim() ); 
			throw ex;
		}
		
		if ( dataIni.after( dataFim ) )
			throw new DataIniAposDataFimException();
		
		boolean incluirPagas = request.getIncluirPagas().equals( "true" );
		boolean incluirCliente = request.getIncluirCliente().equals( "true" );
				
		List<Venda> vendas;
		if ( incluirCliente ) {
			String nomeIni = (request.getClienteNomeIni().equals( "*" ) ? "" : request.getClienteNomeIni() );
			vendas = vendaRepository.filtra( dataIni, dataFim, nomeIni+"%", incluirPagas );
		} else {
			vendas = vendaRepository.filtraSemCliente( dataIni, dataFim, incluirPagas );
		}
		
		Double totalCompleto = vendaRepository.calculaContasReceberTotalCompleto();
		if ( totalCompleto == null )
			totalCompleto = 0d;
		
		ContasReceberResponse resp = contasReceberBuilder.novoContasReceberResponse();
		contasReceberBuilder.carregaContasReceberResponse( resp, vendas, totalCompleto, incluirPagas );
		
		return resp;
	}
	
}
