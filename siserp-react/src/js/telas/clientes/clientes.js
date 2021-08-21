import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

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
			clientes : [],
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {} 
		};

		this.nomeIni = React.createRef();
	}
	
	componentDidMount() {
		this.nomeIni.current.value = "*";
		
		this.filtrar( null, false );		
	}
	
	filtrar( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();
		
		sistema.wsPost( "/api/cliente/filtra", {
			nomeIni : this.nomeIni.current.value
		}, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { clientes : dados } );				
				
				if ( dados.length === 0 && filtrarBTClicado === true )
					this.setState( { infoMsg : "Nenhum cliente encontrado!" } );												
			} );
		}, this );			
	}
			
	detalhes( e, clienteId ) {
		e.preventDefault();
		
		ReactDOM.render( <ClienteDetalhes clienteId={clienteId} />, sistema.paginaElemento() );
	}
			
	removerSeConfirmado( e, clienteId ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.remover( e, clienteId );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	remover( e, clienteId ) {
		e.preventDefault();
		
		sistema.wsDelete( "/api/cliente/deleta/"+clienteId, (resposta) => {
			this.setState( { infoMsg : "Cliente removido com êxito!" } );
			this.filtrar( null, false );	
		}, this );		
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <ClienteForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, clientes, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de cliente</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o cliente selecionado?</Modal.Body>
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
							<Button variant="primary"  onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo cliente</Button>
						</Form>
					</Col>
				</Row>
				
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
										<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, cliente.id )}>remover</button></td>
									</tr>
								);
							} ) }	
						</tbody>							
					</Table>
				</div>
					
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />	
				
				<Row>
					<Col className="col-md-6">
						<Card className="p-3">
							<h4>Filtrar clientes</h4>
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Form.Group className="mb-3">
									<Form.Label>Nome:</Form.Label>
									<Form.Control type="text" ref={this.nomeIni} name="nomeIni" />						
								</Form.Group>
																
								<Button type="submit" variant="primary">Filtrar</Button>																						
							</Form>	
						</Card>											
					</Col>
				</Row>									 
			</Container>					
		);
	}
	
}