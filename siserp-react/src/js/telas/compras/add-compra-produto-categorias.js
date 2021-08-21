import React from 'react';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';
import InputDropdown from './../../componente/input-dropdown';

export default class AddCompraProdutoCategorias extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 			
			erroMsg : null,
			categoria : '',
			subcategoria : '',
			categoriasLista : [],
			subcategoriasLista : []
		};										
		this.categoria = React.createRef();
		this.subcategoria = React.createRef();
	}			
			
	addCategoria( item ) {
		item.preventDefault();				

		this.setState( { erroMsg : null } );
		
		let catdesc = this.state.categoria;
		let subcatdesc = this.state.subcategoria;
		
		if ( catdesc.length === 0 ) {
			this.setState( { erroMsg : "A categoria é um campo obrigatório" } );
			return;
		}
		
		if ( subcatdesc.length === 0 ) {
			this.setState( { erroMsg : "A subcategoria (valor) é um campo obrigatório" } );
			return;
		}
					
		let categoria = null;
		for( let i = 0; categoria === null && i < this.props.categorias.length; i++ ) {
			let cat = this.props.categorias[ i ];
			if( cat.descricao.toLowerCase() === catdesc.toLowerCase() ) 
				categoria = cat;
		}
		
		let jaInserida = false;
		if ( categoria === null ) {
			categoria = {
				descricao : catdesc,
				subcategorias : []
			}
			this.props.categorias.push( categoria );
		} else {
			for( let i = 0; jaInserida === false && i < categoria.subcategorias.length; i++ ) {
				let subcat = categoria.subcategorias[ i ];
				if ( subcat.descricao.toLowerCase() === subcatdesc.toLowerCase() )
					jaInserida = true;				
			}
		}
		
		if ( jaInserida === false ) {
			categoria.subcategorias.push( {
				descricao : subcatdesc
			} );
			
			this.categoria.current.value = '';
			this.subcategoria.current.value = '';
			
			this.setState( { categoria : '', subcategoria : '' } );
		} else {
			this.setState( { erroMsg : 'Já existe inserida combinação de categoria e subcategoria informadas.' } );
		}
	}
	
	removeCategoria( item, cat, subcat ) {
		item.preventDefault();
		
		let fim = false;
		let size = this.props.categorias.length;
		for( let i = 0; fim === false && i < size; i++ ) {
			let catObj = this.props.categorias[ i ];
			if ( catObj.descricao.toLowerCase() === cat.toLowerCase() ) {
				let size2 = catObj.subcategorias.length;
				for( let j = 0; fim === false && j < size2; j++ ) {
					let subcatObj = catObj.subcategorias[ j ];
					if ( subcatObj.descricao.toLowerCase() === subcat.toLowerCase() ) {
						catObj.subcategorias.splice( j, 1 );
						fim = true;
					}
					
					if ( catObj.subcategorias.length <= 0 )
						this.props.categorias.splice( i, 1 );
				}
				fim = true;
			}
		}
		this.setState( {} );
	}
			
	categoriaOnChange( item ) {
		this.setState( { categoria : item } );	
		
		sistema.wsPost( '/api/categoria/filtra/limit/5', {
			"descricaoIni" : item
		}, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { categoriasLista : [] } );
				
				for( let i = 0; i < dados.length; i++ )
					this.state.categoriasLista.push( dados[ i ].descricao );					

				this.setState( {} );
			} );
		}, this );				
	}
	
	subcategoriaOnChange( item ) {								
		if ( this.state.categoria.length === 0 ) {
			this.setState( { erroMsg : 'Nenhuma categoria selecionada ou informada.' } );
			return;
		}
		
		this.setState( { subcategoria : item } );
				
		sistema.wsPost(	'/api/subcategoria/filtra/limit/'+this.state.categoria+'/5', {
			"descricaoIni" : item
		}, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { subcategoriasLista : [] } );
				
				for( let i = 0; i < dados.length; i++ )
					this.state.subcategoriasLista.push( dados[ i ].descricao );					
				
				this.setState( {} );
			} );
		}, this );							
	}
		
	render() {
		const { erroMsg, categoriasLista, subcategoriasLista } = this.state;
				
		return(											
			<div>
				<h4 className="text-center">Lista de categorias</h4>
				<div className="tbl-pnl-pequeno">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Catetoria</th>
								<th>Subcategoria</th>										
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{this.props.categorias.map( ( cat, index ) => {
								return (
									cat.subcategorias.map( ( subcat, index2 ) => {														
										return (
											<tr key={index * cat.subcategorias.length + index2}>
												<td>{cat.descricao}</td>
												<td>{subcat.descricao}</td>
												<td><button className="btn btn-link p-0" onClick={(item) => this.removeCategoria( item, cat.descricao, subcat.descricao )}>remover</button></td>
											</tr>
										)
									} )
								)
							} ) }	
						</tbody>							
					</Table>
				</div>		
				
				<Card className="p-3 my-2">
					<MensagemPainel cor="danger" msg={erroMsg} />
					
					<Form.Group className="my-2">
						<Row>
							<Col className="col-sm-4">
								<Form.Label>Categoria</Form.Label>
								<InputDropdown referencia={this.categoria} itens={categoriasLista} carregaItens={ (item) => this.categoriaOnChange( item ) } />																	
							</Col>
							<Col className="col-sm-4">
								<Form.Label>Subcategoria (Valor)</Form.Label>
								<InputDropdown referencia={this.subcategoria} itens={subcategoriasLista} carregaItens={ (item) => this.subcategoriaOnChange( item ) } />						
							</Col>
							<Col className="col-sm-4">
								<Form.Label>+</Form.Label>
								<br />
								<Button variant="primary" onClick={ (item) => this.addCategoria( item ) }>Adicionar categoria</Button>
							</Col>
						</Row>
					</Form.Group>									
				</Card>	
			</div>
		);
	}
	
}