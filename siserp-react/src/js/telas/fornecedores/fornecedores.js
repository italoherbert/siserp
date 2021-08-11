import React from 'react';
import ReactDOM from 'react-dom';
import {Container, Row, Col, Card, Form, Table, Button} from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

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
			fornecedores : [],
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {} 
		};			
		
		this.empresaIni = React.createRef();
	}
	
	componentDidMount() {
		this.empresaIni.current.value = "*";
		
		this.filtrar( null, false );		
	}
	
	filtrar( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();

		this.setState( { erroMsg : null, infoMsg : null } );

		sistema.showLoadingSpinner();
		
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
					
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhum fornecedor registrado!" } );																							
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <FornecedorDetalhes fornecedorId={id}/>, sistema.paginaElemento() );
	}
	
	removerSeConfirmado( e, id ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.remover( e, id );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		sistema.showLoadingSpinner();
		
		fetch( "/api/fornecedor/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.filtrar( null, false );																	
				this.setState( { infoMsg : "Fornecedor removido com êxito!" } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <FornecedorForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, fornecedores, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de fornecedor</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o fornecedor selecionado?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
				
				<Row>
					<Col>	
						<Form className="float-end">
							<Button variant="primary" onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo fornecedor</Button>
						</Form>
					</Col>
				</Row>
			
				<Row>
					<Col>
						<h4 className="text-center">Lista de Fornecedores</h4>
						<div className="tbl-pnl">
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
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.removerSeConfirmado( e, fornecedor.id )}>remover</button></td>
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
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<Card className="p-3">
							<h4>Filtrar fornecedores</h4>
							
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Form.Group className="mb-3">
									<Form.Label>Empresa:</Form.Label>
									<Form.Control type="text" ref={this.empresaIni} name="empresaIni" />						
								</Form.Group>
																						
								<Button type="submit" variant="primary" className="mb-3">Filtrar</Button>													
							</Form>	
						</Card>
					</Col>
				</Row>						
			</Container>					
		);
	}
	
}