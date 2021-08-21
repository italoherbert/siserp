import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import SedeForm from './sede-form';

export default class SedeDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			sede : {} 
		};
	}
	
	componentDidMount() {		
		sistema.wsGet( "/api/sede/get", (resposta) => {
			resposta.json().then( (dados) => {											
				this.setState( { sede : dados } );		
			} );
		}, this );	
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( <SedeForm op="editar" />, sistema.paginaElemento() );
	}
		
	render() {
		const { sede, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Dados da sede</h4>																
						
						<Card className="p-3">								
							<h4>Dados gerais</h4>
							
							<div className="display: inline">
								<span className="text-dark font-weight-bold">CNPJ: </span>
								<span className="text-info">{sede.cnpj}</span>
							</div>
							<div className="display: inline">
								<span className="text-dark font-weight-bold">Inscrição Estadual: </span>
								<span className="text-info">{sede.inscricaoEstadual}</span>							
							</div>
						</Card>
						
						<br />
						
						<Card className="p-3">			
							<Form>
								<Button variant="primary" onClick={(e) => this.editar( e )}>Editar sede</Button>
							</Form>						
							
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />																																							
						</Card>
												
					</Col>
				</Row>															
			</Container>
		);
	}

}