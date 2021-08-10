import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';
import InputDropdown from './../../componente/input-dropdown';

import Vendas from './vendas';

export default class VendaRegistro extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,

			itens : [],
			formasPag : [],
			
			quantValorPadrao : 1,
			
			clientesNomeLista : [],
			clienteId : -1,
			clienteNome : '',
			incluirCliente : false,
			
			subtotal : 0,
			desconto : 0,
			total : 0,
			troco : 0
		};			
		this.codigoBarras = React.createRef();
		this.desconto = React.createRef();
		this.formaPag = React.createRef();
		this.valorPago = React.createRef();
	}

	componentDidMount() {
		this.carregaFormasPagamento();
	}
	
	carregaFormasPagamento() {
		sistema.showLoadingSpinner();
		
		fetch( '/api/pagamento/formas/', {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { formasPag : dados } );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}

	addItemVenda( e ) {
		let codigoBarras = this.codigoBarras.current.value;
			
		this.setState( { erroMsg : null, infoMsg : null } );

		sistema.showLoadingSpinner();
		
		fetch( '/api/produto/busca/'+codigoBarras, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.state.itens.unshift( {
						codigoBarras : codigoBarras,
						descricao : dados.descricao,
						categorias : dados.categorias,
						precoUnitario : dados.precoUnitVenda, 	
						unidade : dados.unidade,
						estoqueQuantidade : dados.quantidade,
						quantidade : 1
					} );			
					
					this.codigoBarras.current.value = '';
					
					this.calcularTotal( e );		
					
					sistema.scrollTo( 'produtos-tbl-pnl' );
					
					this.setState( {} );
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}		
			
	removerItemVenda( e, index ) {
		e.preventDefault();
				
		this.setState( { erroMsg : null, infoMsg : null } );

		this.state.itens.splice( index, 1 );
		
		this.calcularTotal( e );		
		
		this.setState( {} );
	}
		
	efetuarVenda( e ) {
		const { itens } = this.state;
		
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
												
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
				quantidade : itens[ i ].quantidade
			} );
		}
		
		sistema.showLoadingSpinner();			
							
		fetch( '/api/venda/efetua/'+sistema.usuario.id, {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				subtotal : sistema.paraFloat( this.state.subtotal ),
				desconto : sistema.paraFloat( this.state.desconto ),
				formaPag : this.formaPag.current.value,
				incluirCliente : this.state.incluirCliente,
				clienteNome : this.state.clienteNome,
				itensVenda : itensVenda
			} )
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				this.valorPago.current.value = '';
				this.desconto.current.value = '';
				this.formaPag.current.value = '';
										
				this.setState( { 
					infoMsg : 'Venda registrada com êxito',
					itens : [],
					subtotal : 0,
					total : 0,
					troco : 0
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	quantidadeItemProdOnChange( e, index ) {		
		let itens = this.state.itens;				
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let quant = sistema.paraFloat( e.target.value );
		itens[ index ].quantidade = quant;
		
		if ( e.target.value.trim().length > 0 )	{
			let estoqueQuant = parseFloat( itens[ index ].estoqueQuantidade );

			if ( isNaN( quant ) === false ) {
				if ( quant <= estoqueQuant ) {					
					this.calcularTotal( e );
					this.setState( { itens : itens } );
				} else {					
					let descricao = itens[ index ].descricao;
					this.setState( { erroMsg : "Quantidade informada maior que a quantidade em estoque. Produto="+descricao } );
				}
			} else {
				this.setState( { itens : itens } );
			}
		}				
	}
		
	incluirClienteOnChange( e ) {
		this.setState( { incluirCliente : e.target.checked } );
	}
		
	clienteNomeOnChange( item ) {	
		this.setState( { clienteNome : item } );
	
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
					this.setState( { clientesNomeLista : [] } );
										
					for( let i = 0; i < dados.length; i++ )
						this.state.clientesNomeLista.push( dados[ i ].pessoa.nome );					

					this.setState( {} );
				} );
			}
		} );
	}
	
	calcularTotal( e ) {
		const { itens } = this.state;
				
		let desconto = parseFloat( sistema.paraFloat( this.desconto.current.value ) );
		if ( isNaN( desconto ) === true ) {
			desconto = 0;					
		} else {
			desconto /= 100.0;
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
								
		let total = subtotal * ( 1.0 - desconto );		
		
		let valorPago = parseFloat( sistema.paraFloat( this.valorPago.current.value ) );
		
		let troco = 0;
		if ( !isNaN( valorPago ) )
			troco = sistema.paraFloat( this.valorPago.current.value ) - total;		
		
		this.setState( { desconto : desconto, subtotal : subtotal, total : total, troco : troco } );
	}
			
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}	
		
	render() {
		const { infoMsg, erroMsg, itens, formasPag, quantValorPadrao, subtotal, total, troco, clientesNomeLista, incluirCliente } = this.state;
							
		return(	
			<div>												
				<h4 className="text-center">Lista de Produtos</h4>
				<div id="produtos-tbl-pnl" className="tbl-pnl-pequeno">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Descrição</th>
								<th>Codigo de Barras</th>
								<th>Preço unitário</th>
								<th>Quant. Estoque</th>
								<th>Quantidade</th>
								<th>Unidade</th>
								<th>Categorias</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{itens.map( ( item, index ) => {
								return (
									<tr key={index}>
										<td>{ item.descricao }</td>
										<td>{ item.codigoBarras }</td>
										<td>{ sistema.formataReal( item.precoUnitario ) }</td>
										<td>{ sistema.formataFloat( item.estoqueQuantidade ) }</td>
										<td>
											<Form>
												<Form.Control type="text" onChange={ (e) => this.quantidadeItemProdOnChange( e, index ) } defaultValue={quantValorPadrao} />
											</Form>
										</td>
										<td>{ item.unidade }</td>
										<td>
											<select>
												{ item.categorias.map( (item, index) => {
													return (
														item.subcategorias.map( (subitem, index2) => {
															return (
																<option key={index} value={subitem.descricao}>{subitem.descricao}</option>
															)
														} )
													)
												} )}
											</select>
										</td>			
										<td><button className="btn btn-link p-0" onClick={(e) => this.removerItemVenda( e, index )}>remover</button></td>
									</tr>
								)
							} ) }	
						</tbody>							
					</Table>
				</div>			

				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<div className="p-3 bg-light">
					<Form.Group>
						<div style={{fontSize : '1.6em' }}>
							<Row>
								<Col>
									<Form.Label>Subtotal: &nbsp; <span className="text-danger">{ sistema.formataReal( subtotal ) }</span></Form.Label>
								</Col>
								<Col>
									<Form.Group className="mb-2">
										<Form.Label>Desconto (%): &nbsp;</Form.Label>
										<input className="border-light rounded text-danger" type="text" ref={this.desconto} name="desconto" onChange={ (e) => this.calcularTotal( e ) } />										
									</Form.Group>								
								</Col>	
							</Row>
							<Row>
								<Col>
									<Form.Label>Total: &nbsp; <span className="text-danger">{ sistema.formataReal( total ) }</span></Form.Label>										
								</Col>
								<Col>
									<Form>
										<span>Valor pago (R$): </span> 
										<input className="border-light rounded text-danger" type="text" ref={this.valorPago} name="valorPago" onChange={ (e) => this.calcularTotal( e ) } />									
									</Form>
								</Col>	
							</Row>
							<Row>
								<Col>
									<Form.Label>Troco: &nbsp; <span className="text-danger">{ sistema.formataReal( troco ) }</span></Form.Label>										
								</Col>
							</Row>
						</div>
					</Form.Group>
				</div>

				<br />

				<Form onSubmit={ (e) => this.efetuarVenda( e ) }>
					<Row>
						<Col>
							<Card className="p-3">
								<h4>Incluir produtos</h4>
								<Row>
									<Col className="col-sm-8">
										<Form.Group className="mb-2">
											<Form.Label>Codigo de barras</Form.Label>
											<Form.Control type="text" ref={this.codigoBarras} name="codigoBarras" />
										</Form.Group>
									</Col>
									<Col className="col-sm-4">
										<Form.Group className="mb-2">
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<Button variant="primary" onClick={ (e) => this.addItemVenda( e ) }>Adicionar produto</Button>
										</Form.Group>								
									</Col>
								</Row>					
							</Card>
						</Col>
						<Col>												
							<Card className="p-3">
								<h4>Forma de pagamento</h4>									
								<Row>									
									<Col>
										<Form.Group className="mb-2">
											<Form.Label>Formas de pagamento: &nbsp;</Form.Label>
											<select name="formaPag" ref={this.formaPag} className="form-control">
												<option key="0" value="NONE">Selecione uma forma!</option>
												{ formasPag.map( (item, i) => {
													return <option key={i} value={item}>{item}</option>
												} )	}
											</select>
										</Form.Group>
									</Col>									
								</Row>
							</Card>
						</Col>												
					</Row>
					
					<br />
					
					<Row>
						<Col className="col-sm-6">
							<Card className="p-3">
								<Form.Group className="mb-2">
									<input type="checkbox" defaultValue={incluirCliente} onChange={ (e) => this.incluirClienteOnChange( e ) } /> 
										&nbsp; Incluir cliente
								</Form.Group>
														
								{ incluirCliente === true && (
									<Form.Group>
										<Form.Label>
											<h5>Informe um cliente</h5>
										</Form.Label>
										<InputDropdown 
											referencia={this.clienteNome} 
											itens={clientesNomeLista} 
											carregaItens={ (item) => this.clienteNomeOnChange( item ) } />		
									</Form.Group>
								) }
							</Card>
						</Col>
					</Row>
				</Form>
											
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
						<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaVendas( e ) }>Ir para tela de compras</button>
					</Form>
				</Card>				
			</div>
		);
	}
	
}