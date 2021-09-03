import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaMapForm from './categoria-map-form';
import CategoriaMaps from './categoria-maps';

export default class CategoriaMapDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			categoriaMap : {}
		};		
	}
	
	componentDidMount() {
		this.carregar( null );		
	}
	
	carregar( e ) {
		if ( e != null )
			e.preventDefault();
							
		sistema.wsGet( "/api/categoriamap/get/"+this.props.categoriaMapId, (resposta) => {
			resposta.json().then( (dados) => {		
				this.setState( { categoriaMap : dados } );									
			} );
		}, this );						
	}
			
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<CategoriaMapForm op="editar"
				titulo="Altere a categoria" 
				categoriaMapId={this.props.categoriaMapId} 
			/>,
			sistema.paginaElemento() );
	}
		
	paraTelaCategoriaMaps() {
		ReactDOM.render( <CategoriaMaps />, sistema.paginaElemento() );
	}
		
	render() {
		const { categoriaMap, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>				
				<h4 className="text-center">Dados do categoria</h4>																
					
				<br />
				
				<Row>
					<Col className="col-md-8">
						
						<Card className="p-3">								
							<h4 className="card-title">Dados gerais</h4>
								
							<div className="inline-block">	
								<span className="text-dark font-weight-bold">Categoria: </span>
								<span className="text-info">{categoriaMap.categoria}</span>
							</div>	

							<div className="inline-block">	
								<span className="text-dark font-weight-bold">Subcategoria: </span>
								<span className="text-info">{categoriaMap.subcategoria}</span>
							</div>	
							
							<br />
							<Form>
								<Button variant="primary" onClick={(e) => this.editar( e )}>Editar categoria</Button>															
							</Form>
						</Card>
					</Col>
				</Row>
																					
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />																						
												
				<br />
				
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCategoriaMaps( e ) }>Ir para categoria</button>
						</Col>
					</Row>
				</Card>
			</Container>
		);
	}

}