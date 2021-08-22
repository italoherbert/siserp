import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import LogoPainel from './../../componente/logo-painel';
import sistema from './../../logica/sistema';

export default class ConfigsForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		
		this.logoFile = React.createRef();
	}
		
	salvar( e ) {
		e.preventDefault();
		
		let files = this.logoFile.current.files;
		if ( files.length === 0 ) {
			this.setState( { erroMsg : 'Nenhum arquivo de imagem selecionado.' } );
			return;
		}
		
		let reader = new FileReader();
		reader.onload = (e) => {			
			sistema.wsPut( '/api/config/logo/salva', {
				"logoBase64" : e.target.result
			}, (resposta) => {		
				resposta.json().then( (dados) => {
					this.setState( { infoMsg : 'Logomarca salva com sucesso!' } );
				
					ReactDOM.render( <LogoPainel src={dados.logoBase64} />, sistema.logoElemento() );
				} );
			}, this );
		};
		reader.readAsDataURL( files[0] );		
	}
		
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Configurações</h4>
						
						<Card className="p-3">																		
							<h4>Altere a logomarca</h4>
							
							<Form onSubmit={(e) => this.salvar( e ) }>									
								<Form.Group className="mb-2">
									<Form.Label>Logomarca</Form.Label>
									<Form.Control type="file" ref={this.logoFile} name="cnpj" />
								</Form.Group>

								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Button type="submit" variant="primary" className="my-1">Salvar logomarca</Button>								
							</Form>															
						</Card>
					</Col>
				</Row>
			</Container>
		);
	}
	
}