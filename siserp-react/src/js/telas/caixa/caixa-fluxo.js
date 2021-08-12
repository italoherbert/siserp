import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaLancamentos from './caixa-lancamentos';

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

		this.incluirFuncionario = React.createRef();
		this.funcionarioNomeIni = React.createRef();
	}
						
	componentDidMount() {
		this.incluirFuncionario.current.checked = false;

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
				"dataFim" : sistema.formataData( this.state.dataFim ),
				"incluirFuncionario" : this.incluirFuncionario.current.checked,
				"funcionarioNomeIni" : this.funcionarioNomeIni.current.value
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {		
					
					for( let i = 0; i < dados.length; i++ ) {					
						let caixa = dados[ i ];
						let debito = 0;
						let credito = 0;
					
						for( let j = 0; j < caixa.lancamentos.length; j++ ) {
							let lancamento = caixa.lancamentos[ j ];
							if ( lancamento.tipo === 'CREDITO' ) {
								credito += sistema.paraFloat( lancamento.valor );
							} else if ( lancamento.tipo === 'DEBITO' ) {
								debito += sistema.paraFloat( lancamento.valor );
							}
						}
					
						let saldo = credito - debito;
						
						this.state.caixas.push( {
							id : caixa.id,
							dataAbertura : caixa.dataAbertura,
							funcionarioNome : caixa.funcionario.pessoa.nome,
							debito : debito,
							credito : credito,
							saldo : saldo
						} );
						
						this.setState( {} );
					}
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhum fluxo de caixa encontrado pelos critérios de busca informados" } );					
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
	
	paraTelaLancamentos( e, caixaId ) {
		ReactDOM.render( <CaixaLancamentos listagemTipo="caixa" caixaId={caixaId} />, sistema.paginaElemento() );
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
										<th>Responsável</th>
										<th>Data de abertura</th>
										<th>Entrada</th>
										<th>Saida</th>
										<th>Saldo</th>
										<th>Visualizar lançamentos</th>
									</tr>
								</thead>
								<tbody>
									{caixas.map( ( caixa, index ) => {
										return (
											<tr key={index}>
												<td>{ caixa.funcionarioNome }</td>
												<td>{ caixa.dataAbertura }</td>
												<td>{ sistema.formataReal( caixa.credito ) }</td>	
												<td>{ sistema.formataReal( caixa.debito ) }</td>	
												<td>{ sistema.formataReal( caixa.saldo ) }</td>	
												<td><button className="btn btn-link p-0" onClick={ (e) => this.paraTelaLancamentos( e, caixa.id ) }>visualizar</button></td>
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
									<Col>										
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
									<Col>										
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
								</Row>							
								
								<Row>
									<Col className="col-md-6 my-2 mb-2">	
										<Form.Group className="mb-2">																								
											<div>
												<Form.Label>Funcionario</Form.Label> &nbsp;&nbsp;
												<input className="my-2" type="checkbox" ref={this.incluirFuncionario} />
												&nbsp; Incluir funcionário no filtro
											</div> 	
											<Form.Control type="text" ref={this.funcionarioNomeIni} name="funcionarioNomeIni" />																								
										</Form.Group>
									</Col>									
								</Row>
							
								<Row>
									<Col>										
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