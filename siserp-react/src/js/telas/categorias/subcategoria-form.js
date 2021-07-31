import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaDetalhes from './categoria-detalhes';

export default class SubCategoriaForm extends React.Component {
	
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
			url = "/api/subcategoria/atualiza/"+this.props.subcategoriaId;
			metodo = 'PUT';									
		} else {
			url = "/api/subcategoria/registra/"+this.props.categoriaId;
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
				this.state.infoMsg = "Subcategoria salva com sucesso.";		
				this.setState( this.state );
				
				if ( typeof( this.props.registrou ) == "function" )
					this.props.registrou.call();
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );				
	}
	
	paraTelaCategoria( e ) {
		e.preventDefault();
		
		ReactDOM.render( 
			<CategoriaDetalhes categoriaId={this.props.categoriaId} />, 
			sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">																
						<form onSubmit={(e) => this.salvar( e ) } className="container">
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
									
									{(this.props.op == 'editar' ) && ( 
										<a href="#" onClick={(e) => this.paraTelaCategoria(e) }>Voltar para tela de categoria</a>
									) }																											
								</div>
							</div>									
						</form>
					</div>
				</div>
			</div>
		);
	}
	
}