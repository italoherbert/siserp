import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Recursos from './recursos';

export default class RecursoForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			titulo : ""
		};
		
		this.nome = React.createRef();
	}
	
	componentDidMount() {		
		if ( this.props.op === 'editar' ) {
			this.setState( { titulo : "Alteração de usuário" } );
			this.carregar();				
		} else {
			this.setState( { titulo : "Registro de usuário" } );
		}			
	}
	
	carregar() {
		this.setState( { erroMsg : null, infoMsg : null } );
				
		sistema.showLoadingSpinner();
		
		fetch( '/api/recurso/get/'+this.props.recursoId, {
			method : 'GET',			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.nome.current.value = dados.nome;					
					this.setState( {} );
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
					
		let url, method;
		if ( this.props.op === 'editar' ) {
			url = "/api/recurso/atualiza/"+this.props.recursoId;
			method = 'PUT';
		} else {
			url = "/api/recurso/registra";
			method = 'POST';
		}
					
		sistema.showLoadingSpinner();
		
		fetch( url, {
			method : method,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"nome" : this.nome.current.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Recurso salvo com sucesso." } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );				
	}
			
	paraTelaRecursos() {
		ReactDOM.render( <Recursos />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, titulo } = this.state;
				
		return(
			<Container>
				<h4 className="text-center">{titulo}</h4>
				<br />
				<Row>
					<Col className="col-md-8">																
						<Card className="p-3">								
							<Form onSubmit={(e) => this.salvar( e ) }>
								<h4 className="card-title">Dados gerais</h4>								
								<Row>
									<Col>
										<Form.Group>
											<Form.Label>Nome: </Form.Label>
											<Form.Control type="text" ref={this.nome} name="nome" />
										</Form.Group>																																				
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<Button type="submit" variant="primary">Salvar</Button>
										</Form.Group>
									</Col>
								</Row>
							</Form>
						</Card>	
					</Col>
				</Row>
				
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />						
				
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaRecursos( e ) }>Ir para recursos</button>								
						</Col>
					</Row>
				</Card>
							
			</Container>
		);
	}
	
}