import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import CategoriaForm from './categoria-form';
import SubCategoriaForm from './subcategoria-form';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

export default class CategoriaDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			categoria : { subcategorias : [] } 
		};
	}
	
	componentDidMount() {
		this.carregar( null );
		
		this.refs.subcatDescricaoIni.value = "*";
	}
	
	carregar( e ) {
		if ( e != null )
			e.preventDefault();
			
		let categoriaId = this.props.categoriaId;
		
		fetch( "/api/categoria/get/"+categoriaId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {											
					this.state.categoria = dados;
					
					if ( dados.subcategorias.length == 0 )
						this.state.infoMsg = "Nenhuma subcategoria registrada.";
					
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
			<CategoriaForm op="editar"
				titulo="Altere a categoria" 
				categoriaId={this.props.categoriaId} 
				descricao={this.state.categoria.descricao} 
			/>,
			sistema.paginaElemento() );
	}
	
	filtrarSubcategorias( e ) {
		if ( e != null )
			e.preventDefault();
			
		let categoriaId = this.props.categoriaId;
		
		fetch( "/api/subcategoria/filtra/"+categoriaId, {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"descricaoIni" : this.refs.subcatDescricaoIni.value
			} )
		} ).then( (resposta) => {
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {											
					this.state.categoria.subcategorias = dados;
					
					if ( dados.length == 0 )
						this.state.infoMsg = "Nenhuma subcategoria encontrada pelos critérios de busca informados.";
					
					this.setState( this.state );				
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}			
		} );		
		
	}
	
	editarSubcategoria( e, subcat ) {
		e.preventDefault();
		
		ReactDOM.render( 			
			<SubCategoriaForm 
				op="editar" 
				titulo="Altere a subcategoria"
				categoriaId={this.state.categoria.id}
				subcategoriaId={subcat.id} 
				descricao={subcat.descricao} 
			/>, sistema.paginaElemento() ); 
	}
	
	removerSubcategoria( e, subcatId ) {
		e.preventDefault();

		fetch( "/api/subcategoria/deleta/"+subcatId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {	
				this.state.infoMsg = "Subcategoria removida com êxito!";
				this.filtrarSubcategorias( e );																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
		} );		
	}
		
	render() {
		const { categoria, erroMsg, infoMsg } = this.state;
		
		return( 
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados do categoria</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{categoria.descricao}</span>
								
								<br />
								<br />
								<a href="#" onClick={(e) => this.editar( e )}>Editar categoria</a>																																							
							</div>																
						</div>
					</div>
				</div>
												
				<br />
				
				<div className="card row">
					<div className="card-body col-md-12">
						<h4 className="card-title text-center">Lista de Subcategorias</h4>
						<div className="tbl-pnl col-md-12">
							<table id="tabela_funcionarios" className="table table-striped table-bordered col-md-12">
								<thead>
									<tr>
										<th>ID</th>
										<th>Descrição</th>
										<th>Editar</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{categoria.subcategorias.map( ( subcategoria, index ) => {
										return (
											<tr>
												<td>{subcategoria.id}</td>
												<td>{subcategoria.descricao}</td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.editarSubcategoria( e, subcategoria )}>editar</button></td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.removerSubcategoria( e, subcategoria.id )}>remover</button></td>
											</tr>
										);
									} ) }	
								</tbody>							
							</table>
						</div>
						
						<br />
						
						<div className="row">
							<div className="col-sm-2"></div>
							<div className="col-sm-8">
								<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
								<MensagemPainel color="info">{infoMsg}</MensagemPainel>
								
								<form onSubmit={ (e) => this.filtrarSubcategorias( e ) }>
									<div className="form-group">
										<label className="control-label" for="subcatDescricaoIni">Descrição:</label>
										<input type="text" ref="subcatDescricaoIni" name="subcatDescricaoIni" className="form-control" />						
									</div>
																							
									<div className="form-group">																							
										<input type="submit" value="Filtrar" className="btn btn-primary" />				
									</div>		
								</form>	
							</div>
						</div>																
					</div>															
				</div>		
				<br />
								
				<SubCategoriaForm 
					op="cadastrar" 
					titulo="Registre nova subcategoria"
					categoriaId={this.props.categoriaId} 
					registrou={ () => this.filtrarSubcategorias(null) } />
																																												
			</div>
		);
	}

}