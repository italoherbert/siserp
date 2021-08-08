import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Caixa from './caixa';

export default class CaixaAbertura extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null
		};		
		
		this.valorInicial = React.createRef();
	}
						
	abrir( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { infoMsg : null, erroMsg : null } );
		
		let valorInicial = this.valorInicial.current.value;
		if ( isNaN( valorInicial ) === true ) {
			this.setState( { erroMsg : "Valor inicial em formato inválido." } );
			return;
		}
		
		sistema.showLoadingSpinner();

		fetch( "/api/caixa/abre/"+sistema.usuario.id, {
			method : "POST",			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8", 
				"Authorization" : "Bearer "+sistema.token
			}, 
			body : JSON.stringify( {
				lancamento : {
					"tipo" : "CREDITO",
					"valor" : sistema.paraFloat( valorInicial )
				}
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { infoMsg : "Caixa aberto com sucesso." } );																							
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}		
			sistema.hideLoadingSpinner();			
		} );
	}
	
	paraTelaCaixa() {
		ReactDOM.render( <Caixa />, sistema.paginaElemento() );
	}
			
	render() {
		const {	erroMsg, infoMsg} = this.state;
				
		return (
			<Container>																								
				<Row>
					<Col>
						<Card className="p-3">
							<h4>Abertura de caixa</h4>
							<Form onSubmit={ (e) => this.abrir( e, true ) }>													
								<Row>
									<Col className="col-md-6">										
										<Form.Group className="mb-2">													
											<Form.Label>Valor inicial: </Form.Label>
											<Form.Control type="number" step="0.01" ref={this.valorInicial} name="valorInicial" />																									
										</Form.Group>
									</Col>									
								</Row>
								
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Row>
									<Col>										
										<Button type="submit" variant="primary">Abrir caixa</Button>														
									</Col>
								</Row>
							</Form>						
						</Card>						
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}