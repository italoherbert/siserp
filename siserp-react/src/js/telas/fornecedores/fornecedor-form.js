import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Fornecedores from './fornecedores';

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
			this.carrega();							
	}
	
	carrega() {
		sistema.wsGet( '/api/fornecedor/get/'+this.props.fornecedorId, (resposta) => {
			resposta.json().then( (dados) => {
				this.empresa.current.value = dados.empresa;
			} );
		}, this );
	}
	
	salvar( e ) {
		e.preventDefault();
				
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/fornecedor/atualiza/"+this.props.fornecedorId;
			metodo = 'PUT';									
		} else {
			url = "/api/fornecedor/registra";
			metodo = 'POST';
		}
		
		sistema.wsSave( url, metodo, {
			"empresa" : this.empresa.current.value
		}, (resposta) => {
			this.setState( { infoMsg : "Fornecedor salvo com sucesso." } );
		}, this );						
	}
	
	paraTelaFornecedores() {
		ReactDOM.render( <Fornecedores />, sistema.paginaElemento() );
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
						
						<br />						
						
						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaFornecedores( e ) }>Ir para fornecedores</button>
								</Col>
							</Row>
						</Card>
							
					</Col>
				</Row>
			</Container>
		);
	}
	
}