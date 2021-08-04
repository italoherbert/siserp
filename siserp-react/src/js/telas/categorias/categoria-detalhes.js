import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import CategoriaForm from './categoria-form';
import SubCategoriaForm from './subcategoria-form';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

export default class CategoriaDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			categoria : { subcategorias : [] } 
		};
		
		this.subcatDescricaoIni = React.createRef();
	}
	
	componentDidMount() {
		this.carregar( null );
		
		this.subcatDescricaoIni.current.value = "*";
	}
	
	carregar( e ) {
		if ( e != null )
			e.preventDefault();
			
		let categoriaId = this.props.categoriaId;
		
		fetch( "/api/categoria/get/"+categoriaId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {		
					this.setState( { categoria : dados } );
					
					if ( dados.subcategorias.length === 0 )
						this.setState( { infoMsg : "Nenhuma subcategoria registrada." } );					
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}			
		} );	
	}
			
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<CategoriaForm op="editar"
				titulo="Altere a categoria" 
				categoriaId={this.props.categoriaId} 
				descricao={this.state.categoria.descricao} 
			/>,
			sistema.paginaElemento() );
	}
	
	filtrarSubcategorias( e ) {
		if ( e != null )
			e.preventDefault();
			
		let categoriaId = this.props.categoriaId;
		
		fetch( "/api/subcategoria/filtra/"+categoriaId, {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"descricaoIni" : this.refs.subcatDescricaoIni.value
			} )
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {					
					this.setState( { categoria : { subcategorias : dados } } );
					
					if ( dados.length === 0 )
						this.setState( { infoMsg : "Nenhuma subcategoria encontrada pelos critérios de busca informados." } );					
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}			
		} );		
		
	}
	
	editarSubcategoria( e, subcat ) {
		e.preventDefault();
		
		ReactDOM.render( 			
			<SubCategoriaForm 
				op="editar" 
				titulo="Altere a subcategoria"
				categoriaId={this.state.categoria.id}
				subcategoriaId={subcat.id} 
				descricao={subcat.descricao} 
			/>, sistema.paginaElemento() ); 
	}
	
	removerSubcategoria( e, subcatId ) {
		e.preventDefault();

		fetch( "/api/subcategoria/deleta/"+subcatId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {	
				this.setState( { infoMsg : "Subcategoria removida com êxito!" } );
				this.filtrarSubcategorias( e );																	
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}
		} );		
	}
		
	render() {
		const { categoria, erroMsg, infoMsg } = this.state;
		
		return( 
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h4 className="text-center">Dados do categoria</h4>																
						
						<Card className="p-3">								
							<h4 className="card-title">Dados gerais</h4>
								
							<div className="inline-block">	
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{categoria.descricao}</span>
							</div>
							
							<br />
							<button className="btn btn-link" onClick={(e) => this.editar( e )}>Editar categoria</button>																																							
						</Card>
					</Col>
				</Row>
												
				<br />
				
				<Row>
					<Col className="card p-3">
						<h4 className="text-center">Lista de Subcategorias</h4>
						<Card className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Descrição</th>
										<th>Editar</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{categoria.subcategorias.map( ( subcategoria, index ) => {
										return (
											<tr key={index}>
												<td>{subcategoria.id}</td>
												<td>{subcategoria.descricao}</td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.editarSubcategoria( e, subcategoria )}>editar</button></td>
												<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.removerSubcategoria( e, subcategoria.id )}>remover</button></td>
											</tr>
										);
									} ) }	
								</tbody>							
							</Table>
						</Card>
						
						<br />
						
						<Row>
							<Col className="col-sm-2"></Col>
							<Col className="col-sm-8">
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />
								
								<Form onSubmit={ (e) => this.filtrarSubcategorias( e ) }>
									<Form.Group className="mb-2">
										<Form.Label>Descrição:</Form.Label>
										<Form.Control type="text" ref={this.subcatDescricaoIni} name="subcatDescricaoIni" />						
									</Form.Group>
																							
									<Button type="submit" variant="primary">Filtrar</Button>				
								</Form>	
							</Col>
						</Row>																
					</Col>															
				</Row>		
				
				<br />
								
				<SubCategoriaForm 
					op="cadastrar" 
					titulo="Registre nova subcategoria"
					categoriaId={this.props.categoriaId} 
					registrou={ () => this.filtrarSubcategorias(null) } />
																																												
			</Container>
		);
	}

}