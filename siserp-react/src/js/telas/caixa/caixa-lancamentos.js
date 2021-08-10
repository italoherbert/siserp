import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaLancamentoNovo from './caixa-lancamento-novo';

export default class CaixaLancamentos extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			lancamentos : [],
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {} 
		};				
	}			

	componentDidMount() {
		this.listar( null, false );
	}
						
	listar( e, listarBTClicado ) {	
		if ( e != null )
			e.preventDefault();
		
		this.setState( { infoMsg : null, erroMsg : null } );
				
		sistema.showLoadingSpinner();
		
		let url;
		if ( this.props.tipoListagem === 'caixa' ) {
			url = "/api/lancamento/lista/"+this.props.caixaId;
		} else {
			url = "/api/lancamento/lista/hoje/"+sistema.usuario.id;
		}

		fetch( url, {
			method : "GET",			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {																							
					this.setState( { lancamentos : [] } );
					
					let creditoAcumulado = 0;
					let debitoAcumulado = 0;
					for( let i = 0; i < dados.length; i++ ) {
						let lanc = dados[ i ];
						
						let debito = 0;
						let credito = 0;
																	
						if ( lanc.tipo === 'CREDITO' ) {
							credito = lanc.valor;							
						} else if ( lanc.tipo === 'DEBITO' ) {
							debito = lanc.valor;
						}
						
						debitoAcumulado += debito;
						creditoAcumulado += credito;
						let saldo = creditoAcumulado - debitoAcumulado;

						this.state.lancamentos.push( {
							id : lanc.id,
							dataOperacao : lanc.dataOperacao,
							debito : debito,
							credito : credito,
							saldo : saldo
						} );
					}
					
					this.setState( {} );
					
					if ( dados.length === 0 && listarBTClicado === true )
						this.setState( { infoMsg : "Nenhum lançamento até agora" } );					
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}		
			sistema.hideLoadingSpinner();			
		} );
	}
	
	removerTodosHojeSeConfirmado( e ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.removerTodosHoje( e );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	removerSeConfirmado( e, lancId ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.remover( e, lancId );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	removerTodosHoje( e ) {
		e.preventDefault();
		
		sistema.showLoadingSpinner();
		
		fetch( '/api/lancamento/deletatodos/hoje/'+sistema.usuario.id, {
			method : 'DELETE',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				this.listar( e, false );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	remover( e, lancId ) {
		e.preventDefault();
		
		sistema.showLoadingSpinner();
		
		fetch( '/api/lancamento/deleta/'+lancId, {
			method : 'DELETE',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				this.listar( e, false );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	paraTelaRegistro() {
		ReactDOM.render( <CaixaLancamentoNovo />, sistema.paginaElemento() );
	}
					
	render() {
		const {	erroMsg, infoMsg, lancamentos, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
				
		return (
			<Container>																												
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de lançamentos</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover os lançamento(s)?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
				
				<Row>
					<Col>
						<Form className="float-end">																
							<Button variant="primary" onClick={ (e) => this.paraTelaRegistro() }>Novo lançamento</Button>
						</Form>
					</Col>
				</Row>
				
				<Row>
					<Col>
						<h4 className="text-center">Lançamentos</h4>
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Data operação</th>
										<th>Entrada</th>
										<th>Saida</th>
										<th>Saldo</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{lancamentos.map( ( lanc, index ) => {
										return (
											<tr key={index}>
												<td>{ lanc.dataOperacao }</td>
												<td>{ sistema.formataReal( lanc.credito ) }</td>	
												<td>{ sistema.formataReal( lanc.debito ) }</td>	
												<td>{ sistema.formataReal( lanc.saldo ) }</td>	
												<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, lanc.id )}>remover</button></td>
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
							<Form>																
								<Button variant="primary" onClick={ (e) => this.listar( e, true ) }>Atualizar lista</Button>
								<Button variant="primary" className="mx-3" onClick={ (e) => this.removerTodosHojeSeConfirmado( e ) }>Remover todos</Button>									
							</Form>
						</Card>												
					</Col>
				</Row>		
					
			</Container>		
		)
	}
	
}