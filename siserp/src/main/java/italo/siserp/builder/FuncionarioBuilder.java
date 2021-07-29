package italo.siserp.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import italo.siserp.model.Endereco;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Pessoa;
import italo.siserp.model.Usuario;
import italo.siserp.model.request.SaveFuncionarioRequest;
import italo.siserp.model.response.EnderecoResponse;
import italo.siserp.model.response.FuncionarioResponse;
import italo.siserp.model.response.PessoaResponse;
import italo.siserp.model.response.UsuarioResponse;

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
		resp.setUsuario( new UsuarioResponse() );
		resp.setPessoa( new PessoaResponse() );
		resp.getPessoa().setEndereco( new EnderecoResponse() );
		return resp;
	}
	
	public Funcionario novoFuncionario() {
		Funcionario f = new Funcionario();
		f.setPessoa( new Pessoa() );
		f.setUsuario( new Usuario() );
		
		f.getPessoa().setEndereco( new Endereco() );
		return f;
	}
			
}
