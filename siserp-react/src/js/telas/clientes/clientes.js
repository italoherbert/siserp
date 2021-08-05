import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import ClienteDetalhes from './cliente-detalhes';
import ClienteForm from './cliente-form';

export default class Clientes extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			clientes : []
		};

		this.nomeIni = React.createRef();
	}
	
	componentDidMount() {
		this.nomeIni.current.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { erroMsg : null, infoMsg : null } );

		fetch( "/api/cliente/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				nomeIni : this.nomeIni.current.value
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { clientes : dados } );						
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhum cliente encontrado!" } );												
				} );							
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}										
		} );
	}
		
	detalhes( e, clienteId ) {
		e.preventDefault();
		
		ReactDOM.render( <ClienteDetalhes clienteId={clienteId} />, sistema.paginaElemento() );
	}
	
	remover( e, clienteId ) {
		e.preventDefault();
		
		fetch( "/api/cliente/deleta/"+clienteId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {						
				this.setState( { infoMsg : "Cliente removido com êxito!" } );
				this.filtrar();																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}						
		} );
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <ClienteForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, clientes } = this.state;
		
		return (
			<Container>
				<Row>
					<Col>
						<h4 className="text-center col-md-12">Lista de Clientes</h4>
						
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Nome</th>
										<th>Detalhes</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{clientes.map( ( cliente, index ) => {
										return (
											<tr key={index}>
												<td>{cliente.id}</td>
												<td>{cliente.pessoa.nome}</td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, cliente.id )}>detalhes</button></td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.remover( e, cliente.id )}>remover</button></td>
											</tr>
										);
									} ) }	
								</tbody>							
							</Table>
						</div>
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
															
							<Button type="submit" variant="primary">Filtrar</Button>							
							
							<br />
							<br />
							
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo cliente</button>
						</Form>	
					</Col>
				</Row>									 
			</Container>					
		);
	}
	
}