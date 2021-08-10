import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Funcionarios from './funcionarios';

export default class FuncionarioForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			uTipos : []
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
		
		this.tipo = React.createRef();
		this.username = React.createRef();		
		this.password = React.createRef();		
		this.password2 = React.createRef();
	}
	
	componentDidMount() {			
		this.carregaTipos();
		
		if ( this.props.op === 'editar' )
			this.carregaFuncionario();		
	}
	
	carregaFuncionario() {
		fetch( '/api/funcionario/get/'+this.props.funcId, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
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
					
					this.username.current.value = dados.usuario.username;
					this.tipo.current.value = dados.usuario.tipo;
					
					this.setState( {} );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
		
	carregaTipos() {
		fetch( "/api/usuario/tipos", {
			method : "GET",			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.setState( { uTipos : dados } );										
			} );			 
		} );
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/funcionario/atualiza/"+this.props.funcId;
			metodo = 'PUT';									
		} else {
			url = "/api/funcionario/registra";
			metodo = 'POST';
		}
				
		sistema.showLoadingSpinner();
		
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
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
				},
				
				"usuario" : {
					"username" : this.username.current.value,
					"password" : this.password.current.value,
					"tipo" : this.tipo.current.value	
				}
			} )		
		} ).then( (resposta) => {						
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Funcionario salvo com sucesso." } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );				
	}
	
	paraTelaFuncionarios() {
		ReactDOM.render( <Funcionarios />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, uTipos } = this.state;
				
		return(
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Registro de funcionarios</h4>
																
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
											<Form.Control type="text" ref={this.ender} name="ender" />
										</Form.Group>
									</Col>
								</Row>
								
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Numero:</Form.Label>
											<Form.Control type="text" ref={this.numero} name="numero" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>Logradouro:</Form.Label>
											<Form.Control type="text" ref={this.logradouro} name="logradouro" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>Bairro:</Form.Label>
											<Form.Control type="text" ref={this.bairro} name="bairro" />
										</Form.Group>
									</Col>
								</Row>
																	
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Cidade:</Form.Label>
											<Form.Control type="text" ref={this.cidade} name="cidade" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>UF:</Form.Label>
											<Form.Control type="text" ref={this.uf} name="uf" />							
										</Form.Group>
									</Col>
								</Row>
							</Card>
							
							<br />
							
							<Card className="p-3">
								<h4 className="card-title">Usuario:</h4>
								
								<Row className="mb-2">
									<Col>
										<Form.Group>
											<Form.Label>Nome de usuário:</Form.Label>
											<Form.Control type="text" ref={this.username} name="username" />
										</Form.Group>
									</Col>									
									<Col>
										<Form.Group>
											<Form.Label>Tipos:</Form.Label>
											<select name="tipo" ref={this.tipo}>
												<option key="0" value="NONE">Selecione um tipo!</option>
												{ uTipos.map( (item, i) => {
													return <option key={i+1} value={item}>{item}</option>
												} )	}
											</select>
										</Form.Group>
									</Col>																				
								</Row>
								<Row className="mb-2">
									<Col>																			
										<Form.Label>Senha:</Form.Label>
										<Form.Control type="password" ref={this.password} name="password" />
									</Col>
									<Col>
										<Form.Label>Repita a senha:</Form.Label>
										<Form.Control type="password" ref={this.password2} name="password2" />							
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
										<br />
										<br />
										<button className="btn btn-link p-0" onClick={(e) => this.paraTelaFuncionarios(e) }>Ir para funcionarios</button>
									</Col>
								</Row>																
							</Card>									
						</Form>
					</Col>
				</Row>
			</Container>
		);
	}
	
}