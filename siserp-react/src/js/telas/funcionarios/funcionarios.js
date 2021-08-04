import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FuncionarioDetalhes from './funcionario-detalhes';
import FuncionarioForm from './funcionario-form';

export default class Funcionarios extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			funcionarios : []
		};

		this.nomeIni = React.createRef();
		this.usernameIni = React.createRef();
	}
	
	componentDidMount() {
		this.nomeIni.current.value = "*";
		this.usernameIni.current.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { erroMsg : null, infoMsg : null } );

		fetch( "/api/funcionario/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				nomeIni : this.nomeIni.current.value,
				usernameIni : this.usernameIni.current.value
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { funcionarios : dados } );						
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhum funcionário encontrado!" } );												
				} );							
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}										
		} );
	}
		
	detalhes( e, funcId ) {
		e.preventDefault();
		
		ReactDOM.render( <FuncionarioDetalhes funcId={funcId}/>, sistema.paginaElemento() );
	}
	
	remover( e, funcId ) {
		e.preventDefault();
		
		fetch( "/api/funcionario/deleta/"+funcId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {						
				this.setState( { infoMsg : "Funcionario removido com êxito!" } );
				this.filtrar();																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}						
		} );
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <FuncionarioForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, funcionarios } = this.state;
		
		return (
			<Container>
				<Row>
					<h4 className="text-center col-md-12">Lista de Funcionarios</h4>
					<Col className="tbl-pnl">
						<Table striped bordered hover>
							<thead>
								<tr>
									<th>ID</th>
									<th>Nome</th>
									<th>Nome de usuário</th>
									<th>Tipo</th>
									<th>Detalhes</th>
									<th>Remover</th>
								</tr>
							</thead>
							<tbody>
								{funcionarios.map( ( func, index ) => {
									return (
										<tr>
											<td>{func.id}</td>
											<td>{func.pessoa.nome}</td>
											<td>{func.usuario.username}</td>
											<td>{func.usuario.tipo}</td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, func.id )}>detalhes</button></td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, func.id )}>remover</button></td>
										</tr>
									);
								} ) }	
							</tbody>							
						</Table>
					</Col>
				</Row>
					
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />	
				
				<Row>
					<Col className="col-md-3"></Col>
					<Col className="col-md-6">
						<Form onSubmit={ (e) => this.filtrar( e ) }>
							<Form.Group className="mb-3">
								<Form.Label>Nome:</Form.Label>
								<Form.Control type="text" ref={this.nomeIni} name="nomeIni" />						
							</Form.Group>
							<Form.Group className="mb-3">
								<Form.Label>Nome de usuário:</Form.Label>
								<Form.Control type="text" ref={this.usernameIni} name="nomeIni" />						
							</Form.Group>
								
							<Button type="submit" variant="primary">Filtrar</Button>							
							
							<br />
							<br />
							
							<button className="btn btn-link" onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo funcionário</button>
						</Form>	
					</Col>
				</Row>									 
			</Container>					
		);
	}
	
}