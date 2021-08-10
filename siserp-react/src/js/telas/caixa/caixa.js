import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaAbertura from './caixa-abertura';
import CaixaLancamentos from './caixa-lancamentos';

export default class Caixa extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			balanco : { valor : 0, debito : 0, credito : 0, saldo : 0 }
		};		
		
		this.valorFechamento = React.createRef();
	}
		
	componentDidMount() {
		this.atualizaDadosHoje();
	}		
		
	atualizaDadosHoje() {
		sistema.showLoadingSpinner();
		
		fetch( '/api/lancamento/balanco/hoje/'+sistema.usuario.id, {
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
		
	fecharCaixa( e ) {
		if ( e != null )
			e.preventDefault();
		
		sistema.showLoadingSpinner();
		
		fetch( '/api/lancamento/novo/hoje/'+sistema.usuario.id, {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				tipo : "DEBITO",
				valor : sistema.paraFloat( this.valorFechamento.current.value )
			} )
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				this.atualizaDadosHoje();
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	visualizarLancamentos( e ) {
		e.preventDefault();
		
		ReactDOM.render( <CaixaLancamentos />, sistema.paginaElemento() );
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
									<Button variant="primary" className="mx-3" onClick={ (e) => this.visualizarLancamentos( e ) }>Lançamentos</Button>
								</Form>
							</div>												
						</Card>						
					</Col>
					<Col>						
						<Card className="p-3">
							<h4>Fechar caixa</h4>
							<Form onSubmit={ (e) => this.fecharCaixa( e ) }>
								<Form.Group className="mb-2">
									<Form.Label>Informe o valor em caixa: </Form.Label>
									<Form.Control type="text" ref={this.valorFechamento} name="valorFechamento" />
								</Form.Group>
								<Button type="submit" variant="primary">Fechar caixa</Button>
							</Form>
						</Card>																	
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}