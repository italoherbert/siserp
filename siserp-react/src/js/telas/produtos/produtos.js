import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import ProdutoDetalhes from './produto-detalhes';
import ProdutoForm from './produto-form';

export default class Produtos extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			addErroMsg : null, 
			addInfoMsg : null,
			erroMsg : null,
			infoMsg : null,
			produtos : [] 
		};			
	}
	
	componentDidMount() {
		this.refs.descricaoIni.value = "*";
		
		this.filtrarPorDescricaoIni( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		let descIni = this.refs.descricaoIni.value;

		fetch( "/api/produto/filtra/"+descIni, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {
					this.state.produtos = dados;						
					if ( dados.length == 0 )
						this.state.infoMsg = "Nenhuma produto encontrado!";
						
					this.setState( this.state );
				} );																		
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
					this.state.produtos = new Array();
					this.state.produtos.push( dados );
											
					if ( dados.length == 0 )
						this.state.infoMsg = "Nenhuma produto encontrado!";
						
					this.setState( this.state );
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <ProdutoDetalhes produtoId={id}/>, sistema.paginaElemento() );
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		fetch( "/api/produto/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				this.state.infoMsg = "Produto removido com êxito!";
				this.filtrarPorDescricaoIni( null );																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
		} );
	}
	
	paraCadastroForm( e ) {
		e.preventDefault();
		
		ReactDOM.render( 
			<ProdutoForm op="cadastrar" titulo="Cadastre um novo produto" />, 
			sistema.paginaElemento() );
	}
		
	render() {
		const { erroMsg, infoMsg, produtos } = this.state;
		
		return (
			<div className="container">												
				<div className="row card">
					<div className="col-sm-12 card-body">
						<h4 className="text-center">Lista de Produtos</h4>
						<div className="tbl-pnl col-md-12">
							<table id="tabela_funcionarios" className="table table-striped table-bordered col-md-12">
								<thead>
									<tr>
										<th>ID</th>
										<th>Descrição</th>
										<th>Código de barras</th>
										<th>Preço compra</th>
										<th>Preço venda</th>
										<th>Unidade</th>
										<th>Detalhes</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{produtos.map( ( produto, index ) => {
										return (
											<tr>
												<td>{produto.id}</td>
												<td>{produto.descricao}</td>
												<td>{produto.codigoBarras}</td>
												<td>{produto.precoUnitCompra}</td>
												<td>{produto.precoUnitVenda}</td>
												<td>{produto.unidade}</td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, produto.id )}>detalhes</button></td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, produto.id )}>remover</button></td>
											</tr>
										);
									} ) }	
								</tbody>							
							</table>
						</div>
						
						<br />
				
						<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
						<MensagemPainel color="info">{infoMsg}</MensagemPainel>
						
						<div className="row">
							<div className="col-md-1"></div>
							<div className="col-md-10">
								<div className="row">
									<div className="col-sm-6">
										<form onSubmit={ (e) => this.filtrar( e ) }>
											<div className="form-group">
												<label className="control-label" for="descricaoIni">Descrição:</label>
												<input type="text" ref="descricaoIni" name="descricaoIni" className="form-control" />						
											</div>	
											<div className="form-group">
												<input type="submit" value="Filtrar" className="btn btn-primary" />				
											</div>										
										</form>
									</div>
									<div className="col-sm-6">
										<form onSubmit={ (e) => this.buscar( e ) }>
											<div className="form-group">
												<label className="control-label" for="codigoBarras">Código de barras:</label>
												<input type="text" ref="codigoBarras" name="codigoBarras" className="form-control" />						
											</div>
											<div className="form-group">
												<input type="submit" value="Buscar" className="btn btn-primary" />				
											</div>											
										</form>
									</div>
								</div>
								
								<br />																
																									
								<a href="#" onClick={ (e) => this.paraCadastroForm( e ) } >Cadastre um novo produto</a>
							</div>
						</div>
					</div>
				</div>
																															
			</div>					
		);
	}
	
}