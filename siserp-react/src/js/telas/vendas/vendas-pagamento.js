import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';
import InputDropdown from './../../componente/input-dropdown';

import Vendas from './vendas';

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
			
			vendas : [],
			
			debito : 0
		};
		this.valorPago = React.createRef();
	}
	
	efetuarPagamento( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		sistema.showLoadingSpinner();
		
		let clienteId = this.state.clienteId;
		
		fetch( '/api/venda/efetuarecebimento/'+clienteId, {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				"valor" : sistema.paraFloat( this.valorPago.current.value )
			} )
		} ).then( (resposta) => {
			if ( resposta.status == 200 ) {
				this.valorPago.current.value = '';
				this.carregaVendas( clienteId );
				this.setState( { infoMsg : 'Pagamento efetuado com sucesso' } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} )
	}
						
	clienteNomeItemClicado( item, index ) {
		const { clientes } = this.state;
				
		let clienteId = clientes[ index ].clienteId;		
		
		this.setState( { clienteId : clienteId } );
		
		this.carregaVendas( clienteId );
	}	

	carregaVendas( clienteId ) {
		sistema.showLoadingSpinner();
						
		this.setState( { erroMsg : null, infoMsg : null } );
		
		fetch( '/api/venda/lista/porcliente/'+clienteId, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					let vendas = dados;
					
					let debito = 0;
					for( let i = 0; i < vendas.length; i++ ) {
						let subtotal = sistema.paraFloat( vendas[ i ].subtotal );
						let desconto = sistema.paraFloat( vendas[ i ].desconto );
						vendas[ i ].total = subtotal * ( 1.0 - desconto );

						debito += sistema.paraFloat( vendas[ i ].debito );
					}

					this.setState( { debito : debito, vendas : vendas } );
					
					if ( dados.length === 0 ) 
						this.setState( { infoMsg : 'O cliente está adimplente.' } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
				
	clienteNomeOnChange( item ) {		
		fetch( '/api/cliente/filtra/limit/5', {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				"nomeIni" : item
			} )
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
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
			}
		} );
	}
				
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}	
		
	render() {
		const { infoMsg, erroMsg, clientesNomeLista, vendas, debito } = this.state;
							
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
				
				<h4 className="text-center">Lista de vendas</h4>
				<div className="tbl-pnl-pequeno">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Data venda</th>
								<th>total</th>
								<th>Débito</th>
							</tr>
						</thead>
						<tbody>
							{vendas.map( (venda, index) => {
								return (
									<tr key={index}>
										<td>{venda.dataVenda}</td>
										<td>{ sistema.formataReal( venda.total ) }</td>
										<td>{ sistema.formataReal( venda.debito ) }</td>
									</tr>
								)
							} ) }
						</tbody>
					</Table>
				</div>
				
				<br />
				
				<Card className="p-3 col-sm-6">
					<h4>Efetuar pagamento</h4>
					
					<Form>
						<Form.Group className="mb-2">
							<Form.Label>Valor pago: </Form.Label>
							<Form.Control type="text" ref={this.valorPago} name="valorPago" />
						</Form.Group>
						<Button variant="primary" onClick={ (e) => this.efetuarPagamento( e ) }>Efetuar pagamento</Button>
					</Form>
				</Card>
				
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