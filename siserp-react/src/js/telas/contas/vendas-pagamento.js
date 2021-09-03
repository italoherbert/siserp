import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from '../../logica/sistema';
import MensagemPainel from '../../componente/mensagem-painel';
import InputDropdown from '../../componente/input-dropdown';

import ContasReceber from './contas-receber';

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
		
		let clienteId = this.state.clienteId;
		
		sistema.wsPost( '/api/venda/efetuarecebimento/'+clienteId, {
			"valor" : sistema.paraFloat( this.valorPago.current.value )
		}, (resposta) => {
			this.valorPago.current.value = '';
			this.carregaVendas( clienteId );
			this.setState( { infoMsg : 'Pagamento efetuado com sucesso' } );
		}, this );		
	}
						
	clienteNomeItemClicado( item, index ) {
		const { clientes } = this.state;
				
		let clienteId = clientes[ index ].clienteId;		
		
		this.setState( { clienteId : clienteId } );
		
		this.carregaVendas( clienteId );
	}	

	carregaVendas( clienteId ) {
		sistema.wsGet( '/api/venda/lista/porcliente/'+clienteId, (resposta) => {
			resposta.json().then( (dados) => {
				let vendas = dados;
				
				let debito = 0;
				for( let i = 0; i < vendas.length; i++ ) {
					let subtotal = sistema.paraFloat( vendas[ i ].subtotal );
					let desconto = sistema.paraFloat( vendas[ i ].desconto );
					vendas[ i ].total = subtotal * ( 1.0 - ( desconto / 100.0 ) );

					debito += sistema.paraFloat( vendas[ i ].debito );
				}

				this.setState( { debito : debito, vendas : vendas } );
				
				if ( dados.length === 0 ) 
					this.setState( { infoMsg : 'O cliente está adimplente.' } );
			} );
		}, this );		
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
				
	paraTelaContasReceber() {
		ReactDOM.render( <ContasReceber />, sistema.paginaElemento() ); 
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
						<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaContasReceber( e ) }>Ir para tela de vendas</button>
					</Form>
				</Card>				
			</div>
		);
	}
	
}