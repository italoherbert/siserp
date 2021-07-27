package italo.siserp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErroResponse {
		
	public final static int FORMATO_DE_DATA_INVALIDO = 1;
	public final static int FORMATO_DE_NUMERO_REAL_INVALIDO = 2;
	
	public final static int ID_OFFSET_BUSCA_FORMATO_INVALIDO = 3;
	public final static int MAX_RESULTS_BUSCA_FORMATO_INVALIDO = 4;
	
	public final static int SEM_PERMISSAO = 5;
	public final static int SEM_PERMISSAO_REG_USUARIO_RAIZ = 6;

	public final static int USUARIO_NAO_ENCONTRADO = 100;
	public final static int FUNCIONARIO_NAO_ENCONTRADO = 101;
	public final static int FORNECEDOR_NAO_ENCONTRADO = 102;	
	public final static int CATEGORIA_NAO_ENCONTRADA = 103;
	public final static int SUBCATEGORIA_NAO_ENCONTRADA = 104;
		
	public final static int PESSOA_JA_EXISTE = 200;
	public final static int USUARIO_JA_EXISTE = 201;
	public final static int FORNECEDOR_JA_EXISTE = 202;
	public final static int CATEGORIA_JA_EXISTE = 203;
	public final static int SUBCATEGORIA_JA_EXISTE = 204;
	
	public final static int USERNAME_OBRIGATORIO = 300;
	public final static int PASSWORD_OBRIGATORIO = 301;			
	public final static int NOME_OBRIGATORIO = 302;
	public final static int RAZAO_SOCIAL_OBRIGATORIA = 303;	
	public final static int CNPJ_OBRIGATORIO = 304;
	public final static int INSCRICAO_ESTADUAL_OBRIGATORIA = 305;
	public final static int FORNECEDOR_EMPRESA_OBRIGATORIA = 306;
	public final static int CATEGORIA_DESCRICAO_OBRIGATORIA = 307;
	public final static int SUBCATEGORIA_DESCRICAO_OBRIGATORIA = 308;

	public final static int USERNAME_PASSWORD_NAO_CORRESPONDEM = 500;
	public final static int USUARIO_TIPO_INVALIDO = 501;
		
	private int codigo;
	private String mensagem;
	
	public ErroResponse( int codigo, String... params ) {
		this.codigo = codigo;
		switch( codigo ) {
			case FORMATO_DE_DATA_INVALIDO: 
				mensagem = "Data em formato inválido.";
				break;
			case FORMATO_DE_NUMERO_REAL_INVALIDO:
				mensagem = "Valor real em formato inválido.";
				break;
				
			case MAX_RESULTS_BUSCA_FORMATO_INVALIDO:
				mensagem = "Número máximo de registros para busca inválido.";
				break;
			case ID_OFFSET_BUSCA_FORMATO_INVALIDO:
				mensagem = "ID de offset para busca inválido.";
				break;
				
			case SEM_PERMISSAO:
				mensagem = "Você não tem permissão para acessar o recurso solicitado.";
				break;
			case SEM_PERMISSAO_REG_USUARIO_RAIZ:
				mensagem = "Você não tem permissão para registrar usuário tipo RAIZ.";
				break;
				
			case USUARIO_NAO_ENCONTRADO:
				mensagem = "Usuário não encontrado.";
				break;
			case FUNCIONARIO_NAO_ENCONTRADO:
				mensagem = "Funcionario não encontrado.";
				break;	
			case FORNECEDOR_NAO_ENCONTRADO:
				mensagem = "Fornecedor não encontrado.";
				break;
			case CATEGORIA_NAO_ENCONTRADA:
				mensagem = "Categoria não encontrada.";
				break;
			case SUBCATEGORIA_NAO_ENCONTRADA:
				mensagem = "Subcategoria não encontrada.";
				break;
				
			case USUARIO_JA_EXISTE:
				mensagem = "Já existe outro usuário para o username informado.";
				break;
			case PESSOA_JA_EXISTE:
				mensagem = "Já existe cadastrada outra pessoa com o nome informado.";
				break;
			case FORNECEDOR_JA_EXISTE:
				mensagem = "Já existe cadastrado outro fornecedor com o nome de empresa informado.";
				break;
			case CATEGORIA_JA_EXISTE:
				mensagem = "Já existe cadastrada outra categoria com a descrição informada.";
				break;			
			case SUBCATEGORIA_JA_EXISTE:
				mensagem = "Já existe cadastrada outra subcategoria com a descrição informada.";
				break;
				
			case USERNAME_OBRIGATORIO:
				mensagem = "O username é um campo de preenchimento obrigatório.";
				break;
			case PASSWORD_OBRIGATORIO:
				mensagem = "A senha é um campo de preenchimento obrigatório.";
				break;																						
			case RAZAO_SOCIAL_OBRIGATORIA:
				mensagem = "Razão social é um campo de preenchimento obrigatório.";
				break;			
			case CNPJ_OBRIGATORIO:
				mensagem = "O cnpj é um campo de preenchimento obrigatório.";
				break;
			case INSCRICAO_ESTADUAL_OBRIGATORIA:
				mensagem = "A inscrição estadual é um campo de preenchimento obrigatório.";
				break;
			case FORNECEDOR_EMPRESA_OBRIGATORIA:
				mensagem = "A empresa do fornecedor é um campo de preenchimento obrigatório.";
				break;
			case CATEGORIA_DESCRICAO_OBRIGATORIA:
				mensagem = "A descrição da categoria é um campo de preenchimento obrigatório.";
				break;
			case SUBCATEGORIA_DESCRICAO_OBRIGATORIA:
				mensagem = "A descrição da subcategoria é um campo de preenchimento obrigatório.";
				break;
				
			case USUARIO_TIPO_INVALIDO:
				mensagem = "Tipo de usuário inválido.";
				break;	
			case USERNAME_PASSWORD_NAO_CORRESPONDEM:
				mensagem = "Nome de usuário e senha não correspondem.";
				break;						
		}
	}
	
}
