package italo.siserp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import italo.siserp.builder.PermissaoGrupoBuilder;
import italo.siserp.exception.PermissaoEscritaException;
import italo.siserp.exception.PermissaoGrupoNaoEncontradoException;
import italo.siserp.exception.PermissaoLeituraException;
import italo.siserp.exception.PermissaoRemocaoException;
import italo.siserp.model.PermissaoGrupo;
import italo.siserp.model.request.SavePermissaoGrupoRequest;
import italo.siserp.repository.PermissaoGrupoRepository;

@Service
public class PermissaoGrupoService {
	
	@Autowired
	private PermissaoGrupoRepository permissaoGrupoRepository;
	
	@Autowired
	private PermissaoGrupoBuilder permissaoGrupoBuilder;
	
	public void salvaPermissaoGrupo( Long permissoesId, SavePermissaoGrupoRequest request ) 
			throws PermissaoGrupoNaoEncontradoException, 
				PermissaoLeituraException, 
				PermissaoEscritaException,
				PermissaoRemocaoException {
		
		PermissaoGrupo pg = permissaoGrupoRepository.findById( permissoesId ).orElseThrow( PermissaoGrupoNaoEncontradoException::new );
		permissaoGrupoBuilder.carregaPermissaoGrupo( pg, request );
		
		permissaoGrupoRepository.save( pg );		
	}
	
}
