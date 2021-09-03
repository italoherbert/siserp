import React from 'react';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import MensagemPainel from './../../../componente/mensagem-painel';
import sistema from './../../../logica/sistema';

import AddCompraProdutocategoriaMaps from './add-compra-produto-categoria-maps';

export default class AddCompraProduto extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			categoriaMaps : []			
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
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
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
			categoriaMaps : this.state.categoriaMaps
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
		
		this.setState( { categoriaMaps : [] } );
	}
	
	buscar( e ) {
		e.preventDefault();
		
		let codbar = this.codigoBarras.current.value;
		
		sistema.wsGet( '/api/produto/busca/'+codbar, (resposta) => {
			resposta.json().then( (dados) => {
				this.descricao.current.value = dados.descricao;
				this.precoUnitCompra.current.value = sistema.formataFloat( dados.precoUnitCompra );
				this.precoUnitVenda.current.value = sistema.formataFloat( dados.precoUnitVenda );
				this.unidade.current.value = dados.unidade;
				this.estoqueQuantidade.current.value = sistema.formataFloat( dados.quantidade );
								
				this.setState( { categoriaMaps : dados.categoriaMaps } );
			} );
		}, this );					
	}
			
	render() {
		const { erroMsg, infoMsg, categoriaMaps } = this.state;
				
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
									<Col className="col-sm-6">
										<Form.Control type="text" ref={this.codigoBarras} name="codigoBarras" />
									</Col>
									<Col className="col-sm-2">
										<Button type="button" variant="primary" onClick={ (e) => this.buscar( e ) }>Buscar</Button>
									</Col>
								</Row>
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
								<Row>
									<Col className="col-sm-4">								
										<Form.Label>Quantidade para adicionar: </Form.Label>
										<Form.Control type="text" ref={this.paraAddQuantidade} name="paraAddQuantidade" />
									</Col>
									<Col className="col-sm-4">								
										<Form.Label>Quantidade em estoque: </Form.Label>
										<Form.Control type="text" disabled={true} ref={this.estoqueQuantidade} name="estoqueQuantidade" />
									</Col>
								</Row>																					
							</Form.Group>							
						</Card>
					</TabPanel>
					<TabPanel>							
						<AddCompraProdutocategoriaMaps categoriaMaps={categoriaMaps} />
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