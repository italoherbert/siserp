import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import ProdutoForm from './produto-form';

export default class ProdutoDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			produto : { subprodutos : [] } 
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
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {											
					this.state.produto = dados;										
					this.setState( this.state );				
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
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados do produto</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{produto.descricao}</span>
								
								<br />
								<span className="text-dark font-weight-bold">Codigo barras: </span>
								<span className="text-info">{produto.codigoBarras}</span>
								
								<br />
								<span className="text-dark font-weight-bold">Preço compra: </span>
								<span className="text-info">{produto.precoUnitCompra}</span>
								
								<br />
								<span className="text-dark font-weight-bold">Preço venda: </span>
								<span className="text-info">{produto.precoUnitVenda}</span>
								
								<br />
								<span className="text-dark font-weight-bold">Unidade: </span>
								<span className="text-info">{produto.unidade}</span>
								
								<br />
								
								<br />
								<br />
								<a href="#" onClick={(e) => this.editar( e )}>Editar produto</a>																																							
							</div>																
						</div>
					</div>
				</div>
												
				<br />
				
				<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
				<MensagemPainel color="info">{infoMsg}</MensagemPainel>																																									
			</div>
		);
	}

}