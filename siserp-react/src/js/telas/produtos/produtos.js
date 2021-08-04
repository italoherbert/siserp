import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import ProdutoDetalhes from './produto-detalhes';
import ProdutoForm from './produto-form';

export default class Produtos extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			produtos : [] 
		};		
		this.descricaoIni = React.createRef();
		this.codigoBarras = React.createRef();
	}
	
	componentDidMount() {
		this.descricaoIni.current.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { erroMsg : null, infoMsg : null } );

		let descIni = this.descricaoIni.current.value;

		fetch( "/api/produto/filtra/"+descIni, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { produtos : dados } );
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhuma produto encontrado!" } );						
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	buscar( e ) {
		if ( e != null )
			e.preventDefault();
							
		this.setState( { erroMsg : null, infoMsg : null } );

		let codBarras = this.codigoBarras.current.value; 

		fetch( "/api/produto/busca/"+codBarras, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {										
					this.state.produtos.push( dados );
											
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhuma produto encontrado!" } );						
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <ProdutoDetalhes produtoId={id}/>, sistema.paginaElemento() );
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		fetch( "/api/produto/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.setState( { infoMsg : "Produto removido com êxito!" } );
				this.filtrar( null );																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
		} );
	}
	
	paraCadastroForm( e ) {
		e.preventDefault();
		
		ReactDOM.render( 
			<ProdutoForm op="cadastrar" titulo="Cadastre um novo produto" />, 
			sistema.paginaElemento() );
	}
		
	render() {
		const { erroMsg, infoMsg, produtos } = this.state;
		
		return (
			<Container>												
				<Card className="p-3">
					<Row>
						<Col>
							<h4 className="text-center">Lista de Produtos</h4>
							<div className="tbl-pnl">
								<Table striped bordered hover>
									<thead>
										<tr>
											<th>ID</th>
											<th>Descrição</th>
											<th>Código de barras</th>
											<th>Preço compra</th>
											<th>Preço venda</th>
											<th>Unidade</th>
											<th>Detalhes</th>
											<th>Remover</th>
										</tr>
									</thead>
									<tbody>
										{produtos.map( ( produto, index ) => {
											return (
												<tr key={index}>
													<td>{produto.id}</td>
													<td>{produto.descricao}</td>
													<td>{produto.codigoBarras}</td>
													<td>{produto.precoUnitCompra}</td>
													<td>{produto.precoUnitVenda}</td>
													<td>{produto.unidade}</td>
													<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, produto.id )}>detalhes</button></td>
													<td><button className="btn btn-link p-0" onClick={(e) => this.remover( e, produto.id )}>remover</button></td>
												</tr>
											);
										} ) }	
									</tbody>							
								</Table>
							</div>
							
							<br />
					
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />
							
							<Row>
								<Col className="col-md-1"></Col>
								<Col className="col-md-10">
									<Row>
										<Col className="col-sm-6">
											<Form onSubmit={ (e) => this.filtrar( e ) }>
												<Form.Group className="mb-2">
													<Form.Label>Descrição:</Form.Label>
													<Form.Control type="text" ref={this.descricaoIni} name="descricaoIni" />						
												</Form.Group>	
												
												<Button type="submit"variant="primary">Filtrar</Button>				
											</Form>
										</Col>
										<Col className="col-sm-6">
											<Form onSubmit={ (e) => this.buscar( e ) }>
												<Form.Group className="mb-2">
													<Form.Label>Codigo de barras:</Form.Label>
													<Form.Control type="text" ref={this.codigoBarras} name="codigoBarras" />						
												</Form.Group>	
												
												<Button type="submit"variant="primary">Buscar</Button>				
											</Form>
										</Col>
									</Row>
									
									<br />																
																										
									<button className="btn btn-link" onClick={ (e) => this.paraCadastroForm( e ) } >Cadastre um novo produto</button>
								</Col>
							</Row>
						</Col>
					</Row>
				</Card>																															
			</Container>					
		);
	}
	
}