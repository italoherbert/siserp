package italo.siserp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErroResponse {
			
	public final static int SEM_PERMISSAO = 1;
	public final static int SEM_PERMISSAO_REG_USUARIO_RAIZ = 2;

	public final static int USUARIO_NAO_ENCONTRADO = 100;
	public final static int FUNCIONARIO_NAO_ENCONTRADO = 101;
	public final static int FORNECEDOR_NAO_ENCONTRADO = 102;	
	public final static int CATEGORIA_NAO_ENCONTRADA = 103;
	public final static int SUBCATEGORIA_NAO_ENCONTRADA = 104;
	public final static int PRODUTO_NAO_ENCONTRADO = 105;
	public final static int COMPRA_NAO_ENCONTRADA = 106;
		
	public final static int PESSOA_JA_EXISTE = 200;
	public final static int USUARIO_JA_EXISTE = 201;
	public final static int FORNECEDOR_JA_EXISTE = 202;
	public final static int CATEGORIA_JA_EXISTE = 203;
	public final static int SUBCATEGORIA_JA_EXISTE = 204;
	public final static int PRODUTO_JA_EXISTE = 205;
	
	public final static int USERNAME_OBRIGATORIO = 300;
	public final static int PASSWORD_OBRIGATORIO = 301;			
	public final static int NOME_OBRIGATORIO = 302;
	public final static int RAZAO_SOCIAL_OBRIGATORIA = 303;	
	public final static int CNPJ_OBRIGATORIO = 304;
	public final static int INSCRICAO_ESTADUAL_OBRIGATORIA = 305;
	public final static int FORNECEDOR_EMPRESA_OBRIGATORIA = 306;
	public final static int CATEGORIA_DESCRICAO_OBRIGATORIA = 307;
	public final static int SUBCATEGORIA_DESCRICAO_OBRIGATORIA = 308;
	public final static int PRODUTO_DESCRICAO_OBRIGATORIA = 309;
	public final static int PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO = 310;
	public final static int PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO = 311;
	public final static int PRODUTO_UNIDADE_OBRIGATORIA = 312;
	public final static int PRODUTO_CODIGO_BARRAS_OBRIGATORIO = 313;
	public final static int PRODUTO_DESCRICAO_OU_CODIGO_BARRAS_OBRIGATORIO = 314;
	public final static int PRODUTO_QUANTIDADE_OBRIGATORIA = 315;
	public final static int COMPRA_DATA_INI_OBRIGATORIA = 316;
	public final static int COMPRA_DATA_FIM_OBRIGATORIA = 317;
	public final static int PARCELA_VALOR_OBRIGATORIO = 318;
	public final static int DATA_PAGAMENTO_OBRIGATORIA = 319;
	public final static int DATA_VENCIMENTO_OBRIGATORIA = 320;

	public final static int USERNAME_PASSWORD_NAO_CORRESPONDEM = 500;
	public final static int USUARIO_TIPO_INVALIDO = 501;
	public final static int PRODUTO_PRECO_UNIT_COMPRA_INVALIDO = 502;
	public final static int PRODUTO_PRECO_UNIT_VENDA_INVALIDO = 503;
	public final static int QUANTIDADE_INVALIDA = 504;
	public final static int PARCELA_VALOR_INVALIDO = 505;	
	public final static int DATA_PAGAMENTO_INVALIDA = 506;
	public final static int DATA_VENCIMENTO_INVALIDA = 507;
	public final static int DATA_COMPRA_INVALIDA = 508;
	public final static int DATA_INI_INVALIDA = 509;
	public final static int DATA_FIM_INVALIDA = 510;
	public final static int DATA_INI_APOS_DATA_FIM = 511;
	public final static int NENHUM_PRODUTO_INFORMADO = 512;
		
	private int codigo;
	private String mensagem;
	
	public ErroResponse( int codigo, String... params ) {
		this.codigo = codigo;
		switch( codigo ) {							
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
			case PRODUTO_NAO_ENCONTRADO:
				mensagem = "Produto não encontrado.";
				break;
			case COMPRA_NAO_ENCONTRADA:
				mensagem = "Compra não encontrada.";
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
			case PRODUTO_JA_EXISTE:
				mensagem = "Já existe um produto cadastrado com descrição ou código de barras informado.";
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
			case PRODUTO_DESCRICAO_OBRIGATORIA:
				mensagem = "A descrição do produto é um campo de preenchimento obrigatório";
				break;
			case PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO:
				mensagem = "O preço unitário de compra do produto é um campo de preenchimento obrigatório";				
				break;
			case PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO:
				mensagem = "O preço unitário de venda do produto é um campo de preenchimento obrigatório";				
				break;
			case PRODUTO_UNIDADE_OBRIGATORIA:
				mensagem = "A unidade do produto é um campo de preenchimento obrigatório";				
				break;
			case PRODUTO_CODIGO_BARRAS_OBRIGATORIO:
				mensagem = "O código de barras do produto é um campo de preenchimento obrigatório";				
				break;
			case PRODUTO_DESCRICAO_OU_CODIGO_BARRAS_OBRIGATORIO:
				mensagem = "É necessário informar as iniciais da descrição ou codigo de barras do produto.";
				break;
			case PRODUTO_QUANTIDADE_OBRIGATORIA:
				mensagem = "A quantidade de unidades do produto é um campo de preenchimento obrigatório.";
				break;
			case COMPRA_DATA_INI_OBRIGATORIA:
				mensagem = "A data de início para filtro de compras é um campo de preenchimento obrigatório.";
				break;
			case COMPRA_DATA_FIM_OBRIGATORIA:
				mensagem = "A data de fim para filtro de compras é um campo de preenchimento obrigatório.";
				break;
			case DATA_PAGAMENTO_OBRIGATORIA:
				mensagem = "A data de pagamento é um campo de preenchimento obrigatório.";
				break;
			case DATA_VENCIMENTO_OBRIGATORIA:
				mensagem = "A data de vencimento é um campo de preenchimento obrigatório.";
				break;
				
			case USUARIO_TIPO_INVALIDO:
				mensagem = "Tipo de usuário inválido.";
				break;	
			case USERNAME_PASSWORD_NAO_CORRESPONDEM:
				mensagem = "Nome de usuário e senha não correspondem.";
				break;						
			case PRODUTO_PRECO_UNIT_COMPRA_INVALIDO:
				mensagem = "O preço unitário de compra do produto está em formato inválido. Valor="+params[0];
				break;
			case PRODUTO_PRECO_UNIT_VENDA_INVALIDO:
				mensagem = "O preço unitário de venda do produto está em formato inválido. Valor="+params[0];
				break;
			case QUANTIDADE_INVALIDA:
				mensagem = "O quantidade de unidades está em formato inválido. Valor="+params[0];
				break;
			case PARCELA_VALOR_INVALIDO:
				mensagem = "O valor da parcela está em formato inválido. Valor="+params[0];
				break;
			case DATA_PAGAMENTO_INVALIDA:
				mensagem = "A data de pagamento está em formato inválido. Valor="+params[0];
				break;
			case DATA_VENCIMENTO_INVALIDA:
				mensagem = "A data de vencimento está em formato inválido. Valor="+params[0];
				break;
			case DATA_COMPRA_INVALIDA:
				mensagem = "A data de compra está em formato inválido. Valor="+params[0];
				break;
			case DATA_INI_INVALIDA:
				mensagem = "A data de início está em formato inválido. Valor="+params[0];
				break;
			case DATA_FIM_INVALIDA:
				mensagem = "A data de fim está em formato inválido. Valor="+params[0];
				break;
			case DATA_INI_APOS_DATA_FIM:
				mensagem = "A data de início tem valor de antes da data de fim";
				break;
			case NENHUM_PRODUTO_INFORMADO:
				mensagem = "É necessário informar os dados de ao menos um produto.";
				break;
		}
	}
	
}
