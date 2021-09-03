import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaMapDetalhes from './categoria-map-detalhes';

export default class CategoriaMapForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		
		this.categoria = React.createRef();
		this.subcategoria = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' )
			this.carrega();				
	}
	
	carrega() {
		sistema.wsGet( "/api/categoriamap/get/"+this.props.categoriaMapId, (resposta) => {
			resposta.json().then( (dados) => {
				this.categoria.current.value = dados.categoria;											
				this.subcategoria.current.value = dados.subcategoria;											
			} );
		}, this );
	}
	
	salvar( e ) {
		e.preventDefault();
				
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/categoriamap/atualiza/"+this.props.categoriaMapId;
			metodo = 'PUT';									
		} else {
			url = "/api/categoriamap/registra";
			metodo = 'POST';
		}
	
		sistema.wsSave( url, metodo, {
			"categoria" : this.categoria.current.value,
			"subcategoria" : this.subcategoria.current.value
		}, (resposta) => {
			this.setState( { infoMsg : "Categoria salva com sucesso." } );
			
			if ( typeof( this.props.registrou ) === "function" )
				this.props.registrou.call();
		}, this );						
	}
		
	paraTelaCategoriaMapDetalhes() {
		ReactDOM.render( <CategoriaMapDetalhes categoriaMapId={this.props.categoriaMapId} />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(	
			<div>																		
				<Card className="p-3">								
					<Form onSubmit={(e) => this.salvar( e ) }>
						<h4 className="card-title">{this.props.titulo}</h4>
						
						<Form.Group className="mb-2">
							<Form.Label>Categoria: </Form.Label>
							<Form.Control type="text" ref={this.categoria} name="categoria" />
						</Form.Group>
						
						<Form.Group className="mb-2">
							<Form.Label>Subcategoria: </Form.Label>
							<Form.Control type="text" ref={this.subcategoria} name="subcategoria" />
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
									<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCategoriaMapDetalhes( e ) }>Ir para detalhes</button>
								</Col>
							</Row>
						</Card>
					</div>
				) }
			</div>
		);
	}
	
}