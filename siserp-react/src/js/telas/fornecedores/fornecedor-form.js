import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class FornecedorForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
	}
	
	componentDidMount() {			
		if ( this.props.op == 'editar' )
			this.refs.empresa.value = this.props.empresa;					
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );
		
		let url;
		let metodo;
		if ( this.props.op == 'editar' ) {			
			url = "/api/fornecedor/atualiza/"+this.props.fornecedorId;
			metodo = 'PUT';									
		} else {
			url = "/api/fornecedor/registra";
			metodo = 'POST';
		}
				
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"empresa" : this.refs.empresa.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status == 200 ) {
				this.state.infoMsg = "Fornecedor cadastrado com sucesso.";		
				this.setState( this.state );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
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
						<h4 className="card-title text-center">Registro de fornecedores</h4>
																
						<form onSubmit={(e) => this.salvar( e ) } className="container">
							<div className="card border-1">								
								<div className="card-body">
									<h4 className="card-title">Dados gerais</h4>
									
									<div className="row">
										<div className="form-group col-sm-12">
											<label className="control-label" for="empresa">Empresa: </label>
											<input type="text" ref="empresa" name="empresa" className="form-control" />
										</div>
									</div>
								</div>
							</div>
							
							<br />
														
							<div className="card border-1">																
								<div className="card-body">
									<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
									<MensagemPainel color="info">{infoMsg}</MensagemPainel>
									
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