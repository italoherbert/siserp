import React from 'react';
import ReactDOM from 'react-dom';
import { Card, Form, Button } from 'react-bootstrap';

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
		
		this.descricao = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' )
			this.carrega();		
	}
	
	carrega() {
		sistema.wsGet( "/api/subcategoria/get/"+this.props.subcategoriaId, (resposta) => {
			resposta.json().then( (dados) => {
				this.descricao.current.value = dados.descricao;											
			} );
		}, this );
	}
	
	salvar( e ) {
		e.preventDefault();
						
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
			url = "/api/subcategoria/atualiza/"+this.props.subcategoriaId;
			metodo = 'PUT';									
		} else {
			url = "/api/subcategoria/registra/"+this.props.categoriaId;
			metodo = 'POST';
		}
		
		sistema.wsSave( url, metodo, {
			"descricao" : this.descricao.current.value
		}, (resposta) => {
			this.setState( { infoMsg : "Subcategoria salva com sucesso." } );
			
			if ( typeof( this.props.registrou ) === "function" )
				this.props.registrou.call();
		}, this );						
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
			<Card className="p-3">								
				<Form onSubmit={(e) => this.salvar( e ) }>
					<h4 className="card-title">{this.props.titulo}</h4>
					<Form.Group className="mb-2">
						<Form.Label>Descrição: </Form.Label>
						<Form.Control type="text" ref={this.descricao} name="descricao" />										
					</Form.Group>
				
					<MensagemPainel cor="danger" msg={erroMsg} />
					<MensagemPainel cor="primary" msg={infoMsg} />
														
					<Button type="submit" variant="primary">Salvar</Button>									
					
					{(this.props.op === 'editar' ) && ( 
						<div>
							<br />
							<button className="btn btn-link p-0" onClick={(e) => this.paraTelaCategoria(e) }>Ir para categoria</button>
						</div>
					) }																											
				</Form>								
			</Card>													
		);
	}
	
}