package italo.siserp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ContaPagarBuilder;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.ParcelaNaoEncontradaException;
import italo.siserp.model.CompraParcela;
import italo.siserp.model.request.BuscaContasPagarRequest;
import italo.siserp.model.request.PagamentoParcelaRequest;
import italo.siserp.model.response.ContaPagarResponse;
import italo.siserp.repository.CompraParcelaRepository;
import italo.siserp.util.DataUtil;

@Service
public class ContaPagarService {

	@Autowired
	private CompraParcelaRepository compraParcelaRepository;
	
	@Autowired
	private ContaPagarBuilder contaPagarBuilder;
	
	@Autowired
	private DataUtil dataUtil;
	
	public void alteraParcelaSituacao( Long parcelaId, PagamentoParcelaRequest request ) throws ParcelaNaoEncontradaException {
		CompraParcela p = compraParcelaRepository.findById( parcelaId ).orElseThrow( ParcelaNaoEncontradaException::new );		
		p.setPaga( request.getPaga().equals( "true" ) );
		
		compraParcelaRepository.save( p );
	}
	
	public List<ContaPagarResponse> filtra( BuscaContasPagarRequest request ) 
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
		
		
		List<CompraParcela> parcelas = compraParcelaRepository.filtra( dataIni, dataFim );
		List<ContaPagarResponse> lista = new ArrayList<>();
		for( CompraParcela p : parcelas ) {
			ContaPagarResponse resp = contaPagarBuilder.novoContaPagarResponse();
			contaPagarBuilder.carregaContaPagarResponse( resp, p );
			
			lista.add( resp );
		}
		
		return lista;
	}
	
}
