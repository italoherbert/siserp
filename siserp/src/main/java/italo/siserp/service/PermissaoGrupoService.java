package italo.siserp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.PermissaoGrupoBuilder;
import italo.siserp.exception.PermissaoEscritaException;
import italo.siserp.exception.PermissaoGrupoNaoEncontradoException;
import italo.siserp.exception.PermissaoLeituraException;
import italo.siserp.exception.PermissaoRemocaoException;
import italo.siserp.exception.PermissaoTipoInvalidoException;
import italo.siserp.model.PermissaoGrupo;
import italo.siserp.model.request.SavePermissaoGrupoRequest;
import italo.siserp.model.request.SavePermissaoRequest;
import italo.siserp.repository.PermissaoGrupoRepository;

@Service
public class PermissaoGrupoService {
	
	@Autowired
	private PermissaoGrupoRepository permissaoGrupoRepository;
	
	@Autowired
	private PermissaoGrupoBuilder permissaoGrupoBuilder;
	
	public void salvaPermissao( Long permissaoGrupoId, SavePermissaoRequest request )
			throws PermissaoGrupoNaoEncontradoException, 
				PermissaoLeituraException, 
				PermissaoEscritaException,
				PermissaoRemocaoException,
				PermissaoTipoInvalidoException {	
		
		PermissaoGrupo pg = permissaoGrupoRepository.findById( permissaoGrupoId ).orElseThrow( PermissaoGrupoNaoEncontradoException::new );
		permissaoGrupoBuilder.carregaPermissao( pg, request );
		
		permissaoGrupoRepository.save( pg );
	}
	
	public void salvaPermissaoGrupo( Long permissaoGrupoId, SavePermissaoGrupoRequest request ) 
			throws PermissaoGrupoNaoEncontradoException, 
				PermissaoLeituraException, 
				PermissaoEscritaException,
				PermissaoRemocaoException {
		
		PermissaoGrupo pg = permissaoGrupoRepository.findById( permissaoGrupoId ).orElseThrow( PermissaoGrupoNaoEncontradoException::new );
		permissaoGrupoBuilder.carregaPermissaoGrupo( pg, request );
		
		permissaoGrupoRepository.save( pg );		
	}
	
}
