import React from 'react';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class FornecedorForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		
		this.empresa = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' )
			this.empresa.current.value = this.props.empresa;					
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/fornecedor/atualiza/"+this.props.fornecedorId;
			metodo = 'PUT';									
		} else {
			url = "/api/fornecedor/registra";
			metodo = 'POST';
		}
				
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"empresa" : this.empresa.current.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Fornecedor salvo com sucesso." } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );				
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Registro de fornecedores</h4>
																
						<Card className="p-3">								
							<Form onSubmit={(e) => this.salvar( e ) }>
								<h4 className="card-title">Dados gerais</h4>
								
								<Form.Group className="mb-2">
									<Form.Label>Empresa: </Form.Label>
									<Form.Control type="text" ref={this.empresa} name="empresa" />
								</Form.Group>
																						
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Button type="submit" variant="primary">Salvar</Button>
							</Form>
						</Card>									
					</Col>
				</Row>
			</Container>
		);
	}
	
}