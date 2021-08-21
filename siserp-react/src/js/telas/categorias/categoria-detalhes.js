import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import CategoriaForm from './categoria-form';
import SubCategoriaForm from './subcategoria-form';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import Categorias from './categorias';

export default class CategoriaDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			categoria : { subcategorias : [] },
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {} 
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
							
		sistema.wsGet( "/api/categoria/get/"+this.props.categoriaId, (resposta) => {
			resposta.json().then( (dados) => {		
				this.setState( { categoria : dados } );									
			} );
		}, this );						
	}
			
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<CategoriaForm op="editar"
				titulo="Altere a categoria" 
				categoriaId={this.props.categoriaId} 
			/>,
			sistema.paginaElemento() );
	}
	
	filtrarSubcategorias( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();
			
		sistema.wsPost(	"/api/subcategoria/filtra/"+this.props.categoriaId, {
			"descricaoIni" : this.subcatDescricaoIni.current.value		
		}, (resposta) => {
			resposta.json().then( (dados) => {		
				let cat = this.state.categoria;
				cat.subcategorias = dados;
			
				this.setState( { categoria : cat } );
				
				if ( dados.length === 0 && filtrarBTClicado === true )
					this.setState( { infoMsg : "Nenhuma subcategoria encontrada pelos critérios de busca informados." } );					
			} );
		}, this );					
	}
	
	editarSubcategoria( e, subcat ) {
		e.preventDefault();
		
		ReactDOM.render( 			
			<SubCategoriaForm 
				op="editar" 
				titulo="Altere a subcategoria"
				categoriaId={this.state.categoria.id}
				subcategoriaId={subcat.id} 
			/>, sistema.paginaElemento() ); 
	}
	
	removerSubcategoriaSeConfirmado( e, subcatId ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.removerSubcategoria( e, subcatId );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	removerSubcategoria( e, subcatId ) {
		e.preventDefault();

		sistema.showLoadingSpinner();

		sistema.wsDelete( "/api/subcategoria/deleta/"+subcatId, (resposta) => {
			this.setState( { infoMsg : "Subcategoria removida com êxito!" } );
			this.filtrarSubcategorias( e, null );
		}, this );	
	}
	
	paraTelaCategorias() {
		ReactDOM.render( <Categorias />, sistema.paginaElemento() );
	}
		
	render() {
		const { categoria, erroMsg, infoMsg, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
		
		return( 
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de subcategoria</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover a subcategoria selecionada?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>

				<h4 className="text-center">Dados do categoria</h4>																
					
				<br />
				
				<Row>
					<Col className="col-md-8">
						
						<Card className="p-3">								
							<h4 className="card-title">Dados gerais</h4>
								
							<div className="inline-block">	
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{categoria.descricao}</span>
							</div>							
							
							<br />
							<Form>
								<Button variant="primary" onClick={(e) => this.editar( e )}>Editar categoria</Button>																																																						
							</Form>
						</Card>
					</Col>
				</Row>
												
				<br />
				
				<h4 className="text-center">Lista de Subcategorias</h4>
				<div className="tbl-pnl">
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
										<td><button className="btn btn-link p-0" onClick={(e) => this.editarSubcategoria( e, subcategoria )}>editar</button></td>
										<td><button className="btn btn-link p-0" onClick={(e) => this.removerSubcategoriaSeConfirmado( e, subcategoria.id )}>remover</button></td>
									</tr>
								);
							} ) }	
						</tbody>							
					</Table>
				</div>
						
				<br />
				
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col>
						<Card className="p-3">		
							<h4>Filtro de categorias</h4>
							<Form onSubmit={ (e) => this.filtrarSubcategorias( e, true ) }>
								<Form.Group className="mb-2">
									<Form.Label>Descrição:</Form.Label>
									<Form.Control type="text" ref={this.subcatDescricaoIni} name="subcatDescricaoIni" />						
								</Form.Group>
																						
								<Button type="submit" variant="primary">Filtrar</Button>				
							</Form>	
						</Card>
					</Col>
					<Col>
						<SubCategoriaForm 
							op="cadastrar" 
							titulo="Registre nova subcategoria"
							categoriaId={this.props.categoriaId} 
							registrou={ () => this.filtrarSubcategorias( null, false ) } />				
					</Col>
				</Row>																
												
				<br />
				
				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-link p-0" onClick={ (e) => this.paraTelaCategorias( e ) }>Ir para categorias</button>
						</Col>
					</Row>
				</Card>
			</Container>
		);
	}

}