import React from 'react';
import { Card, Table } from 'react-bootstrap';
import { Tab, Tabs, TabPanel, TabList } from 'react-tabs';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import AddCompraProduto from './add-compra-produto';
import SetCompraFornecedor from './set-compra-fornecedor';
import GeraCompraParcelas from './gera-compra-parcelas';

export default class CompraRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,
			produtos : [],
			parcelas : [],
			fornecedor : { 
				empresa : null
			}
		};								
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
		const { fornecedor, produtos, parcelas } = this.state;
		
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let itensCompra = [];
		for( let i = 0; i < produtos.length; i++ ) {
			let p = produtos[ i ];
			itensCompra.push( {				
				precoUnitario : p.precoUnitCompra,
				quantidade : p.quantidade,
				produto : {
					descricao : p.descricao,
					codigoBarras : p.codigoBarras,
					precoUnitCompra : p.precoUnitCompra,
					precoUnitVenda : p.precoUnitVenda,
					unidade : p.unidade,
					
					categorias : p.categorias,
					quantidade : p.quantidade
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
				
		fetch( '/api/compra/registra', {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				dataCompra : sistema.formataData( new Date() ),
				fornecedor : {
					empresa : fornecedor.empresa
				},
				itensCompra : itensCompra,
				parcelas : parcelasList
			} )
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : 'Compra registrada com êxito' } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );
	}
		
	render() {
		const { infoMsg, erroMsg, produtos, parcelas, fornecedor } = this.state;
				
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
										<th>ID</th>
										<th>Descrição</th>
										<th>Codigo de Barras</th>
										<th>Categorias</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{produtos.map( ( p, index ) => {
										return (
											<tr key={index}>
												<td>{p.id}</td>
												<td>{p.descricao}</td>
												<td>{p.codigoBarras}</td>
												<td>
													<select>
														{p.categorias.map( (cat, index) => {
															return (
																cat.subcategorias.map( ( subcat, index ) => {
																	return (
																		<option>{subcat.descricao}</option>
																	)
																} )
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
						<SetCompraFornecedor fornecedor={fornecedor} />
					</TabPanel>
					<TabPanel>
						<GeraCompraParcelas produtos={produtos} parcelas={parcelas} />
					</TabPanel>
				</Tabs>
				
				<br />
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Card className="text-right p-3">
					<button type="button" variant="primary" className="btn btn-primary" onClick={ (e) => this.registrarCompra( e ) }>
						Registrar compra
					</button>																																										
				</Card>
			</div>
		);
	}
	
}