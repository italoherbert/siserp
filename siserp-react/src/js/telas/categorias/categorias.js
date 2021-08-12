import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

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
			categorias : [],			
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {}
		};			
		
		this.descricaoIni = React.createRef();
	}
	
	componentDidMount() {
		this.descricaoIni.current.value = "*";
		
		this.filtrar( null, true );		
	}
	
	filtrar( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { erroMsg : null, infoMsg : null } );

		sistema.showLoadingSpinner();

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
					
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhuma categoria registrada!" } );						
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
			sistema.hideLoadingSpinner();	
		} );
	}
	
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <CategoriaDetalhes categoriaId={id}/>, sistema.paginaElemento() );
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
		
		sistema.showLoadingSpinner();
		
		fetch( "/api/categoria/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.filtrar( null, false );																	
				this.setState( { infoMsg : "Categoria removida com êxito!" } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	render() {
		const { erroMsg, infoMsg, categorias, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return (
			<Container>				
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de categoria</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover a categoria selecionada?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
					
				<h4 className="text-center">Lista de Categorias</h4>
				<div className="tbl-pnl">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>ID</th>
								<th>Descrição</th>
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
										<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, categoria.id )}>detalhes</button></td>
										<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, categoria.id )}>remover</button></td>
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
					<Col>
						<Card className="p-3">
							<h4>Filtro de categorias</h4>
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Form.Group className="mb-2">
									<Form.Label>Descrição:</Form.Label>
									<Form.Control type="text" ref={this.descricaoIni} name="descricaoIni" />						
								</Form.Group>
																																						
								<Button type="submit" variant="primary">Filtrar</Button>				
							</Form>	
						</Card>
					</Col>
					<Col>								
						<CategoriaForm 
							op="cadastrar"
							titulo="Cadastre nova categoria" 
							registrou={ () => this.filtrar( null, false) } />							
					</Col>
				</Row>
																													
			</Container>					
		);
	}
	
}