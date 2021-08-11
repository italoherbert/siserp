import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import UsuarioGrupos from './usuario-grupos';

export default class UsuarioGrupoForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			grupo : { permissaoGrupos : [] }
		};
		
		this.nome = React.createRef();
	}
	
	componentDidMount() {			
		this.carregar();					
	}
	
	carregar() {
		this.setState( { erroMsg : null, infoMsg : null } );
				
		sistema.showLoadingSpinner();
		
		fetch( '/api/usuario/grupo/get/'+this.props.grupoId, {
			method : 'GET',			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { grupo : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );	
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/usuario/grupo/atualiza/"+this.props.grupoId;
			metodo = 'PUT';									
		} else {
			url = "/api/usuario/grupo/registra";
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
				"nome" : this.nome.current.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Grupo salvo com sucesso." } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );				
	}
	
	paraTelaUsuarioGrupos() {
		ReactDOM.render( <UsuarioGrupos />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, grupo } = this.state;
				
		return(
			<Container>
				<h4 className="text-center">Registro de grupos</h4>
				<br />
				<Row>
					<Col className="col-md-8">
																
						<Card className="p-3">								
							<Form onSubmit={(e) => this.salvar( e ) }>
								<h4 className="card-title">Dados gerais</h4>								
								<Row>
									<Col>
										<Form.Group>
											<Form.Label>Nome: </Form.Label>
											<Form.Control type="text" ref={this.nome} name="nome" />
										</Form.Group>																																				
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<Button type="submit" variant="primary">Salvar</Button>
										</Form.Group>
									</Col>
								</Row>
							</Form>
						</Card>	
					</Col>
				</Row>
				
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />	
				
				<Row>
					<Col>
						<h4 className="text-center">Permissões sobre recursos</h4>
						
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Recurso</th>
										<th>Leitura</th>
										<th>Escrita</th>
										<th>Remoção</th>
									</tr>
								</thead>
								<tbody>
									{grupo.permissaoGrupos.map( (item, index) => {
										return (
											<tr key={index}>
												<td>{item.recurso}</td>
												<td><input type="checkbox" checked={item.leitura} onChange={ (e) => this.permissaoLeituraOnChange( e, item.id ) } /></td>
												<td><input type="checkbox" checked={item.escrita} onChange={ (e) => this.permissaoEscritaOnChange( e, item.id ) } /></td>
												<td><input type="checkbox" checked={item.remocao} onChange={ (e) => this.permissaoRemocaoOnChange( e, item.id ) } /></td>
											</tr>
										)
									} ) }
								</tbody>
							</Table>			
						</div>
					</Col>
				</Row>
				
				<br />
				
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaUsuarioGrupos( e ) }>Ir para grupos</button>								
						</Col>
					</Row>
				</Card>
							
			</Container>
		);
	}
	
}