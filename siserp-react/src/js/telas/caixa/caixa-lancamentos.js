import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Card, Form, Table, Button } from 'react-bootstrap';
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
			caixaFuncNome : null,
			caixaDataAbertura : null,
			
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
		if ( this.props.getTipo === "caixaid" ) {
			url = '/api/caixa/get/'+this.props.caixaId;
		} else {
			url = '/api/caixa/get/uid/hoje/'+sistema.usuario.id;
		}
				
		sistema.wsGet( url, (resposta) => {
			resposta.json().then( (dados) => {										
				this.setState( { lancamentos : [] } );
				
				let caixa = dados;					
				
				let creditoAcumulado = 0;
				let debitoAcumulado = 0;
				for( let i = 0; i < caixa.lancamentos.length; i++ ) {
					let lanc = caixa.lancamentos[ i ];
					
					let debito = 0;
					let credito = 0;
																
					if ( lanc.tipo === 'CREDITO' ) {
						credito = sistema.paraFloat( lanc.valor );							
					} else if ( lanc.tipo === 'DEBITO' ) {
						debito = sistema.paraFloat( lanc.valor );
					}
					
					debitoAcumulado += debito;
					creditoAcumulado += credito;
					let saldo = creditoAcumulado - debitoAcumulado;

					this.state.lancamentos.push( {
						id : lanc.id,
						dataOperacao : lanc.dataOperacao,
						obs : lanc.obs,
						debito : debito,
						credito : credito,
						saldo : saldo
					} );
				}
				
				this.setState( { caixaFuncNome : caixa.funcionario.pessoa.nome, caixaDataAbertura : caixa.dataAbertura } );
				
				if ( dados.length === 0 && listarBTClicado === true )
					this.setState( { infoMsg : "Nenhum lançamento até agora" } );					
			} );
		}, this ); 				
	}
					
	remover( e, lancId ) {
		e.preventDefault();
				
		sistema.wsDelete( '/api/lancamento/deleta/'+lancId, (resposta) => {
			this.listar( e, false );
		}, this );		
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
		
	paraTelaRegistro() {
		ReactDOM.render( <CaixaLancamentoNovo />, sistema.paginaElemento() );
	}
					
	render() {
		const {	erroMsg, infoMsg, lancamentos, caixaFuncNome, caixaDataAbertura, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
				
		return (
			<Container>																												
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de lançamentos</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o lançamento?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
				
				{ this.props.gettipo !== 'caixaid' && (
					<Form className="float-end">																
						<Button variant="primary" onClick={ (e) => this.paraTelaRegistro() }>Novo lançamento</Button>
					</Form>
				) }
				
				<br />
				
				<Form.Group style={{fontSize: '1.2em'}}>
					<Form.Label>Funcionario: &nbsp; <span className="text-primary">{ caixaFuncNome }</span></Form.Label>
					<br />
					<Form.Label>Data abertura do caixa: &nbsp; <span className="text-primary">{ caixaDataAbertura }</span></Form.Label>
				</Form.Group>
					
		
				<h4 className="text-center">Lançamentos</h4>
				<div className="tbl-pnl">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Data operação</th>
								<th>Entrada</th>
								<th>Saida</th>
								<th>Saldo</th>
								<th>Obs</th>
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
										<td>{ lanc.obs }</td>
										<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, lanc.id )}>remover</button></td>
									</tr>
								)
							} ) }	
						</tbody>							
					</Table>
				</div>					
				
				<br />
		
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Card className="p-3">
					<Form>																
						<Button variant="primary" onClick={ (e) => this.listar( e, true ) }>Atualizar lista</Button>
					</Form>
				</Card>					
			</Container>		
		)
	}
	
}