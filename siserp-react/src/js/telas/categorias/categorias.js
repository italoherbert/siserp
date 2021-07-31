import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaDetalhes from './categoria-detalhes';
import CategoriaForm from './categoria-form';

export default class Categorias extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			addErroMsg : null, 
			addInfoMsg : null,
			erroMsg : null,
			infoMsg : null,
			categorias : [] 
		};			
	}
	
	componentDidMount() {
		this.refs.descricaoIni.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		fetch( "/api/categoria/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"descricaoIni" : this.refs.descricaoIni.value,
			} )
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {
					this.state.categorias = dados;						
					if ( dados.length == 0 )
						this.state.infoMsg = "Nenhuma categoria registrado!";
						
					this.setState( this.state );
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <CategoriaDetalhes categoriaId={id}/>, sistema.paginaElemento() );
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		fetch( "/api/categoria/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				this.state.infoMsg = "Categoria removido com êxito!";
				this.filtrar();																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
		} );
	}
		
	render() {
		const { erroMsg, infoMsg, categorias } = this.state;
		
		return (
			<div className="container">				
				<div className="row">
					<div className="col-md-12">
						<CategoriaForm 
							op="cadastrar"
							titulo="Cadastre nova categoria" 
							registrou={ () => this.filtrar(null) } />
					</div>
				</div>
				
				<br />
				
				<div className="row card">
					<div className="col-sm-12 card-body">
						<h4 className="text-center">Lista de Categorias</h4>
						<div className="tbl-pnl col-md-12">
							<table id="tabela_funcionarios" className="table table-striped table-bordered col-md-12">
								<thead>
									<tr>
										<th>ID</th>
										<th>Nome da empresa</th>
										<th>Detalhes</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{categorias.map( ( categoria, index ) => {
										return (
											<tr>
												<td>{categoria.id}</td>
												<td>{categoria.descricao}</td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, categoria.id )}>detalhes</button></td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, categoria.id )}>remover</button></td>
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
							<div className="col-md-3"></div>
							<div className="col-md-6">
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
						</div>
					</div>
				</div>
																															
			</div>					
		);
	}
	
}