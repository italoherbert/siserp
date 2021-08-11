package italo.siserp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import italo.siserp.model.Caixa;
import italo.siserp.model.Funcionario;
import italo.siserp.model.Produto;
import italo.siserp.model.Venda;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.SaveEnderecoRequest;
import italo.siserp.model.request.SaveFuncionarioRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.request.SavePessoaRequest;
import italo.siserp.model.request.SaveProdutoRequest;
import italo.siserp.model.request.SaveUsuarioGrupoRequest;
import italo.siserp.model.request.SaveUsuarioRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.model.response.CaixaBalancoResponse;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.FuncionarioRepository;
import italo.siserp.repository.LancamentoRepository;
import italo.siserp.repository.ProdutoRepository;
import italo.siserp.repository.VendaRepository;
import italo.siserp.service.CaixaService;
import italo.siserp.util.DataUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "admin", authorities = "ADMIN")
public class VendaTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private CaixaRepository caixaRepository;
		
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private CaixaService caixaService;
	
	@Autowired
	private DataUtil dataUtil;
		
	@Test
	public void vendaTest() {		 																				
		try {			
			SaveFuncionarioRequest req = new SaveFuncionarioRequest();
			req.setPessoa( new SavePessoaRequest() );
			req.setUsuario( new SaveUsuarioRequest() );
			
			req.getUsuario().setGrupo( new SaveUsuarioGrupoRequest() );
			req.getPessoa().setEndereco( new SaveEnderecoRequest() );
			
			req.getPessoa().setNome( "Teste" );
			req.getUsuario().setUsername( "teste" );
			req.getUsuario().setPassword( "teste" );
			req.getUsuario().getGrupo().setNome( "CAIXA" );
			
			RequestBuilder saveFuncRB = MockMvcRequestBuilders.post( "/api/funcionario/registra" )
					.content( toJson( req ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			mockMvc.perform( saveFuncRB ).andExpect( status().isOk() );
			
			List<Funcionario> lista = funcionarioRepository.filtra( "Te%", "te%" );
			assertTrue( lista.size() == 1 );
			
			Funcionario f = lista.get( 0 );
			assertNotNull( f );
			
			Long uid = f.getUsuario().getId();
			double valorAberturaCaixa = 50.5;
			double valorL1 = 280.59;
			double valorL2 = 80.5;
			double valorL3 = 100.0;
			
			int produto1Quantidade = 80;
			int produto2Quantidade = 100;
			
			int itemVenda1Quantidade = 25;
			int itemVenda2Quantidade = 48;
			
			double valorAposLancamentos = valorAberturaCaixa + valorL1 - valorL2 + valorL3;
			double valorAposVendaRegistrada = valorAberturaCaixa + ( ( ( 25 * 18.5 + 48 * 45 ) * 0.88 ) + 300 );
			
			AbreCaixaRequest abreCaixaReq = new AbreCaixaRequest();
			abreCaixaReq.setLancamento( new SaveLancamentoRequest() );
			
			abreCaixaReq.getLancamento().setTipo( "CREDITO" );
			abreCaixaReq.getLancamento().setValor( String.valueOf( valorAberturaCaixa ) );
			
			RequestBuilder abreCaixaRB = MockMvcRequestBuilders.post( "/api/caixa/abre/{usuarioId}", uid )
					.content( toJson( abreCaixaReq ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			mockMvc.perform( abreCaixaRB ).andExpect( status().isOk() );
								
			CaixaBalancoResponse balanco = caixaService.geraCaixaBalancoHoje( uid );
			assertEquals( Double.parseDouble( balanco.getSaldo() ), valorAberturaCaixa, 0.01 );
			
			SaveLancamentoRequest lancamentoRequest1 = new SaveLancamentoRequest();
			lancamentoRequest1.setTipo( "CREDITO" );
			lancamentoRequest1.setValor( ""+valorL1 );
			
			SaveLancamentoRequest lancamentoRequest2 = new SaveLancamentoRequest();
			lancamentoRequest2.setTipo( "DEBITO" );
			lancamentoRequest2.setValor( ""+valorL2 );
						
			SaveLancamentoRequest lancamentoRequest3 = new SaveLancamentoRequest();
			lancamentoRequest3.setTipo( "CREDITO" );
			lancamentoRequest3.setValor( ""+valorL3 );
			
			RequestBuilder regLanc1RB = MockMvcRequestBuilders.post( "/api/caixa/lancamento/efetua/{usuarioId}", uid )
					.content( toJson( lancamentoRequest1 ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			RequestBuilder regLanc2RB = MockMvcRequestBuilders.post( "/api/caixa/lancamento/efetua/{usuarioId}", uid )
					.content( toJson( lancamentoRequest2 ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			RequestBuilder regLanc3RB = MockMvcRequestBuilders.post( "/api/caixa/lancamento/efetua/{usuarioId}", uid )
					.content( toJson( lancamentoRequest3 ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			int quantLancamentos = lancamentoRepository.findAll().size();
			
			mockMvc.perform( regLanc1RB ).andExpect( status().isOk() );			
			mockMvc.perform( regLanc2RB ).andExpect( status().isOk() );
			mockMvc.perform( regLanc3RB ).andExpect( status().isOk() );
						
			int quantLancamentos2 = lancamentoRepository.findAll().size();
			assertEquals( quantLancamentos2, quantLancamentos + 3 );
			
			balanco = caixaService.geraCaixaBalancoHoje( uid );
			assertEquals( Double.parseDouble( balanco.getSaldo() ), valorAposLancamentos, 0.01 );
			
			SaveProdutoRequest p1 = new SaveProdutoRequest();
			p1.setCodigoBarras( "_000" );
			p1.setDescricao( "P1" );
			p1.setPrecoUnitCompra( "15" );
			p1.setPrecoUnitVenda( "18.5" );
			p1.setQuantidade( ""+produto1Quantidade );			
			
			SaveProdutoRequest p2 = new SaveProdutoRequest();
			p2.setCodigoBarras( "_001" );
			p2.setDescricao( "P2" );
			p2.setPrecoUnitCompra( "40.52" );
			p2.setPrecoUnitVenda( "45.0" );
			p2.setQuantidade( ""+produto2Quantidade );						
		
			RequestBuilder regP1RB = MockMvcRequestBuilders.post( "/api/produto/salva" )
					.content( toJson( p1 ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			RequestBuilder regP2RB = MockMvcRequestBuilders.post( "/api/produto/salva" )
					.content( toJson( p2 ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			mockMvc.perform( regP1RB ).andExpect( status().isOk() );
			mockMvc.perform( regP2RB ).andExpect( status().isOk() );	
									
			SaveItemVendaRequest saveIVRequest1 = new SaveItemVendaRequest();
			saveIVRequest1.setCodigoBarras( "_000" );
			saveIVRequest1.setQuantidade( ""+itemVenda1Quantidade );
			
			SaveItemVendaRequest saveIVRequest2 = new SaveItemVendaRequest();
			saveIVRequest2.setCodigoBarras( "_001" );
			saveIVRequest2.setQuantidade( ""+itemVenda2Quantidade );
			
			SaveVendaRequest saveVendaRequest = new SaveVendaRequest();
			saveVendaRequest.setItensVenda( new ArrayList<>() );
			saveVendaRequest.setSubtotal( ""+(25 * 18.5 + 48 * 45 ) );
			saveVendaRequest.setDesconto( "0.12" );
			saveVendaRequest.setFormaPag( "ESPECIE" );
			saveVendaRequest.setIncluirCliente( "false" );
			
			saveVendaRequest.getItensVenda().add( saveIVRequest1 );
			saveVendaRequest.getItensVenda().add( saveIVRequest2 );
			
			RequestBuilder regVendaRB = MockMvcRequestBuilders.post( "/api/venda/efetua/{usuarioId}", uid )
					.content( toJson( saveVendaRequest ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			mockMvc.perform( regVendaRB ).andExpect( status().isOk() );
						
			balanco = caixaService.geraCaixaBalancoHoje( uid );
			assertEquals( Double.parseDouble( balanco.getSaldo() ), valorAposVendaRegistrada );
					
			List<Produto> prod1List = produtoRepository.filtraPorDescIni( "P1" );
			List<Produto> prod2List = produtoRepository.filtraPorDescIni( "P2" );
			
			assertTrue( prod1List.size() == 1 );
			assertTrue( prod2List.size() == 1 );
			
			Produto prod1 = prod1List.get( 0 );
			assertNotNull( prod1 );
			
			Produto prod2 = prod2List.get( 0 );
			assertNotNull( prod2 );
			
			assertEquals( prod1.getQuantidade(), produto1Quantidade - itemVenda1Quantidade );
			assertEquals( prod2.getQuantidade(), produto2Quantidade - itemVenda2Quantidade );
			
			List<Venda> vendas = vendaRepository.findAll();
			assertFalse( vendas.isEmpty() );
			
			Venda v = vendas.get( vendas.size()-1 );
			
			Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), dataUtil.apenasData( new Date() ) );
			assertTrue( cop.isPresent() );
			
			Caixa c = cop.get();
			
			RequestBuilder delVenda = MockMvcRequestBuilders.delete( "/api/venda/deleta/{id}", v.getId() );							
			RequestBuilder delCaixa = MockMvcRequestBuilders.delete( "/api/caixa/deleta/{id}", c.getId() );							
			RequestBuilder delFunc = MockMvcRequestBuilders.delete( "/api/funcionario/deleta/{id}", f.getId() );
			
			RequestBuilder delP1 = MockMvcRequestBuilders.delete( "/api/produto/deleta/{id}", prod1.getId());	
			RequestBuilder delP2 = MockMvcRequestBuilders.delete( "/api/produto/deleta/{id}", prod2.getId() );	
									
			mockMvc.perform( delVenda ).andExpect( status().isOk() );
			
			balanco = caixaService.geraCaixaBalancoHoje( uid );
			assertEquals( Double.parseDouble( balanco.getSaldo() ), valorAposVendaRegistrada, 0.01 );
			
			balanco = caixaService.geraCaixaBalancoHoje( uid );
			assertEquals( Double.parseDouble( balanco.getSaldo() ), valorAposLancamentos, 0.01 );
						
			prod1 = produtoRepository.filtraPorDescIni( "P1" ).get( 0 );
			prod2 = produtoRepository.filtraPorDescIni( "P2" ).get( 0 ); 
			
			assertEquals( prod1.getQuantidade(), produto1Quantidade );
			assertEquals( prod2.getQuantidade(), produto2Quantidade );
			
			mockMvc.perform( delCaixa ).andExpect( status().isOk() );
			mockMvc.perform( delFunc ).andExpect( status().isOk() );
			mockMvc.perform( delP1 ).andExpect( status().isOk() );
			mockMvc.perform( delP2 ).andExpect( status().isOk() );			
		} catch (Exception e) {			
			e.printStackTrace();
		}						
	}
	
	public String toJson( Object obj ) {
		try {
			return new ObjectMapper().writeValueAsString( obj );
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	
}
