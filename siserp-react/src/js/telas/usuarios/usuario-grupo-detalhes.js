import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import UsuarioGrupoForm from './usuario-grupo-form';
import UsuarioGrupos from './usuario-grupos';

export default class UsuarioGrupoDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			grupo : {} 
		};
	}
	
	componentDidMount() {
		let grupoId = this.props.grupoId;
		
		sistema.showLoadingSpinner();
		
		fetch( "/api/usuario/grupo/get/"+grupoId, {
			method : "GET",			
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
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( <UsuarioGrupoForm op="editar" grupoId={this.props.grupoId} />, sistema.paginaElemento() );
	}
		
	paraTelaUsuarioGrupos() {
		ReactDOM.render( <UsuarioGrupos />, sistema.paginaElemento() );
	}
	
	render() {
		const { grupo, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Detalhes do grupo</h4>																
						
						<Card className="p-3">								
							<h4>Dados gerais</h4>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Nome: </span>
								<span className="text-info">{grupo.nome}</span>																							
							</div>									
						</Card>
						
						<br />
						
						<Card className="p-3">																				
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />	
							
							<Form>
								<Button variant="primary" onClick={(e) => this.editar( e )}>Editar grupo</Button>
							</Form>
							<br />
							<br />
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaUsuarioGrupos( e ) }>Ir para grupos</button>
						</Card>																		
					</Col>
				</Row>															
			</Container>
		);
	}

}