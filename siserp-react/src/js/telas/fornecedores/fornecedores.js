import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FornecedorDetalhes from './fornecedor-detalhes';
import FornecedorForm from './fornecedor-form';

export default class Fornecedores extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			fornecedores : [] 
		};			
	}
	
	componentDidMount() {
		this.refs.empresaIni.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		fetch( "/api/fornecedor/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"empresaIni" : this.refs.empresaIni.value,
			} )
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {
					this.state.fornecedores = dados;						
					if ( dados.length == 0 )
						this.state.infoMsg = "Nenhum fornecedor registrado!";
																							
					this.setState( this.state );
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
		
	detalhes( e, id ) {
		e.preventDefault();
		
		ReactDOM.render( <FornecedorDetalhes fornecedorId={id}/>, sistema.paginaElemento() );
	}
	
	remover( e, id ) {
		e.preventDefault();
		
		fetch( "/api/fornecedor/deleta/"+id, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				this.state.infoMsg = "Fornecedor removido com êxito!";
				this.filtrar();																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
	
	paraTelaRegistro() {
		ReactDOM.render( <FornecedorForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, fornecedores } = this.state;
		
		return (
			<div className="container">
				<div className="row">
					<h4 className="text-center col-md-12">Lista de Fornecedores</h4>
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
								{fornecedores.map( ( fornecedor, index ) => {
									return (
										<tr>
											<td>{fornecedor.id}</td>
											<td>{fornecedor.empresa}</td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, fornecedor.id )}>detalhes</button></td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, fornecedor.id )}>remover</button></td>
										</tr>
									);
								} ) }	
							</tbody>							
						</table>
					</div>
				</div>
					
				<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
				<MensagemPainel color="info">{infoMsg}</MensagemPainel>									
				
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<form onSubmit={ (e) => this.filtrar( e ) }>
							<div className="form-group">
								<label className="control-label" for="empresaIni">Empresa:</label>
								<input type="text" ref="empresaIni" name="empresaIni" className="form-control" />						
							</div>
																					
							<div className="form-group">
								<input type="submit" value="Filtrar" className="btn btn-primary" />				
							</div>		
						</form>	
					</div>
				</div>	
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<button className="btn btn-link" style={{ padding : 0 }} onClick={ (e) => this.paraTelaRegistro(e)}>Registrar novo fornecedor</button>	
					</div>
				</div>				 
			</div>					
		);
	}
	
}