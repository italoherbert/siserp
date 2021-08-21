import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaDetalhes from './categoria-detalhes';

export default class CategoriaForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		
		this.descricao = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' )
			this.carrega();				
	}
	
	carrega() {
		sistema.wsGet( "/api/categoria/get/"+this.props.categoriaId, (resposta) => {
			resposta.json().then( (dados) => {
				this.descricao.current.value = dados.descricao;											
			} );
		}, this );
	}
	
	salvar( e ) {
		e.preventDefault();
				
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/categoria/atualiza/"+this.props.categoriaId;
			metodo = 'PUT';									
		} else {
			url = "/api/categoria/registra";
			metodo = 'POST';
		}
	
		sistema.wsSave( url, metodo, {
			"descricao" : this.descricao.current.value			
		}, (resposta) => {
			this.setState( { infoMsg : "Categoria cadastrada com sucesso." } );
			
			if ( typeof( this.props.registrou ) === "function" )
				this.props.registrou.call();
		}, this );						
	}
		
	paraTelaCategoriaDetalhes() {
		ReactDOM.render( <CategoriaDetalhes categoriaId={this.props.categoriaId} />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(	
			<div>																		
				<Card className="p-3">								
					<Form onSubmit={(e) => this.salvar( e ) }>
						<h4 className="card-title">{this.props.titulo}</h4>
						
						<Form.Group className="mb-2">
							<Form.Label>Descrição: </Form.Label>
							<Form.Control type="text" ref={this.descricao} name="descricao" />
						</Form.Group>
					
						<MensagemPainel cor="danger" msg={erroMsg} />
						<MensagemPainel cor="primary" msg={infoMsg} />
						
						<Button type="submit" variant="primary">Salvar</Button>
					</Form>
				</Card>													
					
				{ this.props.op === 'editar' && (
					<div>
						<br />
						<Card className="p-3">
							<Row>
								<Col>
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCategoriaDetalhes( e ) }>Ir para detalhes</button>
								</Col>
							</Row>
						</Card>
					</div>
				) }
			</div>
		);
	}
	
}