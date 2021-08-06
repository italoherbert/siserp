import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import ClienteForm from './cliente-form';
import Clientes from './clientes';

export default class ClienteDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			cliente : {
				pessoa : {
					endereco : {}
				}
			} 
		};
	}
	
	componentDidMount() {		
		sistema.showLoadingSpinner();
		
		fetch( "/api/cliente/get/"+this.props.clienteId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {											
					this.setState( { cliente : dados } );
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			 
			sistema.hideLoadingSpinner();
		} );
	}
		
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<ClienteForm op="editar" clienteId={this.props.clienteId} />,
				sistema.paginaElemento() );
	}
		
	paraTelaClientes() {
		ReactDOM.render( <Clientes />, sistema.paginaElemento() );
	}
	
	render() {
		const { cliente, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Dados do cliente</h4>																
						
						<Card className="p-3">								
							<h4>Dados gerais</h4>
							
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Nome: </span>
									<span className="text-info">{cliente.pessoa.nome}</span>
								</Col>
							</Row>
							
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Telefone: </span>
									<span className="text-info">{cliente.pessoa.telefone}</span>
								</Col>
							</Row>
							
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">E-Mail: </span>
									<span className="text-info">{cliente.pessoa.email}</span>
								</Col>
							</Row>
						</Card>
						
						<br />
						
						<Card className="p-3">								
							<h4 className="card-title">Endereço</h4>
							
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Ender: </span>
									<span className="text-info">{cliente.pessoa.endereco.ender}</span>
								</Col>
							</Row>
							
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Numero: </span>
									<span className="text-info">{cliente.pessoa.endereco.numero}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">Logradouro: </span>
									<span className="text-info">{cliente.pessoa.endereco.logradouro}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">Bairro: </span>
									<span className="text-info">{cliente.pessoa.endereco.bairro}</span>
								</Col>
							</Row>
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Cidade: </span>
									<span className="text-info">{cliente.pessoa.endereco.cidade}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">UF: </span>
									<span className="text-info">{cliente.pessoa.endereco.uf}</span>
								</Col>																
							</Row>
						</Card>
												
						<br />
						
						<Card className="p-3">								
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />
						
							<Row>
								<Col>
									<Form>
										<Button variant="primary" onClick={(e) => this.editar( e )}>Editar cliente</Button>
									</Form>
									<br />
									<br />
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaClientes( e ) }>Ir para clientes</button>
								</Col>
							</Row>																																								
						</Card>	
					</Col>
				</Row>															
			</Container>
		);
	}

}