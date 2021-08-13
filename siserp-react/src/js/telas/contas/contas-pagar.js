import React from 'react';
import {Container, Row, Col, Card, Form, Table, Button} from 'react-bootstrap';
import DatePicker from 'react-datepicker';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class ContasPagar extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			
			dataIni : new Date(),
			dataFim : new Date(),
			
			contasObj : { contas : [] }
		};						
		
		this.incluirFornecedor = React.createRef();
		this.fornecedorNomeIni = React.createRef();
		this.incluirContasPagas = React.createRef();
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
		
		fetch( "/api/conta/pagar/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				"incluirPagas" : this.incluirContasPagas.current.checked,
				"incluirFornecedor" : this.incluirFornecedor.current.checked,
				"fornecedorNomeIni" : this.fornecedorNomeIni.current.value,
				"dataIni" : sistema.formataData( this.state.dataIni ),
				"dataFim" : sistema.formataData( this.state.dataFim )
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { contasObj : dados } );
										
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhuma conta encontrada pelos critérios de filtro informados!" } );																							
				} );				
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	alterarSituacao( e, index, parcelaId ) {
		e.preventDefault();
				
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let parcelaPaga = this.state.contasObj.contas[ index ].parcela.paga;

		sistema.showLoadingSpinner();
		
		fetch( '/api/conta/pagar/altera/situacao/'+parcelaId, {
			method : 'PATCH',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				paga : ( parcelaPaga === 'true' ? false : true )
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
								<th>Data pagamento</th>
								<th>Data vencimento</th>
								<th>Fornecedor</th>
								<th>Alterar status</th>
							</tr>
						</thead>
						<tbody>
							{contasObj.contas.map( ( conta, index ) => {
								return (
									<tr key={index}>
										<td>{ conta.parcela.dataPagamento }</td>
										<td>{ conta.parcela.dataVencimento }</td>
										<td>{ conta.fornecedorEmpresa }</td>
										<td>
											<Button variant="primary" onClick={(e) => this.alterarSituacao( e, index, conta.parcela.id )}>
												{ conta.parcela.paga === 'true' ? 'Desfazer pagamento' : 'Pagar' }
											</Button>
										</td>
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
								<Form.Label>Débito: &nbsp;<span className="text-danger">{ sistema.formataReal( contasObj.debitoTotalPeriodo ) }</span></Form.Label>							
							</Col>
							<Col>
								<Form.Label>Débito total: &nbsp;<span className="text-danger">{ sistema.formataReal( contasObj.debitoTotalCompleto ) }</span></Form.Label>
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
							<h4>Filtrar contas a pagar</h4>
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Row>
									<Col>										
										<Form.Group className="pb-2">													
											<Form.Label>Data de início: </Form.Label>
											<br />
											<DatePicker selected={dataIni} 
													onChange={ (date) => this.changeDataIni( date ) } 
													locale="pt" 
													name="dataIni" 
													dateFormat="dd/MM/yyyy" className="form-control" />						
										</Form.Group>									
									</Col>
									<Col>										
										<Form.Group className="pb-2">													
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
									<Col>
										<Form.Group className="mb-2">													
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<input className="my-2" type="checkbox" ref={this.incluirContasPagas} /> &nbsp; Incluir contas pagas										
										</Form.Group>
									</Col>
								</Row>		
								<Row>
									<Col>										
										<Form.Group className="mb-2">													
											<Row>
												<Col>
													<Form.Label>Fornecedor: </Form.Label>
													<Form.Control type="text" ref={this.fornecedorNomeIni} name="fornecedorNomeIni" />
													
													<input className="my-2" type="checkbox" ref={this.incluirFornecedor} />Incluir fornecedor no filtro
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