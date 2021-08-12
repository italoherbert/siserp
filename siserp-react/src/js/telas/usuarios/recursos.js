import React from 'react';
import ReactDOM from 'react-dom';
import {Container, Row, Col, Card, Form, Table, Button} from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Usuarios from './usuarios';
import RecursoForm from './recurso-form';

export default class Recursos extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			recursos : [],
			
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

		this.setState( { erroMsg : null, infoMsg : null } );

		sistema.showLoadingSpinner();
				
		fetch( "/api/recurso/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"nomeIni" : this.nomeIni.current.value,
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { recursos : dados } );
					
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhum recurso registrado!" } );																							
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	removerSeConfirmado( e, recursoId ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.remover( e, recursoId );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	remover( e, recursoId ) {
		e.preventDefault();
		
		sistema.showLoadingSpinner();
		
		fetch( "/api/recurso/deleta/"+recursoId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {						
				this.filtrar( null, false );																	
				this.setState( { infoMsg : "Recurso removido com êxito!" } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}						
			sistema.hideLoadingSpinner();
		} );
	}
		
	editar( e, recursoId ) {
		ReactDOM.render( <RecursoForm op="editar" recursoId={recursoId} />, sistema.paginaElemento() );
	}
	
	paraRecursoForm( e ) {
		ReactDOM.render( <RecursoForm op="cadastrar" />, sistema.paginaElemento() );		
	}
	
	paraTelaUsuarios( e ) {
		ReactDOM.render( <Usuarios />, sistema.paginaElemento() );
	}		
		
	render() {
		const { erroMsg, infoMsg, recursos, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de recurso</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						Tem certeza que deseja remover o recurso selecionado? <br /><br />
						Se optar por remover o recurso, serão removidas também as permissões do recurso concedidas a grupos de usuários!
					</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
				
				<Form>
					<Button variant="primary" className="float-end" onClick={ (e) => this.paraRecursoForm( e ) } >Registrar recurso</Button>
				</Form>
				
				<br />
				
				<h3 className="text-center">Lista de recursos</h3>
				<div className="tbl-pnl">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>ID</th>
								<th>Nome</th>
								<th>Editar</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{recursos.map( ( recurso, index ) => {
								return (
									<tr key={index}>
										<td>{recurso.id}</td>
										<td>{recurso.nome}</td>
										<td><button className="btn btn-link p-0" onClick={ (e) => this.editar( e, recurso.id ) }>editar</button></td>
										<td><button className="btn btn-link p-0" onClick={ (e) => this.removerSeConfirmado( e, recurso.id ) }>remover</button></td>
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
					<Col className="col-md-8">
						<Card className="p-3">
							<h4>Filtrar recursos</h4>
							
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Form.Group className="mb-3">
									<Form.Label>Nome do recurso: </Form.Label>
									<Form.Control type="text" ref={this.nomeIni} name="nomeIni" />						
								</Form.Group>
																						
								<Button type="submit" variant="primary">Filtrar</Button>													
							</Form>	
						</Card>
						
						<br />

						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaUsuarios( e ) }>Ir para usuários</button>								
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>	

				
			</Container>					
		);
	}
	
}