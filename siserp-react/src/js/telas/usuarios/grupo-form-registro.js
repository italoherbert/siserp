import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Grupos from './grupos';

export default class GrupoFormRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
		};
		
		this.nome = React.createRef();
	}
		
	salvar( e ) {
		e.preventDefault();
		
		sistema.wsPost( "/api/usuario/grupo/registra", {
			"nome" : this.nome.current.value
		}, (resposta) => {
			this.setState( { infoMsg : "Grupo salvo com sucesso." } );
		}, this );				
	}
	
	paraTelaGrupos() {
		ReactDOM.render( <Grupos />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<Container>
				<h4 className="text-center">Registro de grupos</h4>
				<br />
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">																
						<Card className="p-3">								
							<Form onSubmit={(e) => this.salvar( e ) }>
								<h4 className="card-title">Dados gerais</h4>								
								<Form.Group className="mb-2">
									<Form.Label>Nome: </Form.Label>
									<Form.Control type="text" ref={this.nome} name="nome" />
								</Form.Group>																																				
								
								<Button type="submit" variant="primary">Salvar</Button>										
							</Form>
						</Card>	
						
						<br />
						
						<MensagemPainel cor="danger" msg={erroMsg} />
						<MensagemPainel cor="primary" msg={infoMsg} />	
										
						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaGrupos( e ) }>Ir para grupos</button>								
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>															
			</Container>
		);
	}
	
}