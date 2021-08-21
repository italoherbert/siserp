import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import Produtos from './produtos';

export default class ProdutoForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
		this.descricao = React.createRef();
		this.precoUnitCompra = React.createRef();
		this.precoUnitVenda = React.createRef();
		this.unidade = React.createRef();

		this.codigoBarras = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' )
			this.carrega();		
	}
	
	carrega() {
		sistema.wsGet( "/api/produto/get/"+this.props.produtoId, (resposta) => {
			resposta.json().then( (dados) => {
				this.descricao.current.value = dados.descricao;
				this.precoUnitCompra.current.value = dados.precoUnitCompra;
				this.precoUnitVenda.current.value = dados.precoUnitVenda;
				this.unidade.current.value = dados.unidade;

				this.codigoBarras.current.value = dados.codigoBarras;
			} );
		}, this );					
	}
	
	salvar( e ) {
		e.preventDefault();
		
		sistema.wsPost( "/api/produto/salva", {
			"codigoBarras" : this.codigoBarras.current.value,
			"descricao" : this.descricao.current.value,
			"precoUnitCompra" : sistema.paraFloat( this.precoUnitCompra.current.value ),
			"precoUnitVenda" : sistema.paraFloat( this.precoUnitVenda.current.value ),
			"unidade" : this.unidade.current.value
		}, (resposta) => {
			this.setState( { infoMsg : "Produto cadastrado com sucesso." } );
			
			if ( typeof( this.props.registrou ) === "function" )
				this.props.registrou.call( this );
		}, this );				
	}		
	
	buscar( e ) {
		if ( e != null )
			e.preventDefault();
					
		let codBarras = this.codigoBarras.current.value; 

		sistema.wsGet( "/api/produto/busca/"+codBarras, (resposta) => {
			resposta.json().then( (dados) => {
				this.codigoBarras.current.value = dados.codigoBarras;
				this.descricao.current.value = dados.descricao;
				this.precoUnitCompra.current.value = dados.precoUnitCompra;
				this.precoUnitVenda.current.value = dados.precoUnitVenda;
				this.unidade.current.value = dados.unidade;
													
				this.setState({});
			} );
		}, this );					
	}
	
	paraTelaProdutos( e ) {
		e.preventDefault();
		
		ReactDOM.render( <Produtos />, sistema.paginaElemento() );
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
								<h4 className="card-title">Salvar produto</h4>
								
								<Form.Group className="mb-2">
									<Form.Label>Codigo de barras: </Form.Label>
									<Row className="display-inline">
										<Col className="col-sm-10">
											<Form.Control type="text" ref={this.codigoBarras} name="codigoBarras" />
										</Col>
										<Col className="col-sm-2">
											<Button type="button" variant="primary" onClick={ (e) => this.buscar( e ) }>Buscar</Button>
										</Col>
									</Row>
								</Form.Group>
								<Form.Group className="mb-2">
									<Form.Label>Descrição: </Form.Label>
									<Form.Control type="text" ref={this.descricao} name="descricao" />
								</Form.Group>								
								<Form.Group className="mb-2">
									<Row>
										<Col>
											<Form.Label>Preço Compra por Unidade: </Form.Label>
											<Form.Control type="text" ref={this.precoUnitCompra} name="precoUnitCompra" />
										</Col>
										<Col>
											<Form.Label>Preço Venda por Unidade: </Form.Label>
											<Form.Control type="text" ref={this.precoUnitVenda} name="precoUnitVenda" />
										</Col>
									</Row>
								</Form.Group>
								<Form.Group className="mb-2 col-md-6">
									<Form.Label>Unidade: </Form.Label>
									<Form.Control type="text" ref={this.unidade} name="unidade" />
								</Form.Group>
							
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Button type="submit" variant="primary">Salvar</Button>
								
								<br />
								<br />
								<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaProdutos( e ) }>Para tela de produtos</button>																											
							</Form>
						</Card>
					</Col>
				</Row>					
			</Container>
		);
	}
	
}