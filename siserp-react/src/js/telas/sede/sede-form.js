import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import SedeDetalhes from './sede-detalhes';

export default class SedeForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		
		this.cnpj = React.createRef();
		this.inscricaoEstadual = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' ) {
			this.cnpj.current.value = this.props.cnpj;
			this.inscricaoEstadual.current.value = this.props.inscricaoEstadual;
		}										
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
						
		sistema.showLoadingSpinner();
		
		fetch( '/api/sede/salva', {
			method : 'PUT',			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"cnpj" : this.cnpj.current.value,	
				"inscricaoEstadual" : this.inscricaoEstadual.current.value				
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Dados da sede salvos com sucesso." } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );				
	}
	
	paraTelaSedeDetalhes() {
		ReactDOM.render( <SedeDetalhes />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="card-title text-center">Registro de sede</h4>
																
						<Card className="p-3">																		
							<Form onSubmit={(e) => this.salvar( e ) } className="container">
								<h4 className="card-title">Dados gerais</h4>
									
								<Form.Group className="mb-2">
									<Form.Label>CNPJ: </Form.Label>
									<Form.Control type="text" ref={this.cnpj} name="cnpj" />
								</Form.Group>	
								<Form.Group className="mb-2">
									<Form.Label>Inscrição estadual: </Form.Label>
									<Form.Control type="text" ref={this.inscricaoEstadual} name="cnpj" />
								</Form.Group>

								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Button type="submit" variant="primary" className="my-1">Salvar</Button>
								
								<br />
								<br />
								<button className="btn btn-link p-0" onClick={(e) => this.paraTelaSedeDetalhes( e ) }>Ir para detalhes</button>
							</Form>															
						</Card>
					</Col>
				</Row>
			</Container>
		);
	}
	
}