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
import italo.siserp.model.FormaPag;
import italo.siserp.model.Funcionario;
import italo.siserp.model.LancamentoTipo;
import italo.siserp.model.Produto;
import italo.siserp.model.Venda;
import italo.siserp.model.request.AbreCaixaRequest;
import italo.siserp.model.request.SaveEnderecoRequest;
import italo.siserp.model.request.SaveFuncionarioRequest;
import italo.siserp.model.request.SaveItemVendaRequest;
import italo.siserp.model.request.SaveLancamentoRequest;
import italo.siserp.model.request.SavePessoaRequest;
import italo.siserp.model.request.SaveProdutoRequest;
import italo.siserp.model.request.SaveUsuarioRequest;
import italo.siserp.model.request.SaveVendaRequest;
import italo.siserp.repository.CaixaRepository;
import italo.siserp.repository.FuncionarioRepository;
import italo.siserp.repository.ProdutoRepository;
import italo.siserp.repository.VendaRepository;
import italo.siserp.util.DataUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "admin", authorities = "ADMIN")
public class FluxoCaixaTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private CaixaRepository caixaRepository;
		
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private DataUtil dataUtil;
		
	@Test
	public void fluxoCaixaTest() {		 																				
		try {			
			SaveFuncionarioRequest req = new SaveFuncionarioRequest();
			req.setPessoa( new SavePessoaRequest() );
			req.setUsuario( new SaveUsuarioRequest() );
			req.getPessoa().setEndereco( new SaveEnderecoRequest() );
			
			req.getPessoa().setNome( "Teste" );
			req.getUsuario().setUsername( "teste" );
			req.getUsuario().setPassword( "teste" );
			req.getUsuario().setTipo( "CAIXA" );
			
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
			
			
			AbreCaixaRequest abreCaixaReq = new AbreCaixaRequest();
			abreCaixaReq.setLancamento( new SaveLancamentoRequest() );
			
			abreCaixaReq.getLancamento().setTipo( LancamentoTipo.CREDITO.toString() );
			abreCaixaReq.getLancamento().setValor( String.valueOf( valorAberturaCaixa ) );
			
			RequestBuilder abreCaixaRB = MockMvcRequestBuilders.post( "/api/caixa/abre/{usuarioId}", uid )
					.content( toJson( abreCaixaReq ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			mockMvc.perform( abreCaixaRB ).andExpect( status().isOk() );
			
			Optional<Caixa> cop = caixaRepository.buscaCaixa( f.getId(), dataUtil.apenasData( new Date() ) );
			assertTrue( cop.isPresent() );
			
			Caixa c = cop.get();
			
			assertEquals( c.getValor(), valorAberturaCaixa );
			
			SaveProdutoRequest p1 = new SaveProdutoRequest();
			p1.setCodigoBarras( "_000" );
			p1.setDescricao( "P1" );
			p1.setPrecoUnitCompra( "15" );
			p1.setPrecoUnitVenda( "18.5" );
			p1.setQuantidade( "80" );			
			
			SaveProdutoRequest p2 = new SaveProdutoRequest();
			p2.setCodigoBarras( "_001" );
			p2.setDescricao( "P2" );
			p2.setPrecoUnitCompra( "40.52" );
			p2.setPrecoUnitVenda( "45.0" );
			p2.setQuantidade( "100" );						
		
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
			saveIVRequest1.setQuantidade( "25" );
			
			SaveItemVendaRequest saveIVRequest2 = new SaveItemVendaRequest();
			saveIVRequest2.setCodigoBarras( "_001" );
			saveIVRequest2.setQuantidade( "48" );
			
			SaveVendaRequest saveVendaRequest = new SaveVendaRequest();
			saveVendaRequest.setItensVenda( new ArrayList<>() );
			saveVendaRequest.setSubtotal( ""+(25 * 18.5 + 48 * 45 ) );
			saveVendaRequest.setDesconto( "0.12" );
			saveVendaRequest.setFormaPag( FormaPag.ESPECIE.toString() );
			saveVendaRequest.setIncluirCliente( "false" );
			saveVendaRequest.setValorPago( "3000" );						
			
			saveVendaRequest.getItensVenda().add( saveIVRequest1 );
			saveVendaRequest.getItensVenda().add( saveIVRequest2 );
			
			RequestBuilder regVendaRB = MockMvcRequestBuilders.post( "/api/venda/efetua/{usuarioId}", uid )
					.content( toJson( saveVendaRequest ) ) 
					.contentType(MediaType.APPLICATION_JSON )
					.accept( MediaType.APPLICATION_JSON );
			
			mockMvc.perform( regVendaRB ).andExpect( status().isOk() );
			
			cop = caixaRepository.buscaCaixa( f.getId(), dataUtil.apenasData( new Date() ) );
			assertTrue( cop.isPresent() );
			
			c = cop.get();
			assertEquals( c.getValor(), valorAberturaCaixa + ( ( 25 * 18.5 + 48 * 45 ) * 0.88 ) );
			
			List<Produto> prod1List = produtoRepository.filtraPorDescIni( "P1" );
			List<Produto> prod2List = produtoRepository.filtraPorDescIni( "P2" );
			
			assertTrue( prod1List.size() == 1 );
			assertTrue( prod2List.size() == 1 );
			
			Produto prod1 = prod1List.get( 0 );
			assertNotNull( prod1 );
			
			Produto prod2 = prod2List.get( 0 );
			assertNotNull( prod2 );
			
			List<Venda> vendas = vendaRepository.findAll();
			assertFalse( vendas.isEmpty() );
			
			Venda v = vendas.get( vendas.size()-1 );
			
			RequestBuilder delVenda = MockMvcRequestBuilders.delete( "/api/venda/deleta/{id}", v.getId() );							
			RequestBuilder delCaixa = MockMvcRequestBuilders.delete( "/api/caixa/deleta/{id}", c.getId() );							
			RequestBuilder delFunc = MockMvcRequestBuilders.delete( "/api/funcionario/deleta/{id}", f.getId() );
			
			RequestBuilder delP1 = MockMvcRequestBuilders.delete( "/api/produto/deleta/{id}", prod1.getId());	
			RequestBuilder delP2 = MockMvcRequestBuilders.delete( "/api/produto/deleta/{id}", prod2.getId() );	
			
			mockMvc.perform( delVenda ).andExpect( status().isOk() );
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
