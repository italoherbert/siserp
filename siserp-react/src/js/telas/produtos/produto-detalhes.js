import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import ProdutoForm from './produto-form';

export default class ProdutoDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			produto : { subcategorias : [] } 
		};
	}
	
	componentDidMount() {					
		let produtoId = this.props.produtoId;
		
		fetch( "/api/produto/get/"+produtoId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {											
					this.setState( { produto : dados } );				
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}			
		} );	
	}
			
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<ProdutoForm op="editar"
				titulo="Altere a produto" 
				produtoId={this.props.produtoId} 
				codigoBarras={this.state.produto.codigoBarras}
				descricao={this.state.produto.descricao} 
				precoUnitCompra={this.state.produto.precoUnitCompra} 
				precoUnitVenda={this.state.produto.precoUnitVenda} 
				unidade={this.state.produto.unidade} 
			/>,
			sistema.paginaElemento() );
	}
	
	render() {
		const { produto, erroMsg, infoMsg } = this.state;
		
		return ( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Dados do produto</h4>																
						
						<Card className="p-3">								
							<h4 className="card-title">Dados gerais</h4>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{produto.descricao}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Codigo barras: </span>
								<span className="text-info">{produto.codigoBarras}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Preço compra: </span>
								<span className="text-info">{produto.precoUnitCompra}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Preço venda: </span>
								<span className="text-info">{produto.precoUnitVenda}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Unidade: </span>
								<span className="text-info">{produto.unidade}</span>
							</div>
							
							<br />
							<button className="btn btn-link" onClick={(e) => this.editar( e )}>Editar produto</button>																																							
						</Card>
					</Col>
				</Row>
												
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />																																									
			</Container>
		);
	}

}