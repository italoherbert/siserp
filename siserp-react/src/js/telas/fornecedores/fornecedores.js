import React from 'react';
import ReactDOM from 'react-dom';
import {Container, Row, Col, Form, Table, Button} from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FornecedorDetalhes from './fornecedor-detalhes';
import FornecedorForm from './fornecedor-form';

export default class Fornecedores extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			fornecedores : [] 
		};			
		
		this.empresaIni = React.createRef();
	}
	
	componentDidMount() {
		this.empresaIni.current.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();

		this.setState( { erroMsg : null, infoMsg : null } );

		fetch( "/api/fornecedor/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"empresaIni" : this.empresaIni.current.value,
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { fornecedores : dados } );
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhum fornecedor registrado!" } );																							
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
		
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <FornecedorDetalhes fornecedorId={id}/>, sistema.paginaElemento() );
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		fetch( "/api/fornecedor/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.setState( { infoMsg : "Fornecedor removido com êxito!" } );
				this.filtrar();																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <FornecedorForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, fornecedores } = this.state;
		
		return (
			<Container>
				<Row>
					<h4 className="text-center col-md-12">Lista de Fornecedores</h4>
					<Col className="tbl-pnl">
						<Table striped bordered hover>
							<thead>
								<tr>
									<th>ID</th>
									<th>Nome da empresa</th>
									<th>Detalhes</th>
									<th>Remover</th>
								</tr>
							</thead>
							<tbody>
								{fornecedores.map( ( fornecedor, index ) => {
									return (
										<tr key={index}>
											<td>{fornecedor.id}</td>
											<td>{fornecedor.empresa}</td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, fornecedor.id )}>detalhes</button></td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, fornecedor.id )}>remover</button></td>
										</tr>
									);
								} ) }	
							</tbody>							
						</Table>
					</Col>
				</Row>
					
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />									
				
				<Row>
					<Col className="col-md-3"></Col>
					<Col className="col-md-6">
						<Form onSubmit={ (e) => this.filtrar( e ) }>
							<Form.Group className="mb-3">
								<Form.Label>Empresa:</Form.Label>
								<Form.Control type="text" ref={this.empresaIni} name="empresaIni" />						
							</Form.Group>
																					
							<Button type="submit" variant="primary" className="mb-3">Filtrar</Button>				
						</Form>	
					</Col>
				</Row>	
				<Row>
					<Col className="col-md-3"></Col>
					<Col className="col-md-6">
						<button className="btn btn-link" style={{ padding : 0 }} onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo fornecedor</button>	
					</Col>
				</Row>				 
			</Container>					
		);
	}
	
}