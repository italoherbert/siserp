import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Clientes from './clientes';

export default class ClienteForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		
		this.nome = React.createRef();
		this.telefone = React.createRef();
		this.email = React.createRef();
		
		this.ender = React.createRef();
		this.numero = React.createRef();
		this.logradouro = React.createRef();
		this.bairro = React.createRef();
		this.cidade = React.createRef();
		this.uf = React.createRef();				
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' )
			this.carregaCliente();		
	}
	
	carregaCliente() {
		sistema.wsGet( '/api/cliente/get/'+this.props.clienteId, (resposta) => {
			resposta.json().then( (dados) => {
				this.nome.current.value = dados.pessoa.nome;
				this.telefone.current.value = dados.pessoa.telefone;
				this.email.current.value = dados.pessoa.email;
				
				this.ender.current.value = dados.pessoa.endereco.ender;
				this.numero.current.value = dados.pessoa.endereco.numero;
				this.logradouro.current.value = dados.pessoa.endereco.logradouro;
				this.bairro.current.value = dados.pessoa.endereco.bairro;
				this.cidade.current.value = dados.pessoa.endereco.cidade;
				this.uf.current.value = dados.pessoa.endereco.uf;
									
				this.setState( {} );
			} );
		}, this );		
	}
			
	salvar( e ) {
		e.preventDefault();
				
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/cliente/atualiza/"+this.props.clienteId;
			metodo = 'PUT';									
		} else {
			url = "/api/cliente/registra";
			metodo = 'POST';
		}
		
		sistema.wsSave( url, metodo, {
			"pessoa" : {
				"nome" : this.nome.current.value,
				"telefone" : this.telefone.current.value,				
				"email" : this.email.current.value,
				
				"endereco" : {
					"ender" : this.ender.current.value,
					"numero" : this.numero.current.value,
					"bairro" : this.bairro.current.value,
					"cidade" : this.cidade.current.value,
					"uf" : this.uf.current.value,
					"logradouro" : this.logradouro.current.value				
				}
			}	
		}, (resposta) => {
			this.setState( { infoMsg : "Cliente salvo com sucesso." } );
		}, this );					
	}
	
	
	paraTelaClientes() {
		ReactDOM.render( <Clientes />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Registro de cliente</h4>
																
						<Form onSubmit={(e) => this.salvar( e ) }>
							<Card className="p-3">																
								<h4 className="card-title">Dados gerais</h4>
								
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Nome: </Form.Label>
											<Form.Control type="text" ref={this.nome} name="nome" />
										</Form.Group>
									</Col>
								</Row>
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Telefone: </Form.Label>
											<Form.Control type="tel" ref={this.telefone} name="tel" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>E-Mail: </Form.Label>
											<Form.Control type="email" ref={this.email} name="email" />						
										</Form.Group>
									</Col>
								</Row>
							</Card>
							
							<br />
												
							<Card className="p-3">								
								<h4 className="card-title">Endereço</h4>
								
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Ender:</Form.Label>
											<Form.Control type="texto" ref={this.ender} name="ender" />
										</Form.Group>
									</Col>
								</Row>
								
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Numero:</Form.Label>
											<Form.Control type="texto" ref={this.numero} name="numero" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>Logradouro:</Form.Label>
											<Form.Control type="texto" ref={this.logradouro} name="logradouro" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>Bairro:</Form.Label>
											<Form.Control type="texto" ref={this.bairro} name="bairro" />
										</Form.Group>
									</Col>
								</Row>
																	
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Cidade:</Form.Label>
											<Form.Control type="texto" ref={this.cidade} name="cidade" size="30" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>UF:</Form.Label>
											<Form.Control type="texto" ref={this.uf} name="uf" size="10" />							
										</Form.Group>
									</Col>
								</Row>
							</Card>
							
							<br />
																
							<Card className="p-3">																
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Row>
									<Col>
										<Button type="submit" variant="primary">Salvar</Button>
									</Col>
								</Row>
							</Card>									
						</Form>
					</Col>
				</Row>
								
				<br />
				
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaClientes( e ) }>Ir para clientes</button>
						</Col>
					</Row>
				</Card>
					
			</Container>
		);
	}
	
}