import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Table, Form, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import ProdutoDetalhes from './produto-detalhes';
import ProdutoForm from './produto-form';

import Categorias from './../categorias/categorias';

export default class Produtos extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			produtos : [],
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {}
		};		
		this.descricaoIni = React.createRef();
		this.codigoBarras = React.createRef();
	}
	
	componentDidMount() {
		this.descricaoIni.current.value = "*";
		
		this.filtrar( null, false );		
	}
	
	filtrar( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();
					
		let descIni = this.descricaoIni.current.value;

		sistema.wsGet( "/api/produto/filtra/"+descIni, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { produtos : dados } );
				
				if ( dados.length === 0 && filtrarBTClicado === true )
					this.setState( { infoMsg : "Nenhuma produto encontrado!" } );						
			} );
		}, this );
	}
	
	buscar( e ) {
		if ( e != null )
			e.preventDefault();
							
		let codBarras = this.codigoBarras.current.value; 

		sistema.wsGet( "/api/produto/busca/"+codBarras, (resposta) => {	
			resposta.json().then( (dados) => {										
				this.codigoBarras.current.value = "";
				
				this.setState( { produtos : [ dados ] } );
														
				if ( dados.length === 0 )
					this.setState( { infoMsg : "Nenhuma produto encontrado!" } );						
			} );
		}, this );
	}
	
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <ProdutoDetalhes produtoId={id}/>, sistema.paginaElemento() );
	}
	
	removerSeConfirmado( e, id ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.remover( e, id );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		sistema.wsDelete( "/api/produto/deleta/"+id, (resposta) => {
			this.filtrar( null, false );																	
			this.setState( { infoMsg : "Produto removido com êxito!" } );
		}, this );		
	}
	
	paraCadastroForm( e ) {
		e.preventDefault();
		
		ReactDOM.render( 
			<ProdutoForm op="cadastrar" titulo="Cadastre um novo produto" />, sistema.paginaElemento() );
	}
	
	paraCategorias( e ) {
		e.preventDefault();
		
		ReactDOM.render( <Categorias />, sistema.paginaElemento() );
	}
		
	render() {
		const { erroMsg, infoMsg, produtos, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return (
			<Container>			
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de produto</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o produto selecionado?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
				
				<Row>
					<Col>
						<Form>
							<Button  className="float-start" variant="primary" onClick={ (e) => this.paraCategorias( e ) } >Categorias</Button>
							<Button  className="float-end" variant="primary" onClick={ (e) => this.paraCadastroForm( e ) } >Cadastre um novo produto</Button>
						</Form>
					</Col>
				</Row>
				
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
										<th>Quantidade</th>											
										<th>Detalhes</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{produtos.map( ( item, index ) => {
										return (
											<tr key={index}>
												<td>{item.id}</td>
												<td>{item.descricao}</td>
												<td>{item.codigoBarras}</td>
												<td>{ sistema.formataReal( item.precoUnitCompra ) }</td>
												<td>{ sistema.formataReal( item.precoUnitVenda ) }</td>
												<td>{ sistema.formataFloat( item.quantidade ) } {item.unidade}</td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, item.id )}>detalhes</button></td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, item.id )}>remover</button></td>
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
							<Col>
								<Card className="p-3">
									<h4>Filtrar produtos</h4>
									<Row>
										<Col className="col-sm-6">
											<Form onSubmit={ (e) => this.filtrar( e, true ) }>
												<Form.Group className="mb-2">
													<Form.Label>Descrição:</Form.Label>
													<Form.Control type="text" ref={this.descricaoIni} name="descricaoIni" />						
												</Form.Group>	
												
												<Button type="submit" variant="primary">Filtrar</Button>				
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
								</Card>
							</Col>
						</Row>
					</Col>
				</Row>
			</Container>					
		);
	}
	
}