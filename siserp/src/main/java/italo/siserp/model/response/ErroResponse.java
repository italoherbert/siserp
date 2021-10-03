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
	
	public final static int FALHA_GERACAO_RELATORIO = 3;
	public final static int FALHA_LEITURA_OU_AJUSTE_LOGO = 4;
	
	public final static int USUARIO_NAO_ENCONTRADO = 100;
	public final static int FUNCIONARIO_NAO_ENCONTRADO = 101;
	public final static int FORNECEDOR_NAO_ENCONTRADO = 102;	
	public final static int CATEGORIA_MAP_NAO_ENCONTRADA = 103;
	public final static int PRODUTO_NAO_ENCONTRADO = 105;
	public final static int COMPRA_NAO_ENCONTRADA = 106;
	public final static int CLIENTE_NAO_ENCONTRADO = 107;
	public final static int VENDA_NAO_ENCONTRADA = 108;
	public final static int CAIXA_NAO_ENCONTRADO = 109;
	public final static int LANCAMENTO_NAO_ENCONTRADO = 110;
	public final static int USUARIO_GRUPO_NAO_ENCONTRADO = 111;
	public final static int RECURSO_NAO_ENCONTRADO = 112;
	public final static int PERMISSAO_GRUPO_NAO_ENCONTRADO = 113;
	public final static int PARCELA_NAO_ENCONTRADA = 114;
	public final static int USUARIO_LOGADO_NAO_ENCONTRADO = 115;
	
	public final static int CODIGO_BARRAS_NAO_REGISTRADO = 150;
		
	public final static int PESSOA_JA_EXISTE = 200;
	public final static int USUARIO_JA_EXISTE = 201;
	public final static int FORNECEDOR_JA_EXISTE = 202;
	public final static int CATEGORIA_MAP_JA_EXISTE = 203;
	public final static int PRODUTO_JA_EXISTE = 205;
	public final static int USUARIO_GRUPO_JA_EXISTE = 206;
	public final static int RECURSO_JA_EXISTE = 207;
	
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
	public final static int DATA_INI_OBRIGATORIA = 316;
	public final static int DATA_FIM_OBRIGATORIA = 317;
	public final static int PARCELA_VALOR_OBRIGATORIO = 318;
	public final static int DATA_PAGAMENTO_OBRIGATORIA = 319;
	public final static int DATA_VENCIMENTO_OBRIGATORIA = 320;
	public final static int FLAG_INCLUIR_CLIENTE_OBRIGATORIO = 321;
	public final static int CLIENTE_NOME_OBRIGATORIO = 322;
	public final static int CAIXA_VALOR_INICIAL_OBRIGATORIO = 323;
	public final static int TIPO_LANCAMENTO_OBRIGATORIO = 324;
	public final static int VALOR_LANCAMENTO_OBRIGATORIO = 325;
	public final static int LANCAMENTO_ABERTURA_CAIXA_OBRITATORIO = 326;
	public final static int FLAG_INCLUIR_FUNCIONARIO_OBRIGATORIO = 327;
	public final static int FUNCIONARIO_NOME_OBRIGATORIO = 328;
	public final static int USUARIO_GRUPO_OBRIGATORIO = 329;
	public final static int VALOR_RECEBIDO_OBRIGATORIO = 330;
	public final static int FLAG_INCLUIR_PARCELAS_PAGAS_OBRIGATORIO = 331;
	public final static int FLAG_INCLUIR_VENDAS_PAGAS_OBRIGATORIO = 332;
	public final static int EMPRESA_NOME_CAMPO_OBRIGATORIO = 333;
	public final static int LOGO_BASE64_OBRIGATORIA = 334;
	
	public final static int USERNAME_PASSWORD_NAO_CORRESPONDEM = 500;
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
	public final static int FLAG_INCLUIR_CLIENTE_VALOR_INVALIDO = 513;
	public final static int DATA_VENDA_INVALIDA = 514;
	public final static int SUBTOTAL_INVALIDO = 515;
	public final static int DESCONTO_INVALIDO = 516;
	public final static int DEBITO_INVALIDO = 517;
	public final static int PERFIL_DE_CAIXA_REQUERIDO = 518;
	public final static int CAIXA_VALOR_INICIAL_INVALIDO = 519;
	public final static int CAIXA_NAO_ABERTO = 520;
	public final static int CAIXA_JA_ABERTO = 521;
	public final static int VALOR_PAGO_INVALIDO = 522;	
	public final static int LANCAMENTO_TIPO_INVALIDO = 523;
	public final static int LANCAMENTO_VALOR_INVALIDO = 524;
	public final static int FORMA_PAG_INVALIDA = 525;
	public final static int FLAG_INCLUIR_FUNCIONARIO_VALOR_INVALIDO = 526;
	public final static int TENTATIVA_DELETAR_GRUPO_NAO_VAZIO = 527;
	public final static int PERMISSAO_LEITURA_INVALIDA = 528;
	public final static int PERMISSAO_ESCRITA_INVALIDA = 529;
	public final static int PERMISSAO_REMOCAO_INVALIDA = 530;
	public final static int PERMISSAO_TIPO_INVALIDO = 531;
	public final static int VALOR_PARCELA_SITUACAO_INVALIDO = 532;
	public final static int VALOR_RECEBIDO_INVALIDO = 533;
	public final static int DATA_DIA_INVALIDA = 534;
	public final static int VALOR_EM_CAIXA_INSUFICIENTE = 535;
		
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
			case FALHA_GERACAO_RELATORIO:
				mensagem = "Houve falha na geração do relatório: "+params[0];
				break;
			case FALHA_LEITURA_OU_AJUSTE_LOGO:
				mensagem = "Falha na leitura ou ajuste da logomarca.";
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
			case CATEGORIA_MAP_NAO_ENCONTRADA:
				mensagem = "Categoria e subcategoria não encontrada.";
				break;
			case PRODUTO_NAO_ENCONTRADO:
				mensagem = "Produto não encontrado.";
				break;
			case COMPRA_NAO_ENCONTRADA:
				mensagem = "Compra não encontrada.";
				break;
			case CLIENTE_NAO_ENCONTRADO:
				mensagem = "Cliente não encontrado.";
				break;
			case VENDA_NAO_ENCONTRADA:
				mensagem = "Venda não encontrada.";
				break;
			case CAIXA_NAO_ENCONTRADO:
				mensagem = "Caixa não encontrado.";
				break;
			case LANCAMENTO_NAO_ENCONTRADO:
				mensagem = "Lançamento não encontrado.";
				break;
			case RECURSO_NAO_ENCONTRADO:
				mensagem = "Recurso não encontrado.";
				break;
			case USUARIO_GRUPO_NAO_ENCONTRADO:
				mensagem = "Grupo de usuário não encontrado.";
				break;
			case PERMISSAO_GRUPO_NAO_ENCONTRADO:
				mensagem = "Grupo de permissões não encontrado.";
				break;
			case PARCELA_NAO_ENCONTRADA:
				mensagem = "Parcela não encontrada";
				break;
			case USUARIO_LOGADO_NAO_ENCONTRADO:
				mensagem = "Usuário logado não encontrado.";
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
			case CATEGORIA_MAP_JA_EXISTE:
				mensagem = "Já existe um mapeamento para a categoria e subcategoria informadas.";
				break;	
			case PRODUTO_JA_EXISTE:
				mensagem = "Já existe um produto cadastrado com descrição ou código de barras informado.";
				break;
			case USUARIO_GRUPO_JA_EXISTE:
				mensagem = "Já existe um grupo de usuario cadastrado com o nome informado.";
				break;
			case RECURSO_JA_EXISTE:
				mensagem = "Já existe um recurso registrado com o nome informado.";
				break;
							
			case USERNAME_OBRIGATORIO:
				mensagem = "O nome de usuário é um campo de preenchimento obrigatório.";
				break;
			case PASSWORD_OBRIGATORIO:
				mensagem = "A senha é um campo de preenchimento obrigatório.";
				break;
			case NOME_OBRIGATORIO:
				mensagem = "O nome é um campo de preenchimento obrigatório.";
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
			case DATA_INI_OBRIGATORIA:
				mensagem = "A data de início é um campo de preenchimento obrigatório.";
				break;
			case DATA_FIM_OBRIGATORIA:
				mensagem = "A data de fim é um campo de preenchimento obrigatório.";
				break;
			case DATA_PAGAMENTO_OBRIGATORIA:
				mensagem = "A data de pagamento é um campo de preenchimento obrigatório.";
				break;
			case DATA_VENCIMENTO_OBRIGATORIA:
				mensagem = "A data de vencimento é um campo de preenchimento obrigatório.";
				break;
			case FLAG_INCLUIR_CLIENTE_OBRIGATORIO:
				mensagem = "Flag de inclusão de cliente no filtro é um campo de preenchimento obrigatório.";
				break;
			case CLIENTE_NOME_OBRIGATORIO:
				mensagem = "O nome do cliente é um campo de preenchimento obrigatório.";
				break;
			case CAIXA_VALOR_INICIAL_OBRIGATORIO:
				mensagem = "O valor de início para abertura do caixa é um campo de preenchimento obrigatório.";
				break;
			case TIPO_LANCAMENTO_OBRIGATORIO:
				mensagem = "O tipo do lançamento é um campo obrigatório.";
				break;
			case LANCAMENTO_ABERTURA_CAIXA_OBRITATORIO:
				mensagem = "O lançamento de abertura do caixa é um campo obrigatório.";
				break;
			case VALOR_LANCAMENTO_OBRIGATORIO:
				mensagem = "O valor de lançamento é um campo de preenchimento obrigatório.";
				break;
			case FLAG_INCLUIR_FUNCIONARIO_OBRIGATORIO:
				mensagem = "Flag de inclusão de funcionario no filtro é um campo obrigatório.";
				break;
			case FUNCIONARIO_NOME_OBRIGATORIO:
				mensagem = "O nome do funcionário é um campo de preenchimento obrigatório.";
				break;
			case USUARIO_GRUPO_OBRIGATORIO:
				mensagem = "O grupo do usuário é um campo de preenchimento obrigatório.";
				break;
			case VALOR_RECEBIDO_OBRIGATORIO:
				mensagem = "O valor recebido é um campo de preenchimento obrigatório.";
				break;
			case FLAG_INCLUIR_PARCELAS_PAGAS_OBRIGATORIO:
				mensagem = "O flag de inclusão de parcelas pagas é obrigatório.";
				break;
			case FLAG_INCLUIR_VENDAS_PAGAS_OBRIGATORIO:
				mensagem = "O flag de inclusão de vendas pagas é obrigatório";
				break;
			case EMPRESA_NOME_CAMPO_OBRIGATORIO:
				mensagem = "O nome da empresa é um campo obrigatório.";
				break;
			case LOGO_BASE64_OBRIGATORIA:
				mensagem = "A imagem codificada em Base 64 é um campo obrigatório.";
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
			case DATA_VENDA_INVALIDA:
				mensagem = "A data de venda está em formato inválido.";
				break;
			case FLAG_INCLUIR_CLIENTE_VALOR_INVALIDO:
				mensagem = "O campo de incluir cliente ativado está em formato inválido.";
				break;				
			case SUBTOTAL_INVALIDO:
				mensagem = "O campo subtotal está em formato inválido";
				break;
			case DESCONTO_INVALIDO:
				mensagem = "O campo desconto está em formato inválido";
				break;
			case DEBITO_INVALIDO:
				mensagem = "O campo débito está em formato inválido.";
				break;
			case PERFIL_DE_CAIXA_REQUERIDO:
				mensagem = "Perfil de caixa requerido.";
				break;
			case CAIXA_VALOR_INICIAL_INVALIDO:
				mensagem = "Valor inicial para abertura do caixa inválido.";
				break;
			case CAIXA_NAO_ABERTO:
				mensagem = "Caixa não aberto.";
				break;
			case CAIXA_JA_ABERTO:
				mensagem = "Caixa já aberto";
				break;
			case VALOR_PAGO_INVALIDO:
				mensagem = "O valor pago está em formato inválido.";
				break;
			case LANCAMENTO_TIPO_INVALIDO:
				mensagem = "O tipo de lançamento informado é inválido.";
				break;
			case LANCAMENTO_VALOR_INVALIDO:
				mensagem = "O valor de lançamento informado é inválido.";
				break;
			case FORMA_PAG_INVALIDA:
				mensagem = "Forma de pagamento inválida.";
				break;
			case FLAG_INCLUIR_FUNCIONARIO_VALOR_INVALIDO:
				mensagem = "Flag de inclusão de funcionário em formato inválido.";
				break;
			case TENTATIVA_DELETAR_GRUPO_NAO_VAZIO:
				mensagem = "Tentativa de deletar grupo que contém usuários";
				break;
			case PERMISSAO_LEITURA_INVALIDA:
				mensagem = "Permissão de leitura não booleana.";
				break;
			case PERMISSAO_ESCRITA_INVALIDA:
				mensagem = "Permissão de escrita não booleana.";				
				break;
			case PERMISSAO_REMOCAO_INVALIDA:
				mensagem = "Permissão de remoção não booleana.";	
				break;
			case PERMISSAO_TIPO_INVALIDO:
				mensagem = "Tipo de permissão inválido.";
				break;
			case VALOR_PARCELA_SITUACAO_INVALIDO:
				mensagem = "Valor de situação da parcela inválido";
				break;
			case VALOR_RECEBIDO_INVALIDO:
				mensagem = "O valor recebido informado está em formato inválido";
				break;
			case DATA_DIA_INVALIDA:
				mensagem = "A data informada está em formato inválido.";
				break;								
			case VALOR_EM_CAIXA_INSUFICIENTE:
				mensagem = "Valor em caixa insuficiente para o valor informado.";
				break;
		}		
	}
	
}
