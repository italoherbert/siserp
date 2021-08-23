import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import ProdutoForm from './produto-form';
import Produtos from './produtos';

export default class ProdutoDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			produto : { categoriaMaps : [] } 
		};
	}
	
	componentDidMount() {			
		sistema.wsGet( "/api/produto/get/"+this.props.produtoId, (resposta) => {
			resposta.json().then( (dados) => {		
				this.setState( { produto : dados } );				
			} );
		}, this );	
	}
			
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<ProdutoForm op="editar" titulo="Altere a produto" produtoId={this.props.produtoId} />,	sistema.paginaElemento() );
	}
	
	paraTelaProdutos() {
		ReactDOM.render( <Produtos />, sistema.paginaElemento() );
	}
	
	render() {
		const { produto, erroMsg, infoMsg } = this.state;
		
		return ( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Dados do produto</h4>																
						
						<Card className="p-3">								
							<h4 className="card-title">Dados gerais</h4>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{produto.descricao}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Codigo barras: </span>
								<span className="text-info">{produto.codigoBarras}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Preço compra: </span>
								<span className="text-info">{produto.precoUnitCompra}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Preço venda: </span>
								<span className="text-info">{produto.precoUnitVenda}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Unidade: </span>
								<span className="text-info">{produto.unidade}</span>
							</div>
							
							<div className="display-inline">
								<span className="text-dark font-weight-bold">Categorias: &nbsp;</span>
								<select>
									{ produto.categoriaMaps.map( (map, index2 ) => {
										return (
											<option key={index2} value={map.subcategoria}>{map.categoria} - {map.subcategoria}</option>
										)
									} ) }
								</select>
							</div>
							
							<br />
							<Row>
								<Col>
									<Form>
										<Button type="submit" variant="primary" onClick={(e) => this.editar( e )}>Editar produto</Button>
										<br />
										<br />
										<button className="btn btn-link p-0" onClick={(e) => this.paraTelaProdutos( e ) }>Ir para produtos</button>																																							
									</Form>
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>
												
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />																																									
			</Container>
		);
	}

}