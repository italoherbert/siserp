import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import Produtos from './produtos';

export default class ProdutoForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
	}
	
	componentDidMount() {			
		if ( this.props.op == 'editar' ) {
			this.refs.descricao.value = this.props.descricao;
			this.refs.codigoBarras.value = this.props.codigoBarras;
			this.refs.precoUnitCompra.value = this.props.precoUnitCompra;
			this.refs.precoUnitVenda.value = this.props.precoUnitVenda;
			this.refs.unidade.value = this.props.unidade;
		}					
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );
						
		fetch( "/api/produto/salva", {
			method : "POST",			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"descricao" : this.refs.descricao.value,
				"codigoBarras" : this.refs.codigoBarras.value,
				"precoUnitCompra" : this.refs.precoUnitCompra.value,
				"precoUnitVenda" : this.refs.precoUnitVenda.value,
				"unidade" : this.refs.unidade.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status == 200 ) {
				this.state.infoMsg = "Produto cadastrado com sucesso.";		
				this.setState( this.state );
				
				if ( typeof( this.props.registrou ) == "function" )
					this.props.registrou.call( this );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );				
	}		
	
	buscar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		let codBarras = this.refs.codigoBarras.value; 

		fetch( "/api/produto/busca/"+codBarras, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {
					this.refs.descricao.value = dados.descricao;
					this.refs.codigoBarras.value = dados.codigoBarras;
					this.refs.precoUnitCompra.value = dados.precoUnitCompra;
					this.refs.precoUnitVenda.value = dados.precoUnitVenda;
					this.refs.unidade.value = dados.unidade;
														
					this.setState( this.state );
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	paraTelaProdutos( e ) {
		e.preventDefault();
		
		ReactDOM.render( <Produtos />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(	
			<div className="container-fluid">
				<div className="row">
					<div className="col-sm-2"></div>	
					<div className="col-sm-8">															
						<form onSubmit={(e) => this.salvar( e ) }>
							<div className="card border-1">								
								<div className="card-body">
									<h4 className="card-title">Salvar produto</h4>
									
									<div className="form-group">
										<label className="control-label" for="descricao">Descrição: </label>
										<input type="text" ref="descricao" name="descricao" className="form-control" />
									</div>
									<div className="form-group">
										<label className="control-label" for="codigoBarras">Codigo barras: </label>
										<div className="row">
											<input type="text" ref="codigoBarras" name="codigoBarras" className="form-control" />
											<button type="button" className="btn btn-primary" onClick={ (e) => this.buscar( e ) }>Buscar</button>
										</div>
									</div>
									<div className="form-group">
										<label className="control-label" for="precoUnitCompra">Preço Compra (Unidade): </label>
										<input type="text" ref="precoUnitCompra" name="precoUnitCompra" className="form-control" />
									</div>
									<div className="form-group">
										<label className="control-label" for="precoUnitVenda">Preço Venda (Unidade): </label>
										<input type="text" ref="precoUnitVenda" name="precoUnitVenda" className="form-control" />
									</div>
									<div className="form-group">
										<label className="control-label" for="unidade">Unidade: </label>
										<input type="text" ref="unidade" name="unidade" className="form-control" />
									</div>
								
									<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
									<MensagemPainel color="info">{infoMsg}</MensagemPainel>
									
									<div class="form-group">
										<button type="submit" className="btn btn-primary">Salvar</button>
									</div>	
									
									<a href="#" onClick={ (e) => this.paraTelaProdutos( e ) }>Para tela de produtos</a>																											
								</div>
							</div>									
						</form>
					</div>
				</div>					
			</div>
		);
	}
	
}