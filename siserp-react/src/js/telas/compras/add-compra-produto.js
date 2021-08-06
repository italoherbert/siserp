import React from 'react';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import AddCompraProdutoCategorias from './add-compra-produto-categorias';

export default class AddCompraProduto extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			codbarInfoMsg : null,
			categorias : []			
		};		
		this.descricao = React.createRef();
		this.codigoBarras = React.createRef();
		this.precoUnitCompra = React.createRef();
		this.precoUnitVenda = React.createRef();
		this.unidade = React.createRef();
		this.estoqueQuantidade = React.createRef();								
		this.paraAddQuantidade = React.createRef();								
	}			
	
	addProduto( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null, codbarInfoMsg : null } );
		
		if ( this.descricao.current.value.trim().length === 0 ) {
			this.setState( { erroMsg : "A descrição é um campo de preenchiento obrigatório." } );
			return;
		}
		
		if ( this.codigoBarras.current.value.trim().length === 0 ) {
			this.setState( { erroMsg : "O código de barras é um campo de preenchiento obrigatório." } );
			return;
		}
		
		if ( this.precoUnitCompra.current.value.trim().length === 0 ) {
			this.setState( { erroMsg : "O preço unitário de compra é um campo de preenchiento obrigatório." } );
			return;
		}
		
		if ( this.precoUnitVenda.current.value.trim().length === 0 ) {
			this.setState( { erroMsg : "O preço unitário de venda é um campo de preenchiento obrigatório." } );
			return;
		}
				
		if ( this.paraAddQuantidade.current.value.trim().length === 0 ) {
			this.setState( { erroMsg : "A quantidade é um campo de preenchiento obrigatório." } );
			return;
		}
								
		let produto = {
			descricao : this.descricao.current.value,
			codigoBarras : this.codigoBarras.current.value,
			precoUnitCompra : sistema.paraFloat( this.precoUnitCompra.current.value ),
			precoUnitVenda : sistema.paraFloat( this.precoUnitVenda.current.value ),
			unidade : this.unidade.current.value,
			paraAddQuantidade : sistema.paraFloat( this.paraAddQuantidade.current.value ),
			categorias : this.state.categorias
		};	
		
		this.limpaProdutoForm();
		
		if ( typeof( this.props.produtoAdicionado ) === "function" )
			this.props.produtoAdicionado.call( this, produto );		
	}	
			
	limpaProdutoForm() {
		this.descricao.current.value = "";
		this.codigoBarras.current.value = "";
		this.precoUnitCompra.current.value = "";
		this.precoUnitVenda.current.value = "";
		this.unidade.current.value = "";
		this.paraAddQuantidade.current.value = "";
		
		this.setState( { categorias : [] } );
	}
	
	buscar( e ) {
		e.preventDefault();
		
		this.setState( { codbarInfoMsg : null } );
		
		let codbar = this.codigoBarras.current.value;
		
		fetch( '/api/produto/busca/'+codbar, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.descricao.current.value = dados.descricao;
					this.precoUnitCompra.current.value = sistema.formataFloat( dados.precoUnitCompra );
					this.precoUnitVenda.current.value = sistema.formataFloat( dados.precoUnitVenda );
					this.unidade.current.value = dados.unidade;
					this.estoqueQuantidade.current.value = sistema.formataFloat( dados.quantidade );
										
					this.setState( { categorias : dados.categorias } );
				} );
			} else {
				sistema.trataRespostaNaoOkCBK( resposta, ( msg ) => {
					this.setState( { codbarInfoMsg : msg } );					
				} );
			}
		} ); 
			
	}
			
	render() {
		const { erroMsg, infoMsg, codbarInfoMsg, categorias } = this.state;
				
		return(									
			<Form onSubmit={(e) => this.addProduto( e ) }>																							
				<Tabs forceRenderTabPanel={true}>
					<TabList>
						<Tab>Dados do produto</Tab>
						<Tab>Categorias</Tab>
					</TabList>
					<TabPanel>
						<Card className="p-3">
							<h4 className="card-title">Dados do produto</h4>
							
							<Form.Group className="mb-2">
								<Form.Label>Descrição: </Form.Label>
								<Form.Control type="text" ref={this.descricao} name="descricao" />
							</Form.Group>
							<Form.Group className="mb-2">
								<Form.Label>Codigo de barras: </Form.Label>
								<Row className="display-inline mb-2">
									<Col className="col-sm-10">
										<Form.Control type="text" ref={this.codigoBarras} name="codigoBarras" />
									</Col>
									<Col className="col-sm-2">
										<Button type="button" variant="primary" onClick={ (e) => this.buscar( e ) }>Buscar</Button>
									</Col>
								</Row>
								<MensagemPainel cor="info" msg={codbarInfoMsg} />
							</Form.Group>
							<Form.Group className="mb-2">
								<Row>
									<Col className="col-sm-4">
										<Form.Label>Preço Compra: </Form.Label>
										<Form.Control type="text" ref={this.precoUnitCompra} name="precoUnitCompra" />
									</Col>
									<Col className="col-sm-4">								
										<Form.Label>Preço Venda: </Form.Label>
										<Form.Control type="text" ref={this.precoUnitVenda} name="precoUnitVenda" />
									</Col>
									<Col className="col-sm-4">								
										<Form.Label>Unidade: </Form.Label>
										<Form.Control type="text" ref={this.unidade} name="unidade" />
									</Col>
								</Row>
							</Form.Group>
							<Form.Group className="mb-2">						
								<Form.Label>Quantidade em estoque: </Form.Label>
								<Form.Control type="text" disabled={true} ref={this.estoqueQuantidade} name="estoqueQuantidade" />							
							</Form.Group>
							<Form.Group className="mb-2">						
								<Form.Label>Quantidade para adicionar: </Form.Label>
								<Form.Control type="text" ref={this.paraAddQuantidade} name="paraAddQuantidade" />							
							</Form.Group>
						</Card>
					</TabPanel>
					<TabPanel>							
						<AddCompraProdutoCategorias categorias={categorias} />
					</TabPanel>
				</Tabs>
				
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Button type="submit" variant="primary" className="my-3">Adicionar produto</Button>																																										
			</Form>
				
		);
	}
	
}