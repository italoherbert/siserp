import React from 'react';
import { Container, Card, Table } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

export default class CompraRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,
			compra : { parcelas : [], itens : [], fornecedor : { empresa : '' } }
		};								
	}				
	
	componentDidMount() {		
		fetch( '/api/compra/get/'+this.props.compraId, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { compra : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
		
	render() {
		const { infoMsg, erroMsg, compra } = this.state;
				
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
							<div className="tbl-pnl">
								<Table striped bordered hover>
									<thead>
										<tr>
											<th>ID</th>
											<th>Valor Unitário</th>
											<th>Quantidade</th>
										</tr>
									</thead>
									<tbody>
										{compra.itens.map( (item, index) => {							
											return (
												<tr key={index}>
													<td>{ item.id }</td>
													<td>{ sistema.formataReal( item.precoUnitario ) }</td>
													<td>{ item.quantidade }</td>
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
										</tr>
									</thead>
									<tbody>
										{compra.parcelas.map( (item, index) => {							
											return (
												<tr key={index}>
													<td>{ sistema.formataReal( item.valor ) }</td>
													<td>{ item.dataPagamento }</td>
													<td>{ item.dataVencimento }</td>
												</tr>
											)
										} ) }
									</tbody>
								</Table>
							</div>
						</TabPanel>						
					</Tabs>
					
					<MensagemPainel cor="danger" msg={erroMsg} />
					<MensagemPainel cor="primary" msg={infoMsg} />
				</Card>
			</Container>
		);
	}
	
}