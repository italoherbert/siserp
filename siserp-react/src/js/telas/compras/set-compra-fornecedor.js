import React from 'react';
import { Card, Form } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import InputDropdown from './../../componente/input-dropdown';
import sistema from './../../logica/sistema';

export default class SetCompraFornecedor extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			fornecedoresLista : []		
		};		
		this.fornecedorEmpresa = React.createRef();								
	}
		
	fornecedorOnChange( item ) {
		this.props.fornecedor.empresa = item;		

		if ( item.length === 0 )
			return;
								
		fetch( '/api/fornecedor/filtra/limit/5', {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+sistema.token
			},
			body : JSON.stringify( {
				"empresaIni" : item
			} )
		} ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				resposta.json().then( (dados) => {
					this.setState( { fornecedoresLista : [] } );
					
					for( let i = 0; i < dados.length; i++ )
						this.state.fornecedoresLista.push( dados[ i ].empresa );					
					
					this.setState( {} );
				} );
			}
		} );
	}
			
	render() {
		const { erroMsg, infoMsg, fornecedoresLista } = this.state;
				
		return(									
			<Form onSubmit={(e) => this.addFornecedor( e ) }>																							
				
				<Card className="p-3 mb-2">
					<h4 className="card-title">Configurar fornecedor</h4>
					
					<Form.Group className="mb-2">
						<Form.Label>Fornecedor: </Form.Label>
						<InputDropdown referencia={this.fornecedorEmpresa} itens={fornecedoresLista} carregaItens={ (item) => this.fornecedorOnChange( item ) } />						
					</Form.Group>
					
				</Card>
									
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />				
			</Form>
				
		);
	}
	
}