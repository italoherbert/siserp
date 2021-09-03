import React from 'react';
import ReactDOM from 'react-dom';

import { Row, Col, Card, Table, Form } from 'react-bootstrap';
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
			venda : { cliente : { pessoa : {} }, itens : [], parcelas : [] },
			total : 0,
			valorPago : 0,
			debito : 0
		};								
	}				
	
	componentDidMount() {	
		sistema.wsGet( '/api/venda/get/'+this.props.vendaId, (resposta) => {
			resposta.json().then( (dados) => {				
				this.setState( { 
					venda : dados,
					total : dados.total,
					debito : dados.debito,
					valorPago : dados.total - dados.debito
				} );
			} );
		}, this );	
	}
	
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}
		
	render() {
		const { infoMsg, erroMsg, venda, total, valorPago, debito } = this.state;
				
		return(	
			<div>
				<h4>Dados da venda</h4>					
						
				<Tabs>
					<TabList>
						<Tab>Detalhes</Tab>
						<Tab>Produtos</Tab>
						<Tab>Parcelas</Tab>
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
									<span className="text-primary">{ sistema.formataReal( venda.subtotal ) }</span>
								</Col>
							</Row>
							<Row className="mb-2">	
								<Col>									
									<span className="font-weight-bold">Desconto: </span>							
									<span className="text-primary">{ sistema.formataFloat( venda.desconto )} %</span>
								</Col>
							</Row>
							<Row className="mb-2">	
								<Col>									
									<span className="font-weight-bold">Total: </span>
									<span className="text-primary">{ sistema.formataReal( venda.subtotal * ( 1.0 - ( parseFloat( venda.desconto ) / 100.0 ) ) ) }</span>
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
					<TabPanel>
						{ venda.parcelas.length === 0 && (
							<MensagemPainel cor="info" msg="Nenhuma parcela registrada para esta venda!" />
						) }
						
						<div className="tbl-pnl-pequeno">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Valor</th>
										<th>Débito</th>
										<th>Data de pagamento</th>
										<th>Data de vencimento</th>
									</tr>
								</thead>
								<tbody>
									{venda.parcelas.map( (item, index) => {							
										return (
											<tr key={index}>
												<td>{ sistema.formataReal( item.valor ) }</td>
												<td>{ sistema.formataReal( item.debito ) }</td>
												<td>{ item.dataPagamento }</td>
												<td>{ item.dataVencimento }</td>												
											</tr>
										)
									} ) }
								</tbody>
							</Table>
						</div>
						
						<br />
						
						<Form.Group style={{fontSize: '1.6em'}}>
							<Form.Label>Total: &nbsp;<span className="text-primary">{ sistema.formataReal( total ) }</span></Form.Label>
							<br />
							<Row>
								<Col>
									<Form.Label>Valor pago: &nbsp;<span className="text-primary">{ sistema.formataReal( valorPago ) }</span></Form.Label>
								</Col>
								<Col>
									<Form.Label>Débito: &nbsp;<span className="text-danger">{ sistema.formataReal( debito ) }</span></Form.Label>
								</Col>
							</Row>
						</Form.Group>
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