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
				mensagem = "Voc?? n??o tem permiss??o para acessar o recurso solicitado.";
				break;
			case SEM_PERMISSAO_REG_USUARIO_RAIZ:
				mensagem = "Voc?? n??o tem permiss??o para registrar usu??rio tipo RAIZ.";
				break;						
			case FALHA_GERACAO_RELATORIO:
				mensagem = "Houve falha na gera????o do relat??rio: "+params[0];
				break;
			case FALHA_LEITURA_OU_AJUSTE_LOGO:
				mensagem = "Falha na leitura ou ajuste da logomarca.";
				break;
				
			case USUARIO_NAO_ENCONTRADO:
				mensagem = "Usu??rio n??o encontrado.";
				break;
			case FUNCIONARIO_NAO_ENCONTRADO:
				mensagem = "Funcionario n??o encontrado.";
				break;	
			case FORNECEDOR_NAO_ENCONTRADO:
				mensagem = "Fornecedor n??o encontrado.";
				break;
			case CATEGORIA_MAP_NAO_ENCONTRADA:
				mensagem = "Categoria e subcategoria n??o encontrada.";
				break;
			case PRODUTO_NAO_ENCONTRADO:
				mensagem = "Produto n??o encontrado.";
				break;
			case COMPRA_NAO_ENCONTRADA:
				mensagem = "Compra n??o encontrada.";
				break;
			case CLIENTE_NAO_ENCONTRADO:
				mensagem = "Cliente n??o encontrado.";
				break;
			case VENDA_NAO_ENCONTRADA:
				mensagem = "Venda n??o encontrada.";
				break;
			case CAIXA_NAO_ENCONTRADO:
				mensagem = "Caixa n??o encontrado.";
				break;
			case LANCAMENTO_NAO_ENCONTRADO:
				mensagem = "Lan??amento n??o encontrado.";
				break;
			case RECURSO_NAO_ENCONTRADO:
				mensagem = "Recurso n??o encontrado.";
				break;
			case USUARIO_GRUPO_NAO_ENCONTRADO:
				mensagem = "Grupo de usu??rio n??o encontrado.";
				break;
			case PERMISSAO_GRUPO_NAO_ENCONTRADO:
				mensagem = "Grupo de permiss??es n??o encontrado.";
				break;
			case PARCELA_NAO_ENCONTRADA:
				mensagem = "Parcela n??o encontrada";
				break;
			case USUARIO_LOGADO_NAO_ENCONTRADO:
				mensagem = "Usu??rio logado n??o encontrado.";
				break;
				
			case USUARIO_JA_EXISTE:
				mensagem = "J?? existe outro usu??rio para o username informado.";
				break;
			case PESSOA_JA_EXISTE:
				mensagem = "J?? existe cadastrada outra pessoa com o nome informado.";
				break;
			case FORNECEDOR_JA_EXISTE:
				mensagem = "J?? existe cadastrado outro fornecedor com o nome de empresa informado.";
				break;
			case CATEGORIA_MAP_JA_EXISTE:
				mensagem = "J?? existe um mapeamento para a categoria e subcategoria informadas.";
				break;	
			case PRODUTO_JA_EXISTE:
				mensagem = "J?? existe um produto cadastrado com descri????o ou c??digo de barras informado.";
				break;
			case USUARIO_GRUPO_JA_EXISTE:
				mensagem = "J?? existe um grupo de usuario cadastrado com o nome informado.";
				break;
			case RECURSO_JA_EXISTE:
				mensagem = "J?? existe um recurso registrado com o nome informado.";
				break;
							
			case USERNAME_OBRIGATORIO:
				mensagem = "O nome de usu??rio ?? um campo de preenchimento obrigat??rio.";
				break;
			case PASSWORD_OBRIGATORIO:
				mensagem = "A senha ?? um campo de preenchimento obrigat??rio.";
				break;
			case NOME_OBRIGATORIO:
				mensagem = "O nome ?? um campo de preenchimento obrigat??rio.";
				break;
			case RAZAO_SOCIAL_OBRIGATORIA:
				mensagem = "Raz??o social ?? um campo de preenchimento obrigat??rio.";
				break;			
			case CNPJ_OBRIGATORIO:
				mensagem = "O cnpj ?? um campo de preenchimento obrigat??rio.";
				break;
			case INSCRICAO_ESTADUAL_OBRIGATORIA:
				mensagem = "A inscri????o estadual ?? um campo de preenchimento obrigat??rio.";
				break;
			case FORNECEDOR_EMPRESA_OBRIGATORIA:
				mensagem = "A empresa do fornecedor ?? um campo de preenchimento obrigat??rio.";
				break;
			case CATEGORIA_DESCRICAO_OBRIGATORIA:
				mensagem = "A descri????o da categoria ?? um campo de preenchimento obrigat??rio.";
				break;
			case SUBCATEGORIA_DESCRICAO_OBRIGATORIA:
				mensagem = "A descri????o da subcategoria ?? um campo de preenchimento obrigat??rio.";
				break;
			case PRODUTO_DESCRICAO_OBRIGATORIA:
				mensagem = "A descri????o do produto ?? um campo de preenchimento obrigat??rio";
				break;
			case PRODUTO_PRECO_UNIT_COMPRA_OBRIGATORIO:
				mensagem = "O pre??o unit??rio de compra do produto ?? um campo de preenchimento obrigat??rio";				
				break;
			case PRODUTO_PRECO_UNIT_VENDA_OBRIGATORIO:
				mensagem = "O pre??o unit??rio de venda do produto ?? um campo de preenchimento obrigat??rio";				
				break;
			case PRODUTO_UNIDADE_OBRIGATORIA:
				mensagem = "A unidade do produto ?? um campo de preenchimento obrigat??rio";				
				break;
			case PRODUTO_CODIGO_BARRAS_OBRIGATORIO:
				mensagem = "O c??digo de barras do produto ?? um campo de preenchimento obrigat??rio";				
				break;
			case PRODUTO_DESCRICAO_OU_CODIGO_BARRAS_OBRIGATORIO:
				mensagem = "?? necess??rio informar as iniciais da descri????o ou codigo de barras do produto.";
				break;
			case PRODUTO_QUANTIDADE_OBRIGATORIA:
				mensagem = "A quantidade de unidades do produto ?? um campo de preenchimento obrigat??rio.";
				break;
			case DATA_INI_OBRIGATORIA:
				mensagem = "A data de in??cio ?? um campo de preenchimento obrigat??rio.";
				break;
			case DATA_FIM_OBRIGATORIA:
				mensagem = "A data de fim ?? um campo de preenchimento obrigat??rio.";
				break;
			case DATA_PAGAMENTO_OBRIGATORIA:
				mensagem = "A data de pagamento ?? um campo de preenchimento obrigat??rio.";
				break;
			case DATA_VENCIMENTO_OBRIGATORIA:
				mensagem = "A data de vencimento ?? um campo de preenchimento obrigat??rio.";
				break;
			case FLAG_INCLUIR_CLIENTE_OBRIGATORIO:
				mensagem = "Flag de inclus??o de cliente no filtro ?? um campo de preenchimento obrigat??rio.";
				break;
			case CLIENTE_NOME_OBRIGATORIO:
				mensagem = "O nome do cliente ?? um campo de preenchimento obrigat??rio.";
				break;
			case CAIXA_VALOR_INICIAL_OBRIGATORIO:
				mensagem = "O valor de in??cio para abertura do caixa ?? um campo de preenchimento obrigat??rio.";
				break;
			case TIPO_LANCAMENTO_OBRIGATORIO:
				mensagem = "O tipo do lan??amento ?? um campo obrigat??rio.";
				break;
			case LANCAMENTO_ABERTURA_CAIXA_OBRITATORIO:
				mensagem = "O lan??amento de abertura do caixa ?? um campo obrigat??rio.";
				break;
			case VALOR_LANCAMENTO_OBRIGATORIO:
				mensagem = "O valor de lan??amento ?? um campo de preenchimento obrigat??rio.";
				break;
			case FLAG_INCLUIR_FUNCIONARIO_OBRIGATORIO:
				mensagem = "Flag de inclus??o de funcionario no filtro ?? um campo obrigat??rio.";
				break;
			case FUNCIONARIO_NOME_OBRIGATORIO:
				mensagem = "O nome do funcion??rio ?? um campo de preenchimento obrigat??rio.";
				break;
			case USUARIO_GRUPO_OBRIGATORIO:
				mensagem = "O grupo do usu??rio ?? um campo de preenchimento obrigat??rio.";
				break;
			case VALOR_RECEBIDO_OBRIGATORIO:
				mensagem = "O valor recebido ?? um campo de preenchimento obrigat??rio.";
				break;
			case FLAG_INCLUIR_PARCELAS_PAGAS_OBRIGATORIO:
				mensagem = "O flag de inclus??o de parcelas pagas ?? obrigat??rio.";
				break;
			case FLAG_INCLUIR_VENDAS_PAGAS_OBRIGATORIO:
				mensagem = "O flag de inclus??o de vendas pagas ?? obrigat??rio";
				break;
			case EMPRESA_NOME_CAMPO_OBRIGATORIO:
				mensagem = "O nome da empresa ?? um campo obrigat??rio.";
				break;
			case LOGO_BASE64_OBRIGATORIA:
				mensagem = "A imagem codificada em Base 64 ?? um campo obrigat??rio.";
				break;
				
					
			case USERNAME_PASSWORD_NAO_CORRESPONDEM:
				mensagem = "Nome de usu??rio e senha n??o correspondem.";
				break;						
			case PRODUTO_PRECO_UNIT_COMPRA_INVALIDO:
				mensagem = "O pre??o unit??rio de compra do produto est?? em formato inv??lido. Valor="+params[0];
				break;
			case PRODUTO_PRECO_UNIT_VENDA_INVALIDO:
				mensagem = "O pre??o unit??rio de venda do produto est?? em formato inv??lido. Valor="+params[0];
				break;
			case QUANTIDADE_INVALIDA:
				mensagem = "O quantidade de unidades est?? em formato inv??lido. Valor="+params[0];
				break;
			case PARCELA_VALOR_INVALIDO:
				mensagem = "O valor da parcela est?? em formato inv??lido. Valor="+params[0];
				break;
			case DATA_PAGAMENTO_INVALIDA:
				mensagem = "A data de pagamento est?? em formato inv??lido. Valor="+params[0];
				break;
			case DATA_VENCIMENTO_INVALIDA:
				mensagem = "A data de vencimento est?? em formato inv??lido. Valor="+params[0];
				break;
			case DATA_COMPRA_INVALIDA:
				mensagem = "A data de compra est?? em formato inv??lido. Valor="+params[0];
				break;
			case DATA_INI_INVALIDA:
				mensagem = "A data de in??cio est?? em formato inv??lido. Valor="+params[0];
				break;
			case DATA_FIM_INVALIDA:
				mensagem = "A data de fim est?? em formato inv??lido. Valor="+params[0];
				break;
			case DATA_INI_APOS_DATA_FIM:
				mensagem = "A data de in??cio tem valor de antes da data de fim";
				break;
			case NENHUM_PRODUTO_INFORMADO:
				mensagem = "?? necess??rio informar os dados de ao menos um produto.";
				break;
			case DATA_VENDA_INVALIDA:
				mensagem = "A data de venda est?? em formato inv??lido.";
				break;
			case FLAG_INCLUIR_CLIENTE_VALOR_INVALIDO:
				mensagem = "O campo de incluir cliente ativado est?? em formato inv??lido.";
				break;				
			case SUBTOTAL_INVALIDO:
				mensagem = "O campo subtotal est?? em formato inv??lido";
				break;
			case DESCONTO_INVALIDO:
				mensagem = "O campo desconto est?? em formato inv??lido";
				break;
			case DEBITO_INVALIDO:
				mensagem = "O campo d??bito est?? em formato inv??lido.";
				break;
			case PERFIL_DE_CAIXA_REQUERIDO:
				mensagem = "Perfil de caixa requerido.";
				break;
			case CAIXA_VALOR_INICIAL_INVALIDO:
				mensagem = "Valor inicial para abertura do caixa inv??lido.";
				break;
			case CAIXA_NAO_ABERTO:
				mensagem = "Caixa n??o aberto.";
				break;
			case CAIXA_JA_ABERTO:
				mensagem = "Caixa j?? aberto";
				break;
			case VALOR_PAGO_INVALIDO:
				mensagem = "O valor pago est?? em formato inv??lido.";
				break;
			case LANCAMENTO_TIPO_INVALIDO:
				mensagem = "O tipo de lan??amento informado ?? inv??lido.";
				break;
			case LANCAMENTO_VALOR_INVALIDO:
				mensagem = "O valor de lan??amento informado ?? inv??lido.";
				break;
			case FORMA_PAG_INVALIDA:
				mensagem = "Forma de pagamento inv??lida.";
				break;
			case FLAG_INCLUIR_FUNCIONARIO_VALOR_INVALIDO:
				mensagem = "Flag de inclus??o de funcion??rio em formato inv??lido.";
				break;
			case TENTATIVA_DELETAR_GRUPO_NAO_VAZIO:
				mensagem = "Tentativa de deletar grupo que cont??m usu??rios";
				break;
			case PERMISSAO_LEITURA_INVALIDA:
				mensagem = "Permiss??o de leitura n??o booleana.";
				break;
			case PERMISSAO_ESCRITA_INVALIDA:
				mensagem = "Permiss??o de escrita n??o booleana.";				
				break;
			case PERMISSAO_REMOCAO_INVALIDA:
				mensagem = "Permiss??o de remo????o n??o booleana.";	
				break;
			case PERMISSAO_TIPO_INVALIDO:
				mensagem = "Tipo de permiss??o inv??lido.";
				break;
			case VALOR_PARCELA_SITUACAO_INVALIDO:
				mensagem = "Valor de situa????o da parcela inv??lido";
				break;
			case VALOR_RECEBIDO_INVALIDO:
				mensagem = "O valor recebido informado est?? em formato inv??lido";
				break;
			case DATA_DIA_INVALIDA:
				mensagem = "A data informada est?? em formato inv??lido.";
				break;								
			case VALOR_EM_CAIXA_INSUFICIENTE:
				mensagem = "Valor em caixa insuficiente para o valor informado.";
				break;
		}		
	}
	
}
