import React from 'react';
import ReactDOM from 'react-dom';
import { Card, Table, Form, Button } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import AddCompraProduto from './registro/add-compra-produto';
import SetCompraFornecedor from './registro/set-compra-fornecedor';
import GeraCompraParcelas from './registro/gera-compra-parcelas';

import Compras from './compras';

export default class CompraRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,
			produtos : [],
			parcelas : [],
			geraParcelasConfig : {
				valorTotal : 0,
			}
		};	
		
		this.quantParcelasRef = React.createRef();
		this.fornecedorEmpresaRef = React.createRef();
	}				
	
	produtoAdicionado( produto ) {
		this.state.produtos.push( produto );
		this.setState( { parcelas : [] } );
		
		sistema.scrollTo( 'produtos-tbl-pnl' );
	}
	
	removeProduto( e, index ) {
		e.preventDefault();
		
		this.state.produtos.splice( index, 1 );
		this.setState( { parcelas : [] } );
	}
	
	registrarCompra( e ) {
		const { produtos, parcelas } = this.state;
		
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let itensCompra = [];
		for( let i = 0; i < produtos.length; i++ ) {
			let p = produtos[ i ];
			itensCompra.push( {				
				precoUnitario : p.precoUnitCompra,
				quantidade : p.paraAddQuantidade,
				produto : {
					descricao : p.descricao,
					codigoBarras : p.codigoBarras,
					precoUnitCompra : p.precoUnitCompra,
					precoUnitVenda : p.precoUnitVenda,
					unidade : p.unidade,
					
					categoriaMaps : p.categoriaMaps,
					quantidade : p.paraAddQuantidade
				}
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
				
		sistema.wsPost( '/api/compra/registra', {
			dataCompra : sistema.formataData( new Date() ),
			fornecedor : {
				empresa : this.fornecedorEmpresaRef.current.value
			},
			itensCompra : itensCompra,
			parcelas : parcelasList
		}, (resposta) => {
			this.fornecedorEmpresaRef.current.value = '';
			this.quantParcelasRef.current.value = '';
			this.setState( { 
				infoMsg : 'Compra registrada com êxito',
				produtos : [],
				parcelas : [],
				geraParcelasConfig : {
					valorTotal : 0
				}
			} );
		}, this );							
	}
			
	paraTelaCompras() {
		ReactDOM.render( <Compras />, sistema.paginaElemento() ); 
	}	
		
	render() {
		const { infoMsg, erroMsg, produtos, parcelas, geraParcelasConfig } = this.state;
				
		return(	
			<div>
				<Tabs forceRenderTabPanel={true}>
					<TabList>
						<Tab>Produtos</Tab>
						<Tab>Fornecedor</Tab>
						<Tab>Parcelas</Tab>
					</TabList>
					<TabPanel>																				
						<h4 className="text-center">Lista de Produtos</h4>
						<div id="produtos-tbl-pnl" className="tbl-pnl-pequeno">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Descrição</th>
										<th>Codigo de Barras</th>
										<th>Quantidade</th>
										<th>Categorias</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{produtos.map( ( p, index ) => {
										return (
											<tr key={index}>
												<td>{ p.descricao }</td>
												<td>{ p.codigoBarras }</td>
												<td>{ sistema.formataFloat( p.paraAddQuantidade ) } {p.unidade}</td>
												<td>
													<select>
														{p.categoriaMaps.map( (map, index2) => {
															return (
																<option key={produtos.length + index2}>{map.categoria} {map.subcategoria}</option>	
															)
														} ) }
													</select>					
												</td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.removeProduto( e, index )}>remover</button></td>
											</tr>
										)
									} ) }	
								</tbody>							
							</Table>
						</div>							
					
						<div className="my-3">
							<AddCompraProduto produtos={produtos} produtoAdicionado={ (p) => this.produtoAdicionado( p ) } />
						</div>					
					</TabPanel>		
					<TabPanel>
						<SetCompraFornecedor fornecedorEmpresaRef={this.fornecedorEmpresaRef} />
					</TabPanel>
					<TabPanel>
						<GeraCompraParcelas produtos={produtos} parcelas={parcelas} config={geraParcelasConfig} quantParcelasRef={this.quantParcelasRef} />
					</TabPanel>
				</Tabs>
				
				<hr />
												
				<Card className="p-3">
					<MensagemPainel cor="danger" msg={erroMsg} />
					<MensagemPainel cor="primary" msg={infoMsg} />
					
					<Form className="text-center">						
						<Button variant="primary" onClick={ (e) => this.registrarCompra( e ) }>Registrar compra</Button>
					</Form>						
				</Card>		
				
				<br />
				
				<Card className="p-3">					
					<Form>
						<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCompras( e ) }>Ir para tela de compras</button>
					</Form>
				</Card>
				
			</div>
		);
	}
	
}