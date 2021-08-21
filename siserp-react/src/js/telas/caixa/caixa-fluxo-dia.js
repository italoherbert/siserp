import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Table, Form, Button } from 'react-bootstrap';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaLancamentos from './caixa-lancamentos';

export default class CaixaFluxoDia extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			caixas : []
		};				
	}		
		
	componentDidMount() {				
		sistema.wsPost( "/api/caixa/lista/pordatadia", {
			"dataDia" : this.props.dataDia
		}, (resposta) => {
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
		}, this );	
	}
	
	relatorioBalancoHoje( e ) {
		e.preventDefault();
		
		sistema.wsGetPDF( "/api/relatorio/balanco-hoje", (resposta) => {
			resposta.blob().then( (dados) => {
				window.open( window.URL.createObjectURL( dados ) ); 
				this.setState( { infoMsg : "Relatório gerado com êxito!" } );																							
			} );
		}, this );		
	}
	
	paraTelaLancamentos( e, caixaId ) {
		ReactDOM.render( <CaixaLancamentos getTipo="caixaid" caixaId={caixaId} />, sistema.paginaElemento() );
	}
						
	render() {
		const {	erroMsg, infoMsg, caixas } = this.state;
				
		return (
			<Container>
				<Form>
					<Button className="float-end" variant="primary" onClick={ (e) => this.relatorioBalancoHoje( e ) }>Gerar balanço de hoje</Button>
				</Form>
				<br />
				<h4 className="text-center">Fluxo de caixa por dia</h4>
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
					
				<br />
		
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
																		
			</Container>		
		)
	}
	
}