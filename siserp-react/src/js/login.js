import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './logica/sistema';
import MensagemPainel from './componente/mensagem-painel';

import Layout from './layout.js';
import NavegBar from './naveg-bar.js';
import Inicial from './telas/inicial/inicial.js';

export default class Login extends React.Component {

	constructor( props ) {
		super( props );
		this.state = { erroMsg : null };
		
		this.username = React.createRef();
		this.password = React.createRef();
	}
		
	entrar(e) {			
		e.preventDefault();	
												
		this.setState( { erroMsg : null } );

		fetch( "/api/login/entrar", {
			method : "POST",			
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
			},				
			body : JSON.stringify( { 
				username : this.username.current.value,
				password : this.password.current.value
			} )
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					sistema.token = dados.token;
					sistema.usuario = dados.usuario;
					
					console.log( dados.token );
										
					ReactDOM.render( <Layout />, sistema.rootElemento() );				
					ReactDOM.render( <NavegBar />, sistema.menuNavegElemento() );				
					ReactDOM.render( <Inicial />, sistema.paginaElemento() );								
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			 
		} );
	}
	
	
	render() {
		const { erroMsg } = this.state;
								
		return (
			<div>				
				<h1 className="text-center bg-dark text-white p-4">Sistema ERP</h1>						
				<Container className="my-5">
					<Row>
						<Col></Col>
						<Col>
							<Card body>
								<h4 className="card-title">Tela de login</h4>														
								<Form onSubmit={(e) => this.entrar( e )}>
									<Form.Group className="mb-3">
										<Form.Label>Nome de usuário</Form.Label>
										<Form.Control type="text" ref={this.username} id="username" name="username" />						
									</Form.Group>
									<Form.Group className="mb-3">
										<Form.Label>Senha</Form.Label>
										<Form.Control type="password" ref={this.password} id="password" name="password" />						
									</Form.Group>
									<Form.Group className="mb-3">
										<Button type="submit" color="primary">Entrar</Button>				
									</Form.Group>
													
									<Form.Group className="mb-3">
										<MensagemPainel cor="danger" msg={erroMsg} />
									</Form.Group>
								</Form>	
							</Card>
						</Col>
						<Col></Col>
					</Row>			
				</Container>	
			</div>
		);
	}
}
