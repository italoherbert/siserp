package italo.siserp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import italo.siserp.exception.ClienteNaoEncontradoException;
import italo.siserp.exception.PessoaJaExisteException;
import italo.siserp.exception.UsuarioJaExisteException;
import italo.siserp.model.request.BuscaClientesRequest;
import italo.siserp.model.request.SaveClienteRequest;
import italo.siserp.model.response.ClienteResponse;
import italo.siserp.model.response.ErroResponse;
import italo.siserp.model.response.IdResponse;
import italo.siserp.service.ClienteService;

@RestController
@RequestMapping(value="/api/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@PostMapping("/registra")
	public ResponseEntity<Object> registra( @RequestBody SaveClienteRequest request ) {				
		if ( request.getPessoa().getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getPessoa().getNome().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		
		try {
			IdResponse resp = clienteService.registraCliente( request );
			return ResponseEntity.ok( resp );
		} catch (PessoaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PESSOA_JA_EXISTE ) );		
		} catch (UsuarioJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );		
		}				
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@PutMapping("/atualiza/{id}")
	public ResponseEntity<Object> atualiza( @PathVariable Long id, @RequestBody SaveClienteRequest request ) {	
		if ( request.getPessoa().getNome() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getPessoa().getNome().trim().isEmpty() )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		
		try {
			clienteService.atualizaCliente( id, request );
			return ResponseEntity.ok().build();
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );		
		} catch (PessoaJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.PESSOA_JA_EXISTE ) );		
		} catch (UsuarioJaExisteException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.USUARIO_JA_EXISTE ) );		
		}				
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@PostMapping("/filtra")
	public ResponseEntity<Object> buscaClientes( @RequestBody BuscaClientesRequest request ) {		
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
		
		Pageable p = Pageable.unpaged();
		
		List<ClienteResponse> clientes = clienteService.filtra( request, p );
		return ResponseEntity.ok( clientes );		
	}

	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")
	@PostMapping("/filtra/limit/{limit}")
	public ResponseEntity<Object> buscaCategorias( @PathVariable Integer limit, @RequestBody BuscaClientesRequest request ) {		
		if ( request.getNomeIni() == null )
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.NOME_OBRIGATORIO ) );
				
		Pageable p = PageRequest.of( 0, limit );
				
		List<ClienteResponse> clientes = clienteService.filtra( request, p );
		return ResponseEntity.ok( clientes );		
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE', 'CAIXA')")	
	@GetMapping("/get/{id}")
	public ResponseEntity<Object> buscaClientePorId( @PathVariable Long id ) {		
		try {
			ClienteResponse func = clienteService.buscaClientePorId( id );
			return ResponseEntity.ok( func );
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );		
		}
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
	@DeleteMapping("/deleta/{id}")
	public ResponseEntity<Object> deletaCliente( @PathVariable Long id ) {		
		try {
			clienteService.deleta( id );
			return ResponseEntity.ok().build();
		} catch (ClienteNaoEncontradoException e) {
			return ResponseEntity.badRequest().body( new ErroResponse( ErroResponse.CLIENTE_NAO_ENCONTRADO ) );		
		}
	}
		
}

