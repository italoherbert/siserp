package italo.siserp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.ContasReceberBuilder;
import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.DataFimAposDataIniException;
import italo.siserp.exception.DataFimInvalidaException;
import italo.siserp.exception.DataIniInvalidaException;
import italo.siserp.exception.DoubleInvalidoException;
import italo.siserp.exception.ValorRecebidoInvalidoException;
import italo.siserp.model.Cliente;
import italo.siserp.model.Venda;
import italo.siserp.model.request.BuscaContasReceberRequest;
import italo.siserp.model.request.ValorRecebidoRequest;
import italo.siserp.model.response.ContasReceberResponse;
import italo.siserp.repository.ClienteRepository;
import italo.siserp.repository.VendaRepository;
import italo.siserp.util.DataUtil;
import italo.siserp.util.NumeroUtil;

@Service
public class ContasReceberService  {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private VendaRepository vendaRepository;
		
	@Autowired
	private ContasReceberBuilder contasReceberBuilder;
		
	@Autowired
	private DataUtil dataUtil;
	
	@Autowired
	private NumeroUtil numeroUtil;
	
	@Transactional
	public void efetuaRecebimento( Long clienteId, ValorRecebidoRequest request ) 
			throws ClienteNaoEncontradoException, ValorRecebidoInvalidoException {
		
		Cliente c = clienteRepository.findById( clienteId ).orElseThrow( ClienteNaoEncontradoException::new );		
		
		double valor;
		try {
			valor = numeroUtil.stringParaDouble( request.getValor() );
		} catch (DoubleInvalidoException e) {
			throw new ValorRecebidoInvalidoException();
		} 
	
		double debito = valor;
		
		List<Venda> vendas = c.getVendas();
		int size = vendas.size();
		for( int i = 0; debito > 0 && i < size; i++ ) {
			Venda v = vendas.get( i );
			
			if( v.getDebito() <= 0 )
				continue;
			
			if ( valor > v.getDebito() ) {
				debito -= v.getDebito();
				v.setDebito( 0 );
			} else {
				v.setDebito( v.getDebito() - debito );
				debito = 0;
			}
			vendaRepository.save( v );
		}		
	}
	
	public ContasReceberResponse filtra( BuscaContasReceberRequest request ) 
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
		
		boolean incluirCliente = request.getIncluirCliente().equals( "true" );
				
		List<Venda> vendas;
		if ( incluirCliente ) {
			String nomeIni = (request.getClienteNomeIni().equals( "*" ) ? "" : request.getClienteNomeIni() );
			vendas = vendaRepository.filtra( dataIni, dataFim, nomeIni+"%" );
		} else {
			vendas = vendaRepository.filtraSemCliente( dataIni, dataFim );
		}
		
		Double totalCompleto = vendaRepository.calculaContasReceberTotalCompleto();
		if ( totalCompleto == null )
			totalCompleto = 0d;
		
		ContasReceberResponse resp = contasReceberBuilder.novoContasReceberResponse();
		contasReceberBuilder.carregaContasReceberResponse( resp, vendas, totalCompleto );
		
		return resp;
	}
	
}
