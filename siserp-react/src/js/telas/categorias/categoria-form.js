import React from 'react';
import {Container, Row, Col, Card, Form, Button} from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

export default class CategoriaForm extends React.Component {
	
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
				"descricao" : this.descricao.current.value
			} )		
		} ).then( (resposta) => {				
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Categoria cadastrada com sucesso." } );
				
				if ( typeof( this.props.registrou ) === "function" )
					this.props.registrou.call();
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );				
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(	
			<Container>
				<Row>
					<Col className="col-sm-2"></Col>	
					<Col className="col-sm-8">															
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
							</Form>
						</Card>													
					</Col>
				</Row>					
			</Container>
		);
	}
	
}