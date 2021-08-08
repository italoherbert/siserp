package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import italo.siserp.exception.FuncionarioNaoEncontradoException;
import italo.siserp.exception.PessoaJaExisteException;
import italo.siserp.exception.UsuarioJaExisteException;
import italo.siserp.exception.UsuarioTipoInvalidoException;
import italo.siserp.model.request.BuscaFuncionariosRequest;
import italo.siserp.model.request.SaveFuncionarioRequest;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.FuncionarioResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.service.FuncionarioService;

@RestController
@RequestMapping(value="/api/funcionario")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveFuncionarioRequest request ) {				
		if ( request.getUsuario().getUsername() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( request.getUsuario().getUsername().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		
		if ( request.getUsuario().getPassword() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( request.getUsuario().getPassword().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		
		if ( request.getUsuario().getTipo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_INVALIDO ) );		
		
		try {
			IdResponse resp = funcionarioService.registraFuncionario( request );
			return ResponseEntity.ok( resp );
		} catch (PessoaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PESSOA_JA_EXISTE ) );		
		} catch (UsuarioJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );		
		} catch (UsuarioTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_INVALIDO ) );		
		}				
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveFuncionarioRequest request ) {	
		if ( request.getUsuario().getUsername() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( request.getUsuario().getUsername().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		
		if ( request.getUsuario().getPassword() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		if ( request.getUsuario().getPassword().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PASSWORD_OBRIGATORIO ) );
		
		if ( request.getUsuario().getTipo() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_INVALIDO ) );		
		
		try {
			funcionarioService.atualizaFuncionario( id, request );
			return ResponseEntity.ok().build();
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );		
		} catch (PessoaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PESSOA_JA_EXISTE ) );		
		} catch (UsuarioJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );		
		} catch (UsuarioTipoInvalidoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_TIPO_INVALIDO ) );		
		}								
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@PostMapping("/filtra")
	public ResponseEntity<Object> buscaFuncionarios( @RequestBody BuscaFuncionariosRequest request ) {		
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getUsernameIni().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		if ( request.getUsernameIni().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USERNAME_OBRIGATORIO ) );
		
		List<FuncionarioResponse> funcs = funcionarioService.buscaFuncionariosPorNomeIni( request );
		return ResponseEntity.ok( funcs );		
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaFuncionarioPorId( @PathVariable Long id ) {		
		try {
			FuncionarioResponse func = funcionarioService.buscaFuncionarioPorId( id );
			return ResponseEntity.ok( func );
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );		
		}
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaFuncionario( @PathVariable Long id ) {		
		try {
			funcionarioService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (FuncionarioNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.FUNCIONARIO_NAO_ENCONTRADO ) );		
		}
	}
	
}
