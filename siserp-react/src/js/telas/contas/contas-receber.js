import React from 'react';
import {Container, Row, Col, Card, Form, Table, Button} from 'react-bootstrap';
import DatePicker from 'react-datepicker';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class ContasReceber extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			
			dataIni : new Date(),
			dataFim : new Date(),
			
			contasObj : { vendas : [] },
			debitoPeriodo : 0,
			debitoTotal : 0
		};						
		
		this.valorRecebido = React.createRef();
		this.incluirCliente = React.createRef();
		this.clienteNomeIni = React.createRef();
	}
	
	componentDidMount() {		
		this.filtrar( null, false );		
		
		this.setState( { dataFim : new Date().getTime() + 365 * 24 * 60 * 60 * 1000 } );
	}
	
	
	filtrar( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();

		this.setState( { erroMsg : null, infoMsg : null } );

		sistema.showLoadingSpinner();
		
		fetch( "/api/conta/receber/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"incluirCliente" : this.incluirCliente.current.checked,
				"clienteNomeIni" : this.clienteNomeIni.current.value,
				"dataIni" : sistema.formataData( this.state.dataIni ),
				"dataFim" : sistema.formataData( this.state.dataFim )
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					let contasObj = dados;
					
					for( let i = 0; i < contasObj.vendas.length; i++ ) {
						let subtotal = sistema.paraFloat( contasObj.vendas[ i ].subtotal );
						let desconto = sistema.paraFloat( contasObj.vendas[ i ].desconto );
						contasObj.vendas[ i ].total = subtotal * ( 1.0 - desconto );
					}
										
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhuma conta encontrada pelos critérios de filtro informados!" } );																							
					
					this.setState( { contasObj : contasObj } );
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
			
	efetuarRecebimento( e, index, clienteId ) {
		e.preventDefault();
				
		this.setState( { erroMsg : null, infoMsg : null } );
		
		sistema.showLoadingSpinner();
		
		fetch( '/api/conta/receber/efetuarecebimento/'+clienteId, {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				valorRecebido : this.valorRecebido.current.value
			} )
		} ).then( (resposta) => {			
			if ( resposta.status === 200 ) {
				this.filtrar( e, false );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );			
	}
	
	changeDataIni( date ) {
		this.setState( { dataIni : date } );		
	}
	
	changeDataFim( date ) {
		this.setState( { dataFim : date } );
	}
				
	render() {
		const { erroMsg, infoMsg, contasObj, dataIni, dataFim } = this.state;
		
		return (
			<Container>					
				<h4 className="text-center">Lista de contas a pagar</h4>
				<div className="tbl-pnl">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Data de venda</th>
								<th>Cliente</th>
								<th>Total</th>
								<th>Débito</th>
							</tr>
						</thead>
						<tbody>
							{contasObj.vendas.map( ( venda, index ) => {
								return (
									<tr key={index}>
										<td>{ venda.dataVenda }</td>
										<td>{ venda.cliente.pessoa.nome }</td>
										<td>{ sistema.formataReal( venda.total ) }</td>
										<td>{ sistema.formataReal( venda.debito ) }</td>
									</tr>
								);
							} ) }	
						</tbody>							
					</Table>
				</div>
	
				<br />
				
				<Card className="p-3">
					<Form.Group style={{fontSize: '1.6em'}}>
						<Row>
							<Col>
								<Form.Label>Valor a receber: &nbsp;<span className="text-danger">{ sistema.formataReal( contasObj.totalPeriodo ) }</span></Form.Label>							
							</Col>
							<Col>
								<Form.Label>Valor a receber total: &nbsp;<span className="text-danger">{ sistema.formataReal( contasObj.totalCompleto ) }</span></Form.Label>
							</Col>
						</Row>
					</Form.Group>
				</Card>
					
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col className="col-md-8">
						<Card className="p-3">
							<h4>Filtrar contas receber</h4>
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Row>
									<Col className="col-sm-4">										
										<Form.Group className="mb-2">													
											<Form.Label>Data de início: </Form.Label>
											<br />
											<DatePicker selected={dataIni} 
													onChange={ (date) => this.changeDataIni( date ) } 
													locale="pt" 
													name="dataIni" 
													dateFormat="dd/MM/yyyy" className="form-control" />						
										</Form.Group>									
									</Col>
									<Col className="col-sm-4">										
										<Form.Group className="mb-2">													
											<Form.Label>Data de fim: </Form.Label>
											<br />
											<DatePicker selected={dataFim} 
													onChange={ (date) => this.changeDataFim( date ) } 
													locale="pt" 
													name="dataFim" 
													minDate={dataIni}
													dateFormat="dd/MM/yyyy" className="form-control" />						
										</Form.Group>									
									</Col>										
								</Row>						
								<Row>
									<Col className="col-md-6">										
										<Form.Group className="mb-2">													
											<Row>
												<Col>
													<Form.Label>Cliente: </Form.Label>
													<Form.Control type="text" ref={this.clienteNomeIni} name="clienteNomeIni" />
													
													<input className="my-2" type="checkbox" ref={this.incluirCliente} />Incluir cliente no filtro
												</Col>
											</Row>
										</Form.Group>
									</Col>									
								</Row>
								<Row>
									<Col>
										<Button type="submit" variant="primary">Filtrar</Button>														
									</Col>
								</Row>
							</Form>						
						</Card>							
					</Col>
				</Row>						
			</Container>					
		);
	}
	
}