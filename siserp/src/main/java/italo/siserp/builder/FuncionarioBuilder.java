package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Funcionario;
import italo.siserp.model.request.SaveFuncionarioRequest;
import italo.siserp.model.response.FuncionarioResponse;

@Component
public class FuncionarioBuilder {

	@Autowired
	private PessoaBuilder pessoaBuilder;
	
	@Autowired
	private UsuarioBuilder usuarioBuilder;
			
	public void carregaFuncionario( Funcionario f, SaveFuncionarioRequest req ) {
		pessoaBuilder.carregaPessoa( f.getPessoa(), req.getPessoa() );
		usuarioBuilder.carregaUsuario( f.getUsuario(), req.getUsuario() );
	}
	
	public void carregaFuncionarioResponse( FuncionarioResponse resp, Funcionario f ) {
		resp.setId( f.getId() );		
		pessoaBuilder.carregaPessoaResponse( resp.getPessoa(), f.getPessoa() );
		usuarioBuilder.carregaUsuarioResponse( resp.getUsuario(), f.getUsuario() );
	}
	
	public FuncionarioResponse novoFuncionarioResponse() {		
		FuncionarioResponse resp = new FuncionarioResponse();
		resp.setUsuario( usuarioBuilder.novoUsuarioResponse() );
		resp.setPessoa( pessoaBuilder.novoPessoaResponse() );
		return resp;
	}
	
	public Funcionario novoFuncionario() {
		Funcionario f = new Funcionario();
		f.setPessoa( pessoaBuilder.novoPessoa() );
		f.setUsuario( usuarioBuilder.novoUsuario() );		
		return f;
	}
			
}
