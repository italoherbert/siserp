import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CompraRegistro from './compra-registro';

export default class Compras extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			compras : [],
			dataIni : new Date(),
			dataFim : new Date()
		};					
	}
			
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { infoMsg : null, erroMsg : null } );

		fetch( "/api/compra/filtra", {
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
					this.setState( { compras : dados } );	
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhuma compra encontrada pelos critérios de busca informados!" } );																		
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			
		} );
	}
	
	changeDataIni( date ) {
		this.setState( { dataIni : date } );		
	}
	
	changeDataFim( date ) {
		this.setState( { dataFim : date } );
	}
	
	detalhes( e, compraId ) {
		
	}
	
	remover( e, compraId ) {
		
	}
	
	paraRegistroForm( e ) {
		e.preventDefault();
		
		ReactDOM.render( <CompraRegistro />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, compras, dataIni, dataFim } = this.state;
				
		return (
			<Container>												
				<Row>
					<Col>
						<Card className="p-3">
							<h4 className="text-center">Lista de Compras</h4>
							<div className="tbl-pnl">
								<Table striped bordered hover>
									<thead>
										<tr>
											<th>ID</th>
											<th>Data compra</th>
											<th>Debito total</th>	
											<th>Detalhes</th>
											<th>Remover</th>
										</tr>
									</thead>
									<tbody>
										{compras.map( ( compra, index ) => {
											return (
												<tr key={index}>
													<td>{compra.id}</td>
													<td>{compra.dataCompra}</td>
													<td>{compra.debitoTotal}</td>														
													<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, compra.id )}>detalhes</button></td>
													<td><button className="btn btn-link p-0" onClick={(e) => this.remover( e, compra.id )}>remover</button></td>
												</tr>
											)
										} ) }	
									</tbody>							
								</Table>
							</div>
						</Card>
					</Col>
				</Row>
				<br />
		
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col className="col-md-12">
						<Form onSubmit={ (e) => this.filtrar( e ) }>
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
								</Col>
							</Row>
						</Form>
						
						<br />																
																							
						<button className="btn btn-link p-0" onClick={ (e) => this.paraRegistroForm( e ) } >Registre uma nova compra</button>
					</Col>
				</Row>																																			
			</Container>	
		)
	}
	
}