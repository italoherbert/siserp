package italo.siserp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.PermissaoGrupoBuilder;
import italo.siserp.builder.RecursoBuilder;
import italo.siserp.exception.RecursoJaExisteException;
import italo.siserp.exception.RecursoNaoEncontradoException;
import italo.siserp.model.PermissaoGrupo;
import italo.siserp.model.Recurso;
import italo.siserp.model.UsuarioGrupo;
import italo.siserp.model.request.BuscaRecursosRequest;
import italo.siserp.model.request.SaveRecursoRequest;
import italo.siserp.model.response.RecursoResponse;
import italo.siserp.repository.PermissaoGrupoRepository;
import italo.siserp.repository.RecursoRepository;
import italo.siserp.repository.UsuarioGrupoRepository;

@Service
public class RecursoService {

	@Autowired
	private UsuarioGrupoRepository usuarioGrupoRepository;
	
	@Autowired
	private PermissaoGrupoRepository permissaoGrupoRepository;
	
	@Autowired
	private RecursoRepository recursoRepository;
	
	@Autowired
	private RecursoBuilder recursoBuilder;
	
	@Autowired
	private PermissaoGrupoBuilder permissaoGrupoBuilder;
	
	@Transactional
	public void registraRecurso( SaveRecursoRequest request ) throws RecursoJaExisteException {
		Optional<Recurso> rop = recursoRepository.buscaPorNome( request.getNome() );
		if ( rop.isPresent() )
			throw new RecursoJaExisteException();
		
		Recurso r = recursoBuilder.novoRecurso();
		recursoBuilder.carregaRecurso( r, request );
		
		recursoRepository.save( r );				
		
		List<UsuarioGrupo> grupos = usuarioGrupoRepository.findAll();
		for( UsuarioGrupo g : grupos ) {
			PermissaoGrupo pg = permissaoGrupoBuilder.novoINIPermissaoGrupo( g, r );
			permissaoGrupoRepository.save( pg );
		}
	}
	
	public void alteraRecurso( Long recursoId, SaveRecursoRequest request ) throws RecursoJaExisteException, RecursoNaoEncontradoException {
		Recurso r = recursoRepository.findById( recursoId ).orElseThrow( RecursoNaoEncontradoException::new );
		
		String nome = request.getNome();
		
		if ( !nome.equalsIgnoreCase( r.getNome() ) )
			if ( recursoRepository.buscaPorNome( nome ).isPresent() )
				throw new RecursoJaExisteException();
		
		recursoBuilder.carregaRecurso( r, request );		
		recursoRepository.save( r );		
	}
	
	public List<RecursoResponse> filtraGrupos( BuscaRecursosRequest request ) {
		String nomeIni = request.getNomeIni();
		if ( nomeIni.equals( "*" ) )
			nomeIni = "";
		nomeIni += "%";
		
		List<Recurso> recursos = recursoRepository.filtra( nomeIni );
		
		List<RecursoResponse> lista = new ArrayList<>();
		for( Recurso r : recursos ) {
			RecursoResponse resp = recursoBuilder.novoRecursoResponse();
			recursoBuilder.carregaRecursoResponse( resp, r );
			
			lista.add( resp );
		}
		
		return lista;
	}
	
	public RecursoResponse buscaGrupo( Long recursoId ) throws RecursoNaoEncontradoException {
		Recurso r = recursoRepository.findById( recursoId ).orElseThrow( RecursoNaoEncontradoException::new );
		
		RecursoResponse resp = recursoBuilder.novoRecursoResponse();
		recursoBuilder.carregaRecursoResponse( resp, r );
		
		return resp;
	}
	
	public void deletaRecurso( Long recursoId )  throws RecursoNaoEncontradoException {
		boolean existe = recursoRepository.existsById( recursoId );
		if ( !existe )
			throw new RecursoNaoEncontradoException();
		
		recursoRepository.deleteById( recursoId ); 
	}
	
	
}
