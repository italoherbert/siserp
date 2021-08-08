import React from 'react';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class CaixaFluxo extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			dataIni : '',
			dataFim : '',
			caixas : []
		};				
	}
						
	componentDidMount() {
		this.setState( { dataIni : new Date(), dataFim : new Date() } );
	}		
						
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { infoMsg : null, erroMsg : null } );
				
		sistema.showLoadingSpinner();

		fetch( "/api/caixa/filtra/", {
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
					if ( dados.length === 0 ) {
						this.setState( { infoMsg : "Nenhum fluxo de caixa encontrado pelos critérios de busca informados" } );
					} else {				
						this.setState( { caixas : dados, infoMsg : "Fluxos de caixas carregados com sucesso." } );																							
					}
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}		
			sistema.hideLoadingSpinner();			
		} );
	}
	
	dataIniOnChange( data ) {
		this.setState( { dataIni : data } );
	}
	
	dataFimOnChange( data ) {
		this.setState( { dataFim : data } );
	}
				
	render() {
		const {	erroMsg, infoMsg, caixas, dataIni, dataFim } = this.state;
				
		return (
			<Container>																								
				<Row>
					<Col>
						<h4 className="text-center">Fluxo de caixa</h4>
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Data de abertura</th>
										<th>Valor final</th>
									</tr>
								</thead>
								<tbody>
									{caixas.map( ( caixa, index ) => {
										return (
											<tr key={index}>
												<td>{caixa.id}</td>
												<td>{ sistema.formataData( caixa.dataAbertura ) }</td>
												<td>{ sistema.formataReal( caixa.valor ) }</td>	
											</tr>
										)
									} ) }	
								</tbody>							
							</Table>
						</div>
					</Col>
				</Row>
				<br />
		
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col>
						<Card className="p-3">
							<h4>Filtrar</h4>
							<Form onSubmit={ (e) => this.filtrar( e ) }>
								<Row>
									<Col className="col-sm-4">										
										<Form.Group className="pb-2">													
											<Form.Label>Data de início: </Form.Label>
											<br />
											<DatePicker selected={dataIni} 
													onChange={ (date) => this.dataIniOnChange( date ) } 
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
													onChange={ (date) => this.dateFimOnChange( date ) } 
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
		)
	}
	
}