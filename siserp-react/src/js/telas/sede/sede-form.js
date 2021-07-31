import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class SedeForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
	}
	
	componentDidMount() {			
		if ( this.props.op == 'editar' ) {
			this.refs.cnpj.value = this.props.cnpj;
			this.refs.inscricaoEstadual.value = this.props.inscricaoEstadual;
		}										
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );
						
		fetch( '/api/sede/salva', {
			method : 'PUT',			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"cnpj" : this.refs.cnpj.value,	
				"inscricaoEstadual" : this.refs.inscricaoEstadual.value				
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status == 200 ) {
				this.state.infoMsg = "Dados da sede salvos com sucesso.";		
				this.setState( this.state );
			} else if ( resposta.status == 400 ) {
				resposta.json().then( (dados) => {
					this.state.erroMsg = dados.mensagem;
					this.setState( this.state );
				} );
			} else {
				this.state.erroMsg = sistema.getMensagemErro( resposta.status );
				this.setState( this.state );
			}
		} );				
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="card-title text-center">Registro de sede</h4>
																
						<form onSubmit={(e) => this.salvar( e ) } className="container">
							<div className="card border-1">								
								<div className="card-body">
									<h4 className="card-title">Dados gerais</h4>
									
									<div className="row form-group">
										<div className="col-sm-12">
											<label className="control-label" for="cnpj">CNPJ: </label>
											<input type="text" ref="cnpj" name="cnpj" className="form-control" />
										</div>										
									</div>	
									<div className="row form-group">
										<div className="col-sm-12">
											<label className="control-label" for="inscricaoEstadual">Inscrição estadual: </label>
											<input type="text" ref="inscricaoEstadual" name="inscricaoEstadual" className="form-control" />
										</div>										
									</div>								
								</div>
							</div>
							
							<br />
														
							<div className="card border-1">																
								<div className="card-body">
									<MensagemPainel cor="danger" msg={erroMsg} />
									<MensagemPainel cor="info" msg={infoMsg} />
									<div class="form-group">
										<button type="submit" className="btn btn-primary">Salvar</button>
									</div>																												
								</div>
							</div>									
						</form>
					</div>
				</div>
			</div>
		);
	}
	
}