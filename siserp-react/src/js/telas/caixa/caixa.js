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
			balanco : { valor : 0, debito : 0, credito : 0, saldo : 0 }
		};		
	}
		
	componentDidMount() {
		this.atualizaDadosHoje();
	}		
		
	atualizaDadosHoje() {
		sistema.showLoadingSpinner();
		
		fetch( '/api/caixa/balanco/'+sistema.usuario.id, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { balanco : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	removeLancamentosHoje( e ) {
		e.preventDefault();
		
		sistema.showLoadingSpinner();
		
		fetch( '/api/caixa/lancamento/deletatodos/hoje/'+sistema.usuario.id, {
			method : 'DELETE',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				this.atualizaDadosHoje();
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
		const {	erroMsg, infoMsg, balanco } = this.state;
				
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
									Data de abertura: <span className="text-danger">{ balanco.dataAbertura }</span>
								</div>
								
								<div className="display-inline mb-2">
									Credito: <span className="text-danger">{ sistema.formataReal( balanco.credito ) }</span>
								</div>	
								
								<div className="display-inline mb-2">
									Debito: <span className="text-danger">{ sistema.formataReal( balanco.debito ) }</span>
								</div>	
											
								<div className="display-inline mb-2">
									Saldo: <span className="text-danger">{ sistema.formataReal( balanco.saldo ) }</span>
								</div>																
							</div>
							<div>
								<Form>
									<Button variant="primary" onClick={ (e) => this.atualizaDadosHoje( e ) }>Atualizar dados</Button>
									<Button variant="primary" className="mx-3" onClick={ (e) => this.removeLancamentosHoje(e) }>Remover lancamentos</Button>									
								</Form>
							</div>												
						</Card>						
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}