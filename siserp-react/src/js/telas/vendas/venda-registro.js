import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import Vendas from './vendas';
import AddVendaProduto from './registro/add-venda-produto';
import ConfigVendaPagamento from './registro/config-venda-pagamento';
import GeraVendaParcelas from './registro/gera-venda-parcelas';

export default class VendaRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,

			itens : [],
			parcelas : [],
			
			valores : {
				subtotal : 0,
				total : 0,
				troco : 0,
				desconto : 0,
				valorPago : 0,
				formaPag : ''
			},

			cliente : {
				incluir : false
			}
		};	
		
		this.clienteNome = React.createRef();
	}
		
	efetuarVenda( e ) {
		const { itens, parcelas, valores } = this.state;
		
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
							
		if ( itens.length === 0 ) {
			this.setState( { erroMsg : "Nenhum produto adicionado." } );
			return;
		}

		if ( valores.formaPag === 'APRAZO' && parcelas.length === 0 ) {
			this.setState( { erroMsg : "Nenhuma parcela informada." } );
			return;
		}

		let itensVenda = [];
		for( let i = 0; i < itens.length; i++ ) {
			let quant = parseFloat( itens[ i ].quantidade );
			if( isNaN( quant ) === true ) {
				this.setState( { erroMsg : "Quantidade em formato inválido. Quantidade="+quant } );
				return;
			}
			
			let estQ = parseFloat( itens[ i ].estoqueQuantidade );
			if ( quant > estQ ) {
				let q1 = sistema.formataFloat( quant );
				let q2 = sistema.formataFloat( estQ );
				this.setState( { erroMsg : "Não há quantidade suficiente em estoque. ( "+q1+">"+q2+"  ), Produto= "+itens[ i ].descricao } );			
				return;
			}
			
			itensVenda.push( {
				codigoBarras : itens[ i ].codigoBarras,
				quantidade : itens[ i ].quantidade,
				precoUnitario : sistema.paraFloat( itens[ i ].precoUnitario )
			} );
		}
		
		let parcelasList = [];
		for( let i = 0; i < parcelas.length; i++ ) {
			let p = parcelas[ i ];
			parcelasList.push( {
				valor : p.valor,
				dataPagamento : sistema.formataData( p.dataPagamento ),
				dataVencimento : sistema.formataData( p.dataVencimento )
			} );
		}

		let clienteNomeValor = "";
		if ( this.state.cliente.incluir === true )
			clienteNomeValor = this.clienteNome.current.value;		

		sistema.wsPost( '/api/venda/efetua', {
			subtotal : sistema.paraFloat( this.state.valores.subtotal ),
			desconto : sistema.paraFloat( this.state.valores.desconto ),
			formaPag : this.state.formaPag,
			incluirCliente : this.state.cliente.incluir,
			clienteNome : clienteNomeValor,
			itensVenda : itensVenda,
			parcelas : parcelasList
		}, (resposta) => {									
			this.setState( { 
				infoMsg : 'Venda registrada com êxito',
				itens : [],
				valores : {
					subtotal : 0,
					total : 0,
					troco : 0,
					valorPago : 0,
					desconto : 0,				
				}
			} );
		}, this );		
	}
	
	formaPagOnChange( formaPag ) {
		this.setState( { formaPag : formaPag } ); 
	}

	calcularTotal( e ) {
		e.preventDefault();

		const { itens, valores } = this.state;
		
		let desconto = valores.desconto;
		let valorPago = valores.valorPago;
		
		let desconto001 = parseFloat( desconto );
		if ( isNaN( desconto001 ) === true ) {
			desconto001 = 0;					
		} else {
			desconto001 /= 100.0;
		}			
				
		for( let i = 0; i < itens.length; i++ ) {
			if ( isNaN( itens[ i ].precoUnitario ) ) {
				this.setState( { erroMsg : 'Preço unitário em formato inválido' } );
				return;
			}
			
			let quant = parseFloat( itens[ i ].quantidade );
			if( isNaN( quant ) === true )
				return;
			
			if ( quant > itens[ i ].estoqueQuantidade ) {
				this.setState( { erroMsg : "Quantidade maior que a quantidade em estoque para o produto: "+itens[ i ].descricao } );			
				return;
			}
		}
				
		let subtotal = 0;
		for( let i = 0; i < itens.length; i++ )
			subtotal += parseFloat( itens[ i ].quantidade ) * parseFloat( itens[ i ].precoUnitario );				
								
		let total = subtotal * ( 1.0 - desconto001 );		
		
		let valorPagoFloat = parseFloat( sistema.paraFloat( valorPago ) );
		
		let troco = 0;
		if ( !isNaN( valorPagoFloat ) )
			troco = valorPagoFloat - total;		
		
		this.setState( { valores : { 
			subtotal : subtotal, total : total, troco : troco, 
			desconto : desconto, 
			valorPago : valorPago } } );
	}
			
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}	
		
	render() {
		const { infoMsg, erroMsg, itens, valores, cliente, parcelas, formaPag } = this.state;
							
		return(	
			<div>	
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Tabs forceRenderTabPanel={true}>
					<TabList>
						<Tab>Itens de produtos</Tab>
						<Tab>Config. Pagamento</Tab>
						<Tab>Simulação de parcelas</Tab>						
					</TabList>
					<TabPanel>	
						<AddVendaProduto calcularTotal={ (e) => this.calcularTotal( e ) } itens={itens} />										
					</TabPanel>
					<TabPanel>																				
						<ConfigVendaPagamento 
							calcularTotal={ (e) => this.calcularTotal( e ) } 
							formaPagOnChange={ (formaPag) => this.formaPagOnChange( formaPag ) } 
							valores={valores} 
							cliente={cliente} 
							clienteNomeReferencia={this.clienteNome} />
					</TabPanel>
					<TabPanel>
						{ formaPag === 'APRAZO' ?
							<GeraVendaParcelas parcelas={parcelas} valores={valores} /> :
							<div className="p-3 text-primary">Modo de pagamentos à prazo não selecionado.</div>
						}	
					</TabPanel>
				</Tabs>

				<br />

				<Card className="p-3" style={{fontSize : '1.6em' }}>
					<Row>
						<Col>
							<Form.Label>Subtotal: &nbsp; <span className="text-danger">{ sistema.formataReal( valores.subtotal ) }</span></Form.Label>
						</Col>
						<Col>
							<Form.Label>Desconto (%): &nbsp;  <span className="text-danger">{ sistema.formataFloat( valores.desconto ) }</span></Form.Label>
						</Col>
					</Row>
					<Row>
						<Col>
							<Form.Label>Total: &nbsp; <span className="text-danger">{ sistema.formataReal( valores.total ) }</span></Form.Label>										
						</Col>
						<Col>
							<Form.Label>Troco: &nbsp; <span className="text-danger">{ sistema.formataReal( valores.troco ) }</span></Form.Label>
						</Col>
					</Row>
				</Card>
				<br />				
																			
				<Card className="p-3 text-left">	
					<Row>
						<Col>
							<Form>
								<Button variant="primary" onClick={ (e) => this.efetuarVenda( e ) }>Efetuar venda</Button>
							</Form>
						</Col>
					</Row>
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