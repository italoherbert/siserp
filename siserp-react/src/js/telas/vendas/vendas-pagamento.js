import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from '../../logica/sistema';
import MensagemPainel from '../../componente/mensagem-painel';
import InputDropdown from '../../componente/input-dropdown';

import Vendas from '../vendas/vendas';

export default class VendasPagamento extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,
						
			clientes : [],
			clientesNomeLista : [],
			clienteId : -1,
			clienteNome : '',

			parcelas : [],
			
			vendas : [],
			
			debito : 0,
			troco : 0
		};
		this.valorPago = React.createRef();
	}
	
	efetuarRecebimento( e ) {
		e.preventDefault();
		
		let clienteId = this.state.clienteId;
		
		sistema.wsPost( '/api/conta/receber/efetuarecebimento', {
			"clienteId" : clienteId,
			"valorPago" : sistema.paraFloat( this.valorPago.current.value )
		}, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { troco : sistema.paraFloat( dados.troco ) } )

				this.valorPago.current.value = '';
				this.carregaVendas( clienteId );
			} );			
		}, this );		
	}
								
	carregaVendas( clienteId ) {
		sistema.wsGet( '/api/venda/lista/emdebito/porcliente/'+clienteId, (resposta) => {
			resposta.json().then( (dados) => {
				let vendas = dados;
				
				let debito = 0;
				for( let i = 0; i < vendas.length; i++ ) {
					let subtotal = sistema.paraFloat( vendas[ i ].subtotal );
					let desconto = sistema.paraFloat( vendas[ i ].desconto );
					vendas[ i ].total = subtotal * ( 1.0 - ( desconto / 100.0 ) );

					debito += sistema.paraFloat( vendas[ i ].debito );
				}

				this.setState( { debito : debito, vendas : vendas, parcelas : [] } );
				
				if ( dados.length === 0 ) 
					this.setState( { infoMsg : 'O cliente está adimplente.' } );
			} );
		}, this );		
	}
	
	carregaParcelas( e, vendaId ) {
		e.preventDefault();

		this.setState( { erroMsg : null, infoMsg : null } );

		let vendas = this.state.vendas;

		let carregou = false;
		for( let i = 0; !carregou && i < vendas.length; i++ ) {
			let venda = vendas[ i ];
			if ( venda.id === vendaId ) {
				if ( venda.parcelas.length === 0 ) {
					this.setState( { infoMsg : "Nenhuma parcela para esta venda." } );
				} else {
					this.setState( { parcelas : venda.parcelas } );
				}
				carregou = true;
			}
		}
	}

	clienteNomeItemClicado( item, index ) {
		const { clientes } = this.state;
				
		let clienteId = clientes[ index ].clienteId;		
		
		this.setState( { clienteId : clienteId } );
		
		this.carregaVendas( clienteId );
	}	

	clienteNomeOnChange( item ) {	
		sistema.wsPost( '/api/cliente/filtra/limit/5', {
			"nomeIni" : item
		}, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { clientes : [], clientesNomeLista : [] } );
														
				for( let i = 0; i < dados.length; i++ ) {
					this.state.clientes.push( {
						clienteId : dados[ i ].id,
						clienteNome : dados[ i ].pessoa.nome 
					} );	
					
					this.state.clientesNomeLista.push( dados[ i ].pessoa.nome );
				}

				this.setState( {} );
			} );
		}, this );
	}

	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}	
		
	render() {
		const { infoMsg, erroMsg, clientesNomeLista, vendas, parcelas, debito, troco } = this.state;
		
		return(	
			<div>												
				<h4 className="text-center">Pagamentos</h4>
				
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col>
						<Card className="p-3">
							<Form.Group>
								<Form.Label>
									<h5>Informe o cliente: </h5>
								</Form.Label>						
								<InputDropdown 
									referencia={this.clienteNome} 
									itens={clientesNomeLista} 
									carregaItens={ (item) => this.clienteNomeOnChange( item ) } 
									itemClicado={ (item, index) => this.clienteNomeItemClicado( item, index ) } />									
							</Form.Group>
						</Card>
					</Col>
					<Col>
						<Card className="p-3">
							<span className="display-inline" style={{fontSize : '1.6em'}}> 
								Débito total: &nbsp;
								<span className="text-danger">{sistema.formataReal( debito ) }</span>
							</span>
						</Card>
					</Col>
				</Row>
				
				<br />
				<Row>
					<Col>
						<h4 className="text-center">Lista de vendas em débito</h4>
						<div className="tbl-pnl-pequeno">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Data venda</th>
										<th>total</th>
										<th>Débito</th>
										<th>Ver parcelas</th>
									</tr>
								</thead>
								<tbody>
									{vendas.map( (venda, index) => {
										return (
											<tr key={index}>
												<td>{venda.dataVenda}</td>
												<td>{ sistema.formataReal( venda.total ) }</td>
												<td>{ sistema.formataReal( venda.debito ) }</td>
												<td><button type="button" className="btn btn-link p-0" onClick={ (e) => this.carregaParcelas(e, venda.id ) }>parcelas</button></td>
											</tr>
										)
									} ) }
								</tbody>
							</Table>
						</div>
					</Col>
					<Col>
					<h4 className="text-center">Lista de parcelas</h4>
						<div className="tbl-pnl-pequeno">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Total</th>
										<th>Débito</th>
										<th>Data de pagamento</th>
										<th>Data de vencimento</th>
									</tr>
								</thead>
								<tbody>
									{parcelas.map( (parcela, index) => {
										return (
											<tr key={index}>
												<td>{ sistema.formataReal( parcela.valor ) }</td> 
												<td>{ sistema.formataReal( parcela.debito ) }</td> 
												<td>{ parcela.dataPagamento }</td>
												<td>{ parcela.dataVencimento }</td>
											</tr>
										)
									} ) }
								</tbody>
							</Table>
						</div>			
					</Col>
				</Row>
				<br />
				
				<Row>									
					<Col>
						<Card className="p-3">
							<span className="display-inline" style={{fontSize : '1.6em'}}> 
								Débito total: &nbsp;
								<span className="text-danger">{sistema.formataReal( debito ) }</span>
								<br />
								Troco: &nbsp;
								<span className="text-primary">{sistema.formataReal( troco ) }</span>
							</span>
						</Card>
					</Col>
					<Col>
						<Card className="p-3">
							<h4>Efetuar pagamento</h4>					
							<Form>
								<Form.Group className="mb-2">
									<Form.Label>Valor pago: </Form.Label>
									<Form.Control type="text" ref={this.valorPago} name="valorPago" />
								</Form.Group>
								<Button variant="primary" onClick={ (e) => this.efetuarRecebimento( e ) }>Efetuar pagamento</Button>
							</Form>
						</Card>
					</Col>
				</Row>

				<br />
				
				<Card className="p-3">					
					<Form>
						<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaVendas( e ) }>Ir para tela de vendas</button>
					</Form>
				</Card>				
			</div>
		);
	}
	
}