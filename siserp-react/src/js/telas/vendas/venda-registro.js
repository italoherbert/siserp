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
			
			clientesFiltrados : [],
			clienteId : -1,
			clienteNome : '',
			incluirCliente : false,
			
			subtotal : 0,
			desconto : 0,
			total : 0			
		};			
		this.codigoBarras = React.createRef();
		this.desconto = React.createRef();
		this.formaPag = React.createRef();
	}

	componentDidMount() {
		this.desconto.current.value = 0;
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
			
		sistema.showLoadingSpinner();
		
		fetch( '/api/produto/busca/'+codigoBarras, {
			method : 'GET',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.state.itens.push( {
						codigoBarras : codigoBarras,
						valorUnitario : dados.valorUnitVenda, 
						quantidade : 1
					} );			
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}		
			
	removerItemVenda( e, index ) {
		e.preventDefault();
		
		this.state.itens.splice( index, 1 );
		this.setState( {} );
	}
		
	efetuarVenda( e ) {
		const { itens } = this.state;
		
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
								
		sistema.showLoadingSpinner();				
								
		fetch( '/api/venda/efetua/'+sistema.usuario.id, {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				dataVenda : sistema.formataData( new Date() ),
				subtotal : sistema.formataFloat( this.state.subtotal ),
				desconto : sistema.formataFloat( this.state.desconto ),
				formaPag : this.formaPag.current.value,
				incluirCliente : this.state.incluirCliente,
				clienteId : this.state.clienteId,
				itensVenda : itens
			} )
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				this.desconto.current.value = '';
				this.modoPag.current.value = '';
				this.setState( { 
					infoMsg : 'Venda registrada com êxito',
					itens : []
				} );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
		
	quantidadeItemProdOnChange( e, index ) {
		let itens = this.state.itens;
		
		itens[ index ].quantidade= e.target.value;
		
		this.setState( { itens : itens } );
	}
	
	incluirClienteOnChange( e ) {
		this.setState( { incluirCliente : e.target.checked } );
	}
	
	clienteNomeClicado( item, index ) {
		let cliente = this.state.clientesFiltrados[ index ];
		this.setState( { clienteNome : item, clienteId : cliente.id } );
	}
	
	clienteNomeOnChange( item ) {		
		if ( item.length === 0 )
			return;
								
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
					this.setState( { clientesFiltrados : [] } );
					
					for( let i = 0; i < dados.length; i++ )
						this.state.clientesFiltrados.unshift( dados[ i ].descricao );					

					this.setState( {} );
				} );
			}
		} );
	}
	
	calcularTotal( e ) {
		const { itens } = this.state;
		let subtotal = 0;
		for( let i = 0; i < itens.length; i++ )
			subtotal += parseFloat( itens[ i ].valorUnitario );				
		
		let desconto = "";
		let total = 0;		
		let erroMsg = null;
		
		if ( this.desconto.current.value.length > 0 ) {
			desconto = sistema.paraFloat( this.desconto.current.value );
			total = subtotal * ( 1.0 - ( parseFloat( desconto ) / 100.0 ) );
		} else {
			erroMsg = 'Desconto não numérico. Informe um valor inteiro ou real como desconto';
		}
		
		this.setState( { desconto : desconto, subtotal : subtotal, total : total, erroMsg : erroMsg } );
	}
			
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() ); 
	}	
		
	render() {
		const { infoMsg, erroMsg, itens, formasPag, subtotal, desconto, total, clientesFiltrados, incluirCliente } = this.state;
							
		return(	
			<div>
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
								
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
							{itens.map( ( item, index ) => {
								return (
									<tr key={index}>
										<td>{ item.codigoBarras }</td>
										<td>
											<Form>
												<Form.Control type="text" onChange={ (e) => this.quantidadeItemProdOnChange( e, index ) } defaultValue={item.quantidade} />
											</Form>
										</td>										
										<td><button className="btn btn-link p-0" onClick={(e) => this.removerItemVenda( e, index )}>remover</button></td>
									</tr>
								)
							} ) }	
						</tbody>							
					</Table>
				</div>			

				<br />

				<Card className="p-3">
					<h4>Incluir produtos</h4>
					<Form onSubmit={ (e) => this.registrarVenda() }>
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
					</Form>
				</Card>
				
				<br />
											
				<Form onSubmit={ (e) => this.efetuarVenda( e ) }>
					<Card className="p-3">
						<h4>Dados da venda</h4>
							
						<Row>
							<Col>
								<Form.Group className="mb-2">
									<Form.Label>Desconto (%):</Form.Label>
									<Form.Control type="number" ref={this.desconto} name="desconto" />										
								</Form.Group>
							</Col>
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
							
						<br />

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
										itens={clientesFiltrados} 
										carregaItens={ (item) => this.clienteNomeOnChange( item ) } 
										itemClicado={ (item, index) => this.clienteNomeClicado( item, index ) } />		
								</Form.Group>
							) }
						</Card>
						
						<br />
						
						<Card className="p-3">
							<Form.Group>
								<div style={{fontSize : '1.6em' }}>
									<Row>
										<Col className="col-sm-6">
											<Form.Label>Subtotal: &nbsp; <span className="text-danger">{ sistema.formataReal( subtotal ) }</span></Form.Label>
										</Col>
										<Col className="col-sm-6">
											<Form.Label>Desconto: &nbsp; <span className="text-danger">{ sistema.formataFloat( desconto ) }</span></Form.Label>									
										</Col>	
									</Row>
									<hr />
									<Form.Label>Total: &nbsp; <span className="text-danger">{ sistema.formataReal( total ) }</span></Form.Label>										
								</div>
								<Button variant="primary" onClick={ (e) => this.calcularTotal( e ) }>Calcular total</Button>
							</Form.Group>
						</Card>
					</Card>		
				
					<br />
					<Card className="p-3 text-center">	
						<Row>
							<Col>
								<Button variant="primary" onClick={ (e) => this.efetuarVenda( e ) }>Efetuar venda</Button>
							</Col>
						</Row>
					</Card>
				</Form>
				
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