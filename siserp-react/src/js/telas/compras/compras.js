import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class Compras extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			compras : [],
			dataIni : '',
			dadaFim : ''
		};					
	}
	
	componentDidMount() {
		//this.state.dataIni = moment( new Date() ).format( "dd-mm-yyyy" );
		//this.state.dataFim = moment( new Date() ).format( "dd-mm-yyyy" );
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		fetch( "/api/compra/filtra", {
			method : "POST",			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8", 
				"Authorization" : "Bearer "+sistema.token
			}, 
			body : JSON.stringify( {
				"dataIni" : this.refs.dataIni.value,
				"dataFim" : this.refs.dataFim.value
			} )
		} ).then( (resposta) => {	
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {
					this.state.compras = dados;	
					
					if ( dados.length == 0 )
						this.state.infoMsg = "Nenhuma compra encontrada pelos critérios de busca informados!";
																		
					this.setState( this.state );
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	paraRegistroForm( e ) {
		e.preventDefault();
		
		//ReactDOM.render( <CompraRegistro />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, compras, dataIni, dataFim } = this.state;
				
		return (
			<div className="container">												
				<div className="row card">
					<div className="col-sm-12 card-body">
						<h4 className="text-center">Lista de Compras</h4>
						<div className="tbl-pnl col-md-12">
							<table id="tabela_funcionarios" className="table table-striped table-bordered col-md-12">
								<thead>
									<tr>
										<th>ID</th>
										<th>Data compra</th>
										<th>Debito total</th>										
										<th>Detalhes</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{compras.map( ( compra, index ) => {
										return (
											<tr>
												<td>{compra.id}</td>
												<td>{compra.dataCompra}</td>
												<td>{compra.valorTotal}</td>												
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, compra.id )}>detalhes</button></td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, compra.id )}>remover</button></td>
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
								<form onSubmit={ (e) => this.filtrar( e ) }>
									<div className="row">
											<div className="col-sm-6">										
												<div className="form-group">													
													<label className="control-label" for="dataIni">Data de início:</label>
													<input type="date" ref="dataIni" name="dataIni" defaultValue={dataIni} className="form-control" />						
												</div>									
											</div>
											<div className="col-sm-6">
												<div className="form-group">
													<label className="control-label" for="dataFim">Data de fim:</label>
													<input type="date" ref="dataFim" name="dataFim" defaultValue={dataFim} className="form-control" />						
												</div>																					
											</div>
									</div>
									<div className="row">
										<div className="col-sm-12">
											<div className="form-group">
												<input type="submit" value="Filtrar" className="btn btn-primary" />				
											</div>
										</div>
									</div>
								</form>
								
								<br />																
																									
								<a href="#" onClick={ (e) => this.paraRegistroForm( e ) } >Registre uma nova compra</a>
							</div>
						</div>
					</div>
				</div>
																															
			</div>	
		)
	}
	
}