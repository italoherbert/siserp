import React from 'react';
import { Table, Form, Button } from 'react-bootstrap';

import * as moment from 'moment';

import sistema from './../../../logica/sistema';

export default class GeraVendaParcelas extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = {
			vencimento : 10
		};	
        this.quantParcelas = React.createRef();		
	}		
				
	calcularParcelas( e ) {
		e.preventDefault();
		
		const { parcelas, itensProdutos, valores } = this.props;
				
		parcelas.splice( 0, parcelas.length );
					
		let quantParcelas = this.quantParcelas.current.value;
		let valorParcela = valores.total / quantParcelas;
		for( let i = 1; i <= quantParcelas; i++ ) {		
			let dataPag = moment( new Date() ).add( i, 'M' );
			let dataVenc = moment( dataPag ).add( this.state.vencimento, 'd' );
			
			parcelas.push( { valor : valorParcela, dataPagamento : dataPag, dataVencimento : dataVenc } );
		}
		
		this.setState( {} );
	}
				
	render() {				
		const { parcelas } = this.props;
			
		return(											
			<div className="ip-3">
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
							{parcelas.map( ( ip, index ) => {
								return (									
									<tr key={index}>
										<td>{ sistema.formataReal( ip.valor ) }</td>
										<td>{ sistema.formataData( ip.dataPagamento ) }</td>
										<td>{ sistema.formataData( ip.dataVencimento ) }</td>
									</tr>										
								)
							} ) }	
						</tbody>							
					</Table>
				</div>		
				
				<Form onSubmit={ (e) => this.calcularParcelas( e ) }>					
					<Form.Group className="my-2 col-sm-6">
						<Form.Label>Numero de parcelas</Form.Label>
						<Form.Control type="text" ref={this.quantParcelas} name="quantParcelas" />	
					</Form.Group>
					
					<Button type="submit" variant="primary">Calcular parcelas</Button>						
				</Form>
			</div>
		);
	}
	
}