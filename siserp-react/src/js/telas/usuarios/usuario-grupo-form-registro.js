import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import UsuarioGrupos from './usuario-grupos';

export default class UsuarioGrupoFormRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
		};
		
		this.nome = React.createRef();
	}
		
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
					
		sistema.showLoadingSpinner();
		
		fetch( "/api/usuario/grupo/registra", {
			method : 'POST',			
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
		const { erroMsg, infoMsg, grupo } = this.state;
				
		return(
			<Container>
				<h4 className="text-center">Registro de grupos</h4>
				<br />
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">																
						<Card className="p-3">								
							<Form onSubmit={(e) => this.salvar( e ) }>
								<h4 className="card-title">Dados gerais</h4>								
								<Form.Group className="mb-2">
									<Form.Label>Nome: </Form.Label>
									<Form.Control type="text" ref={this.nome} name="nome" />
								</Form.Group>																																				
								
								<Button type="submit" variant="primary">Salvar</Button>										
							</Form>
						</Card>	
						
						<br />
						
						<MensagemPainel cor="danger" msg={erroMsg} />
						<MensagemPainel cor="primary" msg={infoMsg} />	
										
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