package italo.siserp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;

import italo.siserp.exception.FalhaCarregamentoArquivoException;
import italo.siserp.exception.FalhaCriacaoArquivoException;
import italo.siserp.exception.FalhaGravacaoArquivoException;
import italo.siserp.service.request.SaveSedeRequest;
import italo.siserp.service.response.SedeResponse;

@Service
public class SedeService {
		
	private final String arquivoConfig = "config.properties";
	
	public void salvaSede( SaveSedeRequest request )
			throws FalhaCriacaoArquivoException, 
				FalhaCarregamentoArquivoException, 
				FalhaGravacaoArquivoException {
		Properties p = new Properties();
		File f = new File( arquivoConfig );
		if ( !f.exists() ) {
			try {
				f.createNewFile();
			} catch ( IOException e ) {
				FalhaCriacaoArquivoException ex = new FalhaCriacaoArquivoException();
				ex.setParams( arquivoConfig ); 
				throw ex;
			}
		}
		
		try {
			p.load( new FileInputStream( f ) ) ;			
		} catch (IOException e) {
			FalhaCarregamentoArquivoException ex = new FalhaCarregamentoArquivoException();
			ex.setParams( arquivoConfig ); 
			throw ex;
		}
		
		try {
			p.setProperty( "cnpj", request.getCnpj() );
			p.setProperty( "inscricaoEstadual", request.getInscricaoEstadual() );
			p.store( new FileOutputStream( f ), "" );
		} catch ( IOException e ) {
			FalhaGravacaoArquivoException ex = new FalhaGravacaoArquivoException();
			ex.setParams( arquivoConfig ); 
			throw ex;
		}
	}
		
	public SedeResponse getSede() throws FalhaCarregamentoArquivoException {
		Properties p = new Properties();
		try {
			p.load( new FileInputStream( arquivoConfig ) ) ;			
		} catch (IOException e) {
			FalhaCarregamentoArquivoException ex = new FalhaCarregamentoArquivoException();
			ex.setParams( arquivoConfig ); 
			throw ex;
		}
		
		
		SedeResponse resp = new SedeResponse();
		resp.setCnpj( p.getProperty( "cnpj" ) );
		resp.setInscricaoEstadual( p.getProperty( "inscricaoEstadual" ) ); 
		return resp;
	}
		
	
}
