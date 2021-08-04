import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaDetalhes from './categoria-detalhes';
import CategoriaForm from './categoria-form';

export default class Categorias extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			addErroMsg : null, 
			addInfoMsg : null,
			erroMsg : null,
			infoMsg : null,
			categorias : [] 
		};			
		
		this.descricaoIni = React.createRef();
	}
	
	componentDidMount() {
		this.descricaoIni.current.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { erroMsg : null, infoMsg : null } );

		fetch( "/api/categoria/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"descricaoIni" : this.descricaoIni.current.value
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { categorias : dados } );
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhuma categoria registrado!" } );						
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <CategoriaDetalhes categoriaId={id}/>, sistema.paginaElemento() );
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		fetch( "/api/categoria/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.setState( { infoMsg : "Categoria removido com êxito!" } );
				this.filtrar();																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
		} );
	}
		
	render() {
		const { erroMsg, infoMsg, categorias } = this.state;
		
		return (
			<Container>				
			
				<Row>
					<Col>
						<CategoriaForm 
							op="cadastrar"
							titulo="Cadastre nova categoria" 
							registrou={ () => this.filtrar(null) } />
					</Col>
				</Row>
				
				<br />
				
				<Row>
					<Col>
						<Card className="p-3">
							<h4 className="text-center">Lista de Categorias</h4>
							<div className="tbl-pnl">
								<Table striped bordered hover>
									<thead>
										<tr>
											<th>ID</th>
											<th>Nome da empresa</th>
											<th>Detalhes</th>
											<th>Remover</th>
										</tr>
									</thead>
									<tbody>
										{categorias.map( ( categoria, index ) => {
											return (
												<tr key={index}>
													<td>{categoria.id}</td>
													<td>{categoria.descricao}</td>
													<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, categoria.id )}>detalhes</button></td>
													<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, categoria.id )}>remover</button></td>
												</tr>
											)
										} ) }	
									</tbody>							
								</Table>
							</div>
							
							<br />
					
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />
							
							<Row>
								<Col className="col-md-3"></Col>
								<Col className="col-md-6">
									<Form onSubmit={ (e) => this.filtrar( e ) }>
										<Form.Group className="mb-2">
											<Form.Label>Descrição:</Form.Label>
											<Form.Control type="text" ref={this.descricaoIni} name="descricaoIni" />						
										</Form.Group>
																																								
										<Button type="submit" variant="primary">Filtrar</Button>				
									</Form>	
								</Col>
							</Row>
						</Card>
					</Col>	
				</Row>
																															
			</Container>					
		);
	}
	
}