import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaAbertura from './caixa-abertura';

export default class Caixa extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			caixa : {}
		};		
	}
		
	atualizarDados() {
		sistema.showLoadingSpinner();
		
		fetch( '/api/caixa/get/uid/hoje/'+sistema.usuario.id, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				resposta.json().this( (dados) => {
					this.setState( { caixa : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	paraTelaAbrirCaixa( e ) {
		ReactDOM.render( <CaixaAbertura />, sistema.paginaElemento() );
	}
		
	render() {
		const {	erroMsg, infoMsg, caixa } = this.state;
				
		return (
			<Container>	
								
				<Row>
					<Col>
						<Form className="float-end">
							<Button variant="primary"  onClick={ (e) => this.paraTelaAbrirCaixa(e) }>Abrir caixa</Button>
						</Form>
					</Col>
				</Row>
				
				<br />
						
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col>
						<Card className="p-3" style={{fontSize: '1.2em'}}>
							<h4>Dados do caixa</h4>
							
							<div className="p-3">
								<div className="display-inline mb-2">
									ID : <span className="text-info">{caixa.id}</span>
								</div>
								
								<div className="display-inline mb-2">
									Data de abertura: <span className="text-info">{ caixa.dataAbertura }</span>
								</div>
																
								<div className="display-inline mb-2">
									Valor em caixa: <span className="text-info">{ caixa.valor }</span>
								</div>																
							</div>
							<div>
								<Form>
									<Button variant="primary" onClick={ (e) => this.atualizarDados( e ) }>Atualizar dados</Button>
								</Form>
							</div>
						</Card>						
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}