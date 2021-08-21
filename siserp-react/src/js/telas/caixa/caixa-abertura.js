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
						
	abrir( e ) {
		if ( e != null )
			e.preventDefault();
					
		sistema.wsPost(	"/api/caixa/abre/"+sistema.usuario.id, {
			lancamento : {
				"tipo" : "CREDITO",
				"valor" : sistema.paraFloat( this.valorInicial.current.value )
			}
		}, (request) => {
			this.setState( { infoMsg : "Caixa aberto com sucesso." } );
		}, this );			
					
		this.setState( { infoMsg : null, erroMsg : null } );			
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
											<Form.Control type="text" ref={this.valorInicial} name="valorInicial" />																									
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
						<br />
						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCaixa() }>Ir para tela caixa</button>
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}