import React from 'react';
import { Table, Form, Button } from 'react-bootstrap';

import * as moment from 'moment';

import sistema from './../../logica/sistema';

export default class GeraCompraParcelas extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = {
			valorTotal : 0,
			vencimento : 10
		};
			
		this.quantParcelas = React.createRef();
	}		
		
	calcularParcelas( e ) {
		e.preventDefault();
		
		const { parcelas, produtos } = this.props;
				
		parcelas.splice( 0, parcelas.length );
			
		let total = 0;
		for( let i = 0; i < produtos.length; i++ ) {
			let p = produtos[ i ];
			total += parseFloat( p.precoUnitCompra ) * parseFloat( p.quantidade );
		}
		
		let quantParcelas = this.quantParcelas.current.value;
		let valorParcela = total / quantParcelas;
		for( let i = 1; i <= quantParcelas; i++ ) {		
			let dataPag = moment( new Date() ).add( i, 'M' );
			let dataVenc = moment( dataPag ).add( this.state.vencimento, 'd' );
			
			parcelas.push( { valor : valorParcela, dataPagamento : dataPag, dataVencimento : dataVenc } );
		}
		
		this.setState( { valorTotal : total } );
	}
				
	render() {				
		const { parcelas } = this.props;
		const { valorTotal } = this.state;
			
		return(											
			<div className="p-3">
				<h4 className="text-center">Lista de Parcelas</h4>
				<div className="tbl-pnl-pequeno">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Valor</th>
								<th>Data pagamento</th>										
								<th>Data vencimento</th>
							</tr>
						</thead>
						<tbody>
							{parcelas.map( ( p, index ) => {
								return (									
									<tr key={index}>
										<td>{ p.valor.toLocaleString( 'pt-br', { style : 'currency', currency : 'BRL' } ) }</td>
										<td>{ sistema.formataData( p.dataPagamento ) }</td>
										<td>{ sistema.formataData( p.dataVencimento ) }</td>
									</tr>										
								)
							} ) }	
						</tbody>							
					</Table>
				</div>		
				
				<Form onSubmit={ (e) => this.calcularParcelas( e ) }>
					<Form.Group className="my-2">		
						<Form.Label>Valor total: &nbsp;
							<span className="text-primary">
								{valorTotal.toLocaleString( 'pt-br', { style : 'currency', currency : 'BRL' } )}
							</span>
						</Form.Label>							
					</Form.Group>
					<Form.Group className="my-2">
						<Form.Label>Numero de parcelas</Form.Label>
						<Form.Control type="number" ref={this.quantParcelas} name="quantParcelas" />	
					</Form.Group>
					
					<Button type="submit" variant="primary">Calcular parcelas</Button>						
				</Form>
			</div>
		);
	}
	
}