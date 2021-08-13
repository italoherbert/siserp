package italo.siserp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ContasPagarBuilder;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.ParcelaNaoEncontradaException;
import italo.siserp.model.CompraParcela;
import italo.siserp.model.request.BuscaContasPagarRequest;
import italo.siserp.model.request.PagamentoParcelaRequest;
import italo.siserp.model.response.ContasPagarResponse;
import italo.siserp.repository.CompraParcelaRepository;
import italo.siserp.util.DataUtil;

@Service
public class ContasPagarService {

	@Autowired
	private CompraParcelaRepository compraParcelaRepository;
		
	@Autowired
	private ContasPagarBuilder contasPagarBuilder;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void alteraParcelaSituacao( Long parcelaId, PagamentoParcelaRequest request ) throws ParcelaNaoEncontradaException {
		CompraParcela p = compraParcelaRepository.findById( parcelaId ).orElseThrow( ParcelaNaoEncontradaException::new );		
		p.setPaga( request.getPaga().equals( "true" ) );
		
		compraParcelaRepository.save( p );
	}
	
	public ContasPagarResponse filtra( BuscaContasPagarRequest request ) 
				throws DataIniInvalidaException,
					DataFimInvalidaException, 
					DataFimAposDataIniException {
		
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
			throw new DataFimAposDataIniException();
		
		boolean incluirFornecedor = request.getIncluirFornecedor().equals( "true" );
		boolean incluirPagas = request.getIncluirPagas().equals( "true" );
		
		List<CompraParcela> parcelas;
		if ( incluirFornecedor ) {
			String nomeIni = (request.getFornecedorNomeIni().equals( "*" ) ? "" : request.getFornecedorNomeIni() );
			parcelas = compraParcelaRepository.filtra( dataIni, dataFim, nomeIni+"%", incluirPagas );
		} else {
			parcelas = compraParcelaRepository.filtraSemFornecedor( dataIni, dataFim, incluirPagas );
		}
		
		Double debitoTotalCompleto = compraParcelaRepository.calculaDebitoTotalCompleto();
		if ( debitoTotalCompleto == null )
			debitoTotalCompleto = 0d;
		
		ContasPagarResponse resp = contasPagarBuilder.novoContasPagarResponse();
		contasPagarBuilder.carregaContasPagarResponse( resp, parcelas, debitoTotalCompleto, incluirPagas );
				
		return resp;
	}
	
}
