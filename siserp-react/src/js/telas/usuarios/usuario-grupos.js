import React from 'react';
import ReactDOM from 'react-dom';
import {Container, Row, Col, Card, Form, Table, Button} from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import UsuarioGrupoDetalhes from './usuario-grupo-detalhes';
import UsuarioGrupoForm from './usuario-grupo-form';

export default class UsuarioGrupos extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			grupos : [],
			
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
		
		fetch( "/api/usuario/grupo/filtra", {
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
					this.setState( { grupos : dados } );
					
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhum grupo registrado!" } );																							
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	detalhes( e, id ) {
		e.preventDefault();
				
		ReactDOM.render( <UsuarioGrupoDetalhes grupoId={id}/>, sistema.paginaElemento() );
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
		
		fetch( "/api/usuario/grupo/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				this.filtrar( null, false );																	
				this.setState( { infoMsg : "Grupo removido com êxito!" } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <UsuarioGrupoForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, grupos, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de grupo</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o grupo selecionado?</Modal.Body>
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
							<Button variant="primary" onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo grupo</Button>
						</Form>
					</Col>
				</Row>
			
				<Row>
					<Col>
						<h4 className="text-center">Lista de Grupos</h4>
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
									{grupos.map( ( grupo, index ) => {
										return (
											<tr key={index}>
												<td>{grupo.id}</td>
												<td>{grupo.nome}</td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, grupo.id )}>detalhes</button></td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, grupo.id )}>remover</button></td>
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
							<h4>Filtrar grupos</h4>
							
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Form.Group className="mb-3">
									<Form.Label>Nome:</Form.Label>
									<Form.Control type="text" ref={this.nomeIni} name="nomeIni" />						
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