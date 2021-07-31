import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

export default class CategoriaForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
	}
	
	componentDidMount() {			
		if ( this.props.op == 'editar' )
			this.refs.descricao.value = this.props.descricao;					
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );
		
		let url;
		let metodo;
		if ( this.props.op == 'editar' ) {			
			url = "/api/categoria/atualiza/"+this.props.categoriaId;
			metodo = 'PUT';									
		} else {
			url = "/api/categoria/registra";
			metodo = 'POST';
		}
				
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"descricao" : this.refs.descricao.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status == 200 ) {
				this.state.infoMsg = "Categoria cadastrada com sucesso.";		
				this.setState( this.state );
				
				if ( typeof( this.props.registrou ) == "function" )
					this.props.registrou.call();
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );				
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
									<h4 className="card-title">{this.props.titulo}</h4>
									
									<div className="row">
										<div className="form-group col-sm-12">
											<label className="control-label" for="descricao">Descrição: </label>
											<input type="text" ref="descricao" name="descricao" className="form-control" />
										</div>
									</div>
								
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