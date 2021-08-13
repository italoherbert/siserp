import React from 'react';
import ReactDOM from 'react-dom';

import { Row, Col, Card, Table } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import Vendas from './vendas';

export default class VendaDetalhes extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,
			venda : { cliente : { pessoa : {} }, itens : [] }
		};								
	}				
	
	componentDidMount() {	
		sistema.showLoadingSpinner();
	
		fetch( '/api/venda/get/'+this.props.vendaId, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					alert( JSON.stringify( dados ) );
					this.setState( { venda : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();			
		} );
	}
	
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}
		
	render() {
		const { infoMsg, erroMsg, venda } = this.state;
				
		return(	
			<div>
				<h4>Dados da venda</h4>					
						
				<Tabs>
					<TabList>
						<Tab>Detalhes</Tab>
						<Tab>Produtos</Tab>
					</TabList>
					<TabPanel>														
						<Card className="p-3">															
							<Row className="mb-2">
								<Col>
									<span className="font-weight-bold">Cliente: </span>
									<span className="text-success">{ venda.cliente.pessoa.nome }</span>
								</Col>
							</Row>
							
							<Row className="mb-2">
								<Col>
									<span className="font-weight-bold">Data de venda: </span>
									<span className="text-success">{ venda.dataVenda }</span>
								</Col>
							</Row>
							
							<Row className="mb-2">
								<Col>
									<span className="font-weight-bold">Sub total: </span>
									<span className="text-danger">{ sistema.formataReal( venda.subtotal ) }</span>
								</Col>								
								<Col>
									<span className="font-weight-bold">Desconto: </span>
									<span className="text-danger">{ sistema.formataFloat( venda.desconto * 100 )} %</span>
								</Col>
							</Row>
							<Row className="mb-2">
								<Col>
									<span className="font-weight-bold">Total: </span>
									<span className="text-danger">{ sistema.formataReal( venda.subtotal * ( 1.0 - parseFloat( venda.desconto ) ) )}</span>
								</Col>
							</Row>
							<Row className="mb-2">
								<Col>
									<span className="font-weight-bold">Debito: </span>
									<span className="text-danger">{ sistema.formataReal( venda.debito ) }</span>
								</Col>
							</Row>
							<Row className="mb-2">
								<Col>
									<span className="font-weight-bold">Forma de pagamento: </span>
									<span className="text-success">{ venda.formaPag }</span>
								</Col>
							</Row>								
						</Card>
					</TabPanel>		
					<TabPanel>							
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Descrição</th>
										<th>Codigo de barras</th>
										<th>Valor Unitário</th>
										<th>Quantidade</th>
										<th>Unidade</th>
									</tr>
								</thead>
								<tbody>
									{venda.itens.map( (item, index) => {							
										return (
											<tr key={index}>
												<td>{ item.id }</td>
												<td>{ item.produto.descricao }</td>
												<td>{ item.produto.codigoBarras }</td>
												<td>{ sistema.formataReal( item.precoUnitario ) }</td>
												<td>{ sistema.formataFloat( item.quantidade ) }</td>
												<td>{ item.produto.unidade }</td>
											</tr>
										)
									} ) }
								</tbody>
							</Table>
						</div>
					</TabPanel>										
				</Tabs>
					
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />								
					
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaVendas( e ) }>Ir para tela de vendas</button>
						</Col>
					</Row>
				</Card>
			</div>
		);
	}
	
}