import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import DatePicker from 'react-datepicker';
	
import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CompraRegistro from './compra-registro';
import CompraDetalhes from './compra-detalhes';

export default class Compras extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			compras : [],
			dataIni : new Date(),
			dataFim : new Date(),
			
			remocaoModalVisivel : false,
			remocaoModalOkFunc : () => {},
			remocaoModalCancelaFunc : () => {}
		};					
	}
	
	componentDidMount() {
		this.filtrar( null, false );
	}
			
	filtrar( e, filtrarBTClicado ) {
		if ( e != null )
			e.preventDefault();
					
		this.setState( { infoMsg : null, erroMsg : null } );
		
		sistema.showLoadingSpinner();

		fetch( "/api/compra/filtra", {
			method : "POST",			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8", 
				"Authorization" : "Bearer "+sistema.token
			}, 
			body : JSON.stringify( {
				"dataIni" : sistema.formataData( this.state.dataIni ),
				"dataFim" : sistema.formataData( this.state.dataFim )
			} )
		} ).then( (resposta) => {	
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {
					this.setState( { compras : dados } );	
					
					if ( dados.length === 0 && filtrarBTClicado === true )
						this.setState( { infoMsg : "Nenhuma compra encontrada pelos critérios de busca informados!" } );																							
				} );																		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}		
			sistema.hideLoadingSpinner();			
		} );
	}
	
	changeDataIni( date ) {
		this.setState( { dataIni : date } );		
	}
	
	changeDataFim( date ) {
		this.setState( { dataFim : date } );
	}
	
	detalhes( e, compraId ) {
		ReactDOM.render( <CompraDetalhes compraId={compraId} />, sistema.paginaElemento() );
	}
	
	removerSeConfirmado( e, compraId ) {
		this.setState( { 
			remocaoModalVisivel : true, 
			remocaoModalOkFunc : () => { 
				this.setState( { remocaoModalVisivel : false } );
				this.remover( e, compraId );
			},
			remocaoModalCancelaFunc : () => {
				this.setState( { remocaoModalVisivel : false } );
			} 
		} )
	}
	
	remover( e, compraId, instance ) {
		e.preventDefault();

		sistema.showLoadingSpinner();
		
		fetch( '/api/compra/deleta/'+compraId, {
			method : 'DELETE',
			headers : {
				'Authorization' : 'Bearer '+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {
				this.filtrar( null, false );
				this.setState( { infoMsg : 'Compra deletada com êxito.' } );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
			sistema.hideLoadingSpinner();
		} );
	}
	
	paraRegistroForm( e ) {
		e.preventDefault();
		
		ReactDOM.render( <CompraRegistro />, sistema.paginaElemento() );
	}
	
	render() {
		const {	erroMsg, infoMsg, compras, dataIni, dataFim, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;
				
		return (
			<Container>	
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de compra</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						Tem certeza que deseja remover a compra selecionada? <br />
						<br />
						<b>Atenção:</b> Ao escolher remover a compra, esteja ciente que será
						atualizado o estoque com a subtração das unidades de produto componentes
						da compra!						
					</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc() }>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc() }>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>
				
				<Row>
					<Col>
						<Form className="float-end">
							<Button variant="primary"  onClick={ (e) => this.paraRegistroForm(e)}>Registre uma nova compra</Button>
						</Form>
					</Col>
				</Row>
				
				<Row>
					<Col>
						<h4 className="text-center">Lista de Compras</h4>
						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Data compra</th>
										<th>Valor total</th>	
										<th>Detalhes</th>
										<th>Remover</th>
									</tr>
								</thead>
								<tbody>
									{compras.map( ( compra, index ) => {
										return (
											<tr key={index}>
												<td>{compra.id}</td>
												<td>{compra.dataCompra}</td>
												<td>{ sistema.formataReal( compra.debitoTotal ) }</td>														
												<td><button className="btn btn-link p-0" onClick={(e) => this.detalhes( e, compra.id )}>detalhes</button></td>
												<td><button className="btn btn-link p-0" onClick={(e) => this.removerSeConfirmado( e, compra.id )}>remover</button></td>
											</tr>
										)
									} ) }	
								</tbody>							
							</Table>
						</div>
					</Col>
				</Row>
				<br />
		
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
				
				<Row>
					<Col>
						<Card className="p-3">
							<h4>Filtrar compras</h4>
							<Form onSubmit={ (e) => this.filtrar( e, true ) }>
								<Row>
									<Col className="col-sm-4">										
										<Form.Group className="pb-2">													
											<Form.Label>Data de início: </Form.Label>
											<br />
											<DatePicker selected={dataIni} 
													onChange={ (date) => this.changeDataIni( date ) } 
													locale="pt" 
													name="dataIni" 
													dateFormat="dd/MM/yyyy" className="form-control" />						
										</Form.Group>									
									</Col>
									<Col className="col-sm-4">										
										<Form.Group className="pb-2">													
											<Form.Label>Data de fim: </Form.Label>
											<br />
											<DatePicker selected={dataFim} 
													onChange={ (date) => this.changeDataFim( date ) } 
													locale="pt" 
													name="dataFim" 
													minDate={dataIni}
													dateFormat="dd/MM/yyyy" className="form-control" />						
										</Form.Group>									
									</Col>							
									<Col className="col-md-4">
										<div className="mb-2">&nbsp;</div>
										<Button type="submit" variant="primary">Filtrar</Button>				
										<br />
									</Col>
								</Row>								
							</Form>						
						</Card>						
					</Col>
				</Row>																		
			</Container>	
		)
	}
	
}