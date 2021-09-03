import React from 'react';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from './../../../logica/sistema';
import MensagemPainel from './../../../componente/mensagem-painel';

export default class AddVendaProduto extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,

			itens : [],			
		};			
		this.codigoBarras = React.createRef();
	}
	
	addItemVenda( e ) {
		let codigoBarras = this.codigoBarras.current.value;
			
		sistema.wsGet( '/api/produto/busca/'+codigoBarras, (resposta) => {
			resposta.json().then( (dados) => {
				this.props.itens.unshift( {
					codigoBarras : codigoBarras,
					descricao : dados.descricao,
					categorias : dados.categorias,
					precoUnitario : dados.precoUnitVenda, 	
					unidade : dados.unidade,
					estoqueQuantidade : dados.quantidade,
					categoriaMaps : dados.categoriaMaps,
					quantidade : 1
				} );			
				
				this.codigoBarras.current.value = '';
				
                if ( typeof( this.props.calcularTotal ) === 'function' )
                    this.props.calcularTotal.call( this, e );		
								
				this.setState( {} );
			} );
		}, this );
	}		
			
	removerItemVenda( e, index ) {
		e.preventDefault();
				
		this.setState( { erroMsg : null, infoMsg : null } );

		this.props.itens.splice( index, 1 );
		
        if ( typeof( this.props.calcularTotal ) === 'function' )
            this.props.calcularTotal.call( this, e );		
		
		this.setState( {} );
	}		
			
	quantidadeItemProdOnChange( e, index ) {		
		let itens = this.props.itens;				
		
		this.setState( { erroMsg : null, infoMsg : null } );				
		
		if ( e.target.value.trim().length > 0 )	{
			let estoqueQuant = parseFloat( itens[ index ].estoqueQuantidade );

			let quant = sistema.paraFloat( e.target.value );
			if ( isNaN( quant ) === false ) {
				if ( quant <= estoqueQuant ) {		
					itens[ index ].quantidade = quant;
					
                    if ( typeof( this.props.calcularTotal ) === 'function' )
					    this.props.calcularTotal.call( this, e );
				} else {					
					let descricao = itens[ index ].descricao;
					this.setState( { erroMsg : "Quantidade informada maior que a quantidade em estoque. Produto="+descricao } );
				}
			} else {
				this.setState( { itens : itens } );
			}
		}				
	}
			
	render() {
		const { infoMsg, erroMsg } = this.state;
        const { itens } = this.props;
							
		return(	
			<div>	
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
											
                <h4 className="text-center">Lista de Produtos</h4>

                <div className="tbl-pnl-pequeno">
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
                                                <Form.Control type="text" onChange={ (e) => this.quantidadeItemProdOnChange( e, index ) } value={item.quantidade} />
                                            </Form>
                                        </td>
                                        <td>{ item.unidade }</td>
                                        <td>
                                            <select>
                                                { item.categoriaMaps.map( (maps, index2) => {
                                                    return (
                                                        <option key={index2} value={maps.subcategoria}>{maps.subcategoria}</option>								
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
                
                <Card className="p-3 col-sm-6">
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
			</div>
		);
	}
	
}