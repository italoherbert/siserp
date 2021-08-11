import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import UsuarioGrupos from './usuario-grupos';

export default class UsuarioGrupoForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			grupo : {}
		};
		
		this.nome = React.createRef();
	}
	
	componentDidMount() {			
		this.carregar();					
	}
	
	carregar() {
		this.setState( { erroMsg : null, infoMsg : null } );
				
		sistema.showLoadingSpinner();
		
		fetch( '/api/usuario/grupo/get'+this.props.grupoId, {
			method : 'GET',			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { grupo : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );	
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/usuario/grupo/atualiza/"+this.props.usuarioId;
			metodo = 'PUT';									
		} else {
			url = "/api/fornecedor/registra";
			metodo = 'POST';
		}
			
		sistema.showLoadingSpinner();
		
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"nome" : this.nome.current.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Grupo salvo com sucesso." } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );				
	}
	
	paraTelaUsuarioGrupos() {
		ReactDOM.render( <UsuarioGrupos />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<Container>
				<Row>
					<Col className="col-md-8">
						<h4 className="text-center">Registro de grupos</h4>
																
						<Card className="p-3">								
							<Form onSubmit={(e) => this.salvar( e ) }>
								<h4 className="card-title">Dados gerais</h4>
								
								<Form.Group className="mb-2">
									<Form.Label>Nome: </Form.Label>
									<Form.Control type="text" ref={this.nome} name="nome" />
								</Form.Group>
																						
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Button type="submit" variant="primary">Salvar</Button>
							</Form>
						</Card>	
					</Col>
				</Row>
				
				<Row>
					<Col>
						<h4>Permissões sobre recursos</h4>
						
						<Table striped bordered hover>
							<thead>
								<th>Recurso</th>
								<th>Leitura</th>
								<th>Escrita</th>
								<th>Remoção</th>
							</thead>
							<tbody>
								
							</tbody>
						</Table>						
					</Col>
				</Row>
						
				<Row>
					<Col>
						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaUsuarioGrupos( e ) }>Ir para grupos</button>
								</Col>
							</Row>
						</Card>
							
					</Col>
				</Row>
			</Container>
		);
	}
	
}