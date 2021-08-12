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
			
			contas : []
		};						
	}
	
	componentDidMount() {		
		this.filtrar( null, false );		
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
				"dataIni" : sistema.formataData( this.state.dataIni ),
				"dataFim" : sistema.formataData( this.state.dataFim )
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { contas : dados } );
					
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhuma conta registrado!" } );																							
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
		
		let parcelaPaga = this.state.contas[ index ].parcela.paga;

		sistema.showLoadingSpinner();
		
		fetch( '/api/conta/pagar/altera/situacao'+parcelaId, {
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
				this.filtra( e, false );				
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
		const { erroMsg, infoMsg, contas, dataIni, dataFim } = this.state;
		
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
							{contas.map( ( conta, index ) => {
								return (
									<tr key={index}>
										<td>{ sistema.formataData( conta.parcela.dataPagamento ) }</td>
										<td>{ sistema.formataData( conta.parcela.dataVencimento ) }</td>
										<td>{ conta.fornecedorEmpresa }</td>
										<td>
											<button className="btn btn-link p-0" onClick={(e) => this.alterarSituacao( e, index, conta.parcela.id )}>
												{ conta.parcela.paga === 'true' ? 'Desfazer pagamento' : 'Pagar' }
											</button>
										</td>
									</tr>
								);
							} ) }	
						</tbody>							
					</Table>
				</div>
					
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col>
						<Card className="p-3">
							<h4>Filtrar contas a pagar</h4>
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Row>
									<Col className="col-sm-4">										
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
									<Col className="col-sm-4">										
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
									<Col className="col-md-4">
										<div className="mb-2">&nbsp;</div>
										<Button type="submit" variant="primary">Filtrar</Button>				
										<br />
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