import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaLancamentos from './caixa-lancamentos';

export default class CaixaLancamentoNovo extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			
			tipos : []
		};		
		
		this.tipo = React.createRef();
		this.valor = React.createRef();
		this.obs = React.createRef();
	}
	
	componentDidMount() {
		this.carregaTipos();
				
		this.valor.current.value = "";	
		this.obs.current.value = "";
	}
	
	carregaTipos() {
		sistema.showLoadingSpinner();
		
		fetch( '/api/lancamento/tipos', {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { tipos : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
						
	novo( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { infoMsg : null, erroMsg : null } );
		
		sistema.showLoadingSpinner();

		fetch( "/api/lancamento/novo/hoje/"+sistema.usuario.id, {
			method : "POST",			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8", 
				"Authorization" : "Bearer "+sistema.token
			}, 
			body : JSON.stringify( {
				tipo : this.tipo.current.value,
				valor : sistema.paraFloat( this.valor.current.value ),				
				obs : this.obs.current.value
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.setState( { infoMsg : "Lançamento efetuado com sucesso." } );																							
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}		
			sistema.hideLoadingSpinner();			
		} );
	}
	
	paraTelaLancamentos() {
		ReactDOM.render( <CaixaLancamentos />, sistema.paginaElemento() );
	}
			
	render() {
		const {	erroMsg, infoMsg, tipos } = this.state;
				
		return (
			<Container>																								
				<Row>
					<Col className="col-md-3"></Col>
					<Col className="col-md-6">
						<Card className="p-3">
							<h4>Registre um lançamento</h4>
							<Form onSubmit={ (e) => this.novo( e ) }>																					
								<Form.Group className="py-2 pb-2">
									<Form.Label>Tipo: &nbsp; </Form.Label>
									<select name="tipo" ref={this.tipo}>
										<option key="0" value="NONE">Selecione um tipo!</option>
										{ tipos.map( (item, i) => {
											return <option key={i+1} value={item}>{item}</option>
										} )	}
									</select>
								</Form.Group>
																
								<Form.Group className="mb-2">													
									<Form.Label>Valor: </Form.Label>
									<Form.Control type="text" ref={this.valor} name="valor" />																									
								</Form.Group>
								
								<Form.Group className="mb-2">
									<Form.Label>Observações: </Form.Label>
									<Form.Control as="textarea" rows="3" ref={this.obs} />
								</Form.Group>
								
								<Button type="submit" variant="primary">Registrar novo</Button>														
							</Form>
									
							<br />
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />
								
						</Card>	
						
						<br />
						
						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaLancamentos() }>Ir para lançamentos</button>
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}