import React from 'react';
import ReactDOM from 'react-dom';

import { Container, Row, Col, Card, Table, Form } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import Compras from './compras';

export default class CompraRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,
			compra : { parcelas : [], itens : [], fornecedor : { empresa : '' } },
			
			total : 0, 
			valorPago : 0, 
			debito : 0
		};								
	}				
	
	componentDidMount() {	
		sistema.showLoadingSpinner();
	
		fetch( '/api/compra/get/'+this.props.compraId, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					let total = 0;
					let valorPago = 0;					
					for( let i = 0; i < dados.parcelas.length; i++ ) {
						let valor = sistema.paraFloat( dados.parcelas[ i ].valor );
						
						if ( dados.parcelas[ i ].paga === 'true' )
							valorPago += valor;
						
						total += valor;						
					}
										
					let debito = total - valorPago;					
					
					this.setState( { compra : dados, total : total, valorPago : valorPago, debito : debito } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();			
		} );
	}
	
	pagarParcela( e, parcelaId ) {
		
	}
	
	paraTelaCompras() {
		ReactDOM.render( <Compras />, sistema.paginaElemento() ); 
	}
		
	render() {
		const { infoMsg, erroMsg, compra, total, valorPago, debito } = this.state;
				
		return(	
			<Container>
				<Card className="p-3">
					<h4>Dados da compra</h4>
					<br />
					<div>Data de compra: &nbsp; <span className="text-info">{compra.dataCompra}</span></div>
					<div>Fornecedor: &nbsp; <span className="text-info">{compra.fornecedor.empresa}</span></div>
					<br />
					
					<Tabs>
						<TabList>
							<Tab>Produtos</Tab>
							<Tab>Parcelas</Tab>
						</TabList>
						<TabPanel>							
							<div className="tbl-pnl-pequeno">
								<Table striped bordered hover>
									<thead>
										<tr>
											<th>ID</th>
											<th>Descrição</th>
											<th>Codigo de barras</th>
											<th>Valor Unitário</th>
											<th>Quantidade comprada</th>
										</tr>
									</thead>
									<tbody>
										{compra.itens.map( (item, index) => {							
											return (
												<tr key={index}>
													<td>{ item.id }</td>
													<td>{ item.produto.descricao }</td>
													<td>{ item.produto.codigoBarras }</td>
													<td>{ sistema.formataReal( item.precoUnitario ) }</td>
													<td>{ sistema.formataFloat( item.quantidade ) }</td>
												</tr>
											)
										} ) }
									</tbody>
								</Table>
							</div>
						</TabPanel>
						<TabPanel>
							{ compra.parcelas.length === 0 ?
								<MensagemPainel cor="info" msg="Nenhuma parcela registrada para esta compra!" /> : 
								<span></span> 
							}
							
							<div className="tbl-pnl-pequeno">
								<Table striped bordered hover>
									<thead>
										<tr>
											<th>Valor</th>
											<th>Data de pagamento</th>
											<th>Data de vencimento</th>
											<th>Situação</th>
										</tr>
									</thead>
									<tbody>
										{compra.parcelas.map( (item, index) => {							
											return (
												<tr key={index}>
													<td>{ sistema.formataReal( item.valor ) }</td>
													<td>{ item.dataPagamento }</td>
													<td>{ item.dataVencimento }</td>
													<td className={item.paga === 'true' ? "text-primary" : "text-danger" }>
														{ ( item.paga === 'true' ? 'Paga' : 'Em débito' ) }
													</td>
												</tr>
											)
										} ) }
									</tbody>
								</Table>
							</div>
							
							<br />
							
							<Form.Group style={{fontSize: '1.6em'}}>
								<Form.Label>Total: &nbsp;<span className="text-danger">{ sistema.formataReal( total ) }</span></Form.Label>
								<br />
								<Row>
									<Col>
										<Form.Label>Valor pago: &nbsp;<span className="text-danger">{ sistema.formataReal( valorPago ) }</span></Form.Label>
									</Col>
									<Col>
										<Form.Label>Débito: &nbsp;<span className="text-danger">{ sistema.formataReal( debito ) }</span></Form.Label>
									</Col>
								</Row>
							</Form.Group>
						</TabPanel>						
					</Tabs>
					
					<MensagemPainel cor="danger" msg={erroMsg} />
					<MensagemPainel cor="primary" msg={infoMsg} />
				</Card>
				
				<br />
				
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCompras( e ) }>Ir para tela de compras</button>
						</Col>
					</Row>
				</Card>
			</Container>
		);
	}
	
}