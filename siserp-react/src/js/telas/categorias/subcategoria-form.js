import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

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
			this.descricao.current.value = this.props.descricao;							
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );		
		
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
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
				"descricao" : this.descricao.current.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Subcategoria salva com sucesso." } );
				
				if ( typeof( this.props.registrou ) === "function" )
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
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">																
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
									<button className="btn btn-link" onClick={(e) => this.paraTelaCategoria(e) }>Voltar para tela de categoria</button>
								) }																											
							</Form>								
						</Card>									
					</Col>
				</Row>
			</Container>
		);
	}
	
}