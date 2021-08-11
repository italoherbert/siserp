import React from 'react';
import ReactDOM from 'react-dom';
import {Container, Row, Col, Card, Form, Table, Button} from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import UsuarioGrupos from './usuario-grupos';

export default class Usuarios extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			usuarios : []
		};			
		
		this.usernameIni = React.createRef();
	}
	
	componentDidMount() {
		this.usernameIni.current.value = "*";
		
		this.filtrar( null, false );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();

		this.setState( { erroMsg : null, infoMsg : null } );

		sistema.showLoadingSpinner();
				
		fetch( "/api/usuario/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"usernameIni" : this.usernameIni.current.value,
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { usuarios : dados } );
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhum usuario registrado!" } );																							
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
				
	paraTelaGrupos( e ) {
		ReactDOM.render( <UsuarioGrupos />, sistema.paginaElemento() );
	}		
		
	render() {
		const { erroMsg, infoMsg, usuarios } = this.state;
		
		return (
			<Container>		
				<Row>
					<Col>	
						<Form className="float-start">
							<Button variant="primary" onClick={ (e) => this.paraTelaGrupos( e ) }>Grupos</Button>
						</Form>
					</Col>
				</Row>
			
				<Row>
					<Col>
						<h4 className="text-center">Lista de Usuarios</h4>
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Username</th>
										<th>Grupo</th>
									</tr>
								</thead>
								<tbody>
									{usuarios.map( ( usuario, index ) => {
										return (
											<tr key={index}>
												<td>{usuario.id}</td>
												<td>{usuario.username}</td>
												<td>{usuario.grupo.nome}</td>
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
							<h4>Filtrar usuarios</h4>
							
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Form.Group className="mb-3">
									<Form.Label>Nome de usuário: </Form.Label>
									<Form.Control type="text" ref={this.usernameIni} name="usernameIni" />						
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