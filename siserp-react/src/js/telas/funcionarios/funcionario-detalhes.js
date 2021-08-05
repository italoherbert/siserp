import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FuncionarioForm from './funcionario-form';

export default class FuncionarioDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			funcionario : {
				pessoa : {
					endereco : {}
				},
				usuario : {}
			} 
		};
	}
	
	componentDidMount() {		
		fetch( "/api/funcionario/get/"+this.props.funcId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {											
					this.setState( { funcionario : dados } );
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			 
		} );
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<FuncionarioForm op="editar" funcId={this.props.funcId} />,
				sistema.paginaElemento() );
	}
	
	render() {
		const { funcionario, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">						
						<Card className="p-3">
							<h4>Dados do funcionário</h4>																
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Nome: </span>
									<span className="text-info">{funcionario.pessoa.nome}</span>
								</Col>
							</Row>
							
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Telefone: </span>
									<span className="text-info">{funcionario.pessoa.telefone}</span>
								</Col>
							</Row>
							
							<Row  className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">E-Mail: </span>
									<span className="text-info">{funcionario.pessoa.email}</span>
								</Col>
							</Row>
						</Card>
						
						<br />
						
						<Card className="p-3">								
							<h4>Endereço</h4>
							<Row  className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Ender: </span>
									<span className="text-info">{funcionario.pessoa.endereco.ender}</span>
								</Col>
							</Row>
							
							<Row  className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Numero: </span>
									<span className="text-info">{funcionario.pessoa.endereco.numero}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">Logradouro: </span>
									<span className="text-info">{funcionario.pessoa.endereco.logradouro}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">Bairro: </span>
									<span className="text-info">{funcionario.pessoa.endereco.bairro}</span>
								</Col>
							</Row>
							<Row  className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Cidade: </span>
									<span className="text-info">{funcionario.pessoa.endereco.cidade}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">UF: </span>
									<span className="text-info">{funcionario.pessoa.endereco.uf}</span>
								</Col>																
							</Row>
						</Card>
						
						<br />
						
						<Card className="p-3">
							<h4>Dados usuario</h4>
							
							<Row  className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Username: </span>
									<span className="text-info">{funcionario.usuario.username}</span>
								</Col>
								<Col>														
									<span className="text-dark font-weight-bold">Tipo: </span>
									<span className="text-info">{funcionario.usuario.tipo}</span>
								</Col>
							</Row>																
						</Card>
						
						<br />
						
						<Card className="p-3">											
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />
						
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={(e) => this.editar( e )}>Editar funcionario</button>
								</Col>
							</Row>																																																		
						</Card>												
					</Col>
				</Row>															
			</Container>
		);
	}

}