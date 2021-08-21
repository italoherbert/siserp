import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FornecedorForm from './fornecedor-form';
import Fornecedores from './fornecedores';

export default class FornecedorDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			fornecedor : {} 
		};
	}
	
	componentDidMount() {		
		sistema.wsGet( '/api/fornecedor/get/'+this.props.fornecedorId, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { fornecedor : dados } );
			} );
		}, this );		
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<FornecedorForm op="editar"	fornecedorId={this.props.fornecedorId} />, sistema.paginaElemento() );
	}
		
	paraTelaFornecedores() {
		ReactDOM.render( <Fornecedores />, sistema.paginaElemento() );
	}
	
	render() {
		const { fornecedor, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Dados do fornecedor</h4>																
						
						<Card className="p-3">								
							<h4>Dados gerais</h4>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Empresa: </span>
								<span className="text-info">{fornecedor.empresa}</span>																							
							</div>									
						</Card>
						
						<br />
						
						<Card className="p-3">																				
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />	
							
							<Form>
								<Button variant="primary" onClick={(e) => this.editar( e )}>Editar fornecedor</Button>
							</Form>
							<br />
							<br />
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaFornecedores( e ) }>Ir para fornecedores</button>
						</Card>																		
					</Col>
				</Row>															
			</Container>
		);
	}

}