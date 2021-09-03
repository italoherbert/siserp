import React from 'react';
import { Row, Col, Card, Form } from 'react-bootstrap';

import sistema from './../../../logica/sistema';
import MensagemPainel from './../../../componente/mensagem-painel';
import InputDropdown from './../../../componente/input-dropdown';

export default class ConfigVendaPagamento extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			infoMsg : null,
			erroMsg : null,

            itens : [],
			formasPag : [],			
									
			clientesNomeLista : [],					
		};		
        this.incluirCliente = React.createRef();	
	}

	componentDidMount() {
		this.carregaFormasPagamento();
        this.incluirCliente.current.checked = this.props.cliente.incluir;
	}
	
	carregaFormasPagamento() {
		sistema.wsGet( '/api/pagamento/formas/', (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { formasPag : dados } );
			} );
		}, this );
	}		
	
	descontoOnChange( e ) {
		this.props.valores.desconto = e.target.value;
        if ( typeof( this.props.calcularTotal ) === 'function' )
            this.props.calcularTotal.call( this, e );
	}

	valorPagoOnChange( e ) {
		this.props.valores.valorPago = e.target.value;
        if ( typeof( this.props.calcularTotal ) === 'function' )
            this.props.calcularTotal.call( this, e );
	}
		
	formaPagOnChange( e ) {
		if ( e.target.value === 'DEBITO' ) {
            this.props.cliente.incluir = true;			
            this.incluirCliente.current.checked = true;

            this.setState( {} );
		}
	}
		
	incluirClienteOnChange( e ) {        
		this.props.cliente.incluir = e.target.checked;
        this.setState( {} );
	}
		
	clienteNomeOnChange( item ) {	
		this.setState( { clienteNome : item } );
		
		sistema.wsPost( '/api/cliente/filtra/limit/5', {
			"nomeIni" : item
		}, (resposta) => {
			resposta.json().then( (dados) => {
				this.setState( { clientesNomeLista : [] } );
														
				for( let i = 0; i < dados.length; i++ )
					this.state.clientesNomeLista.push( dados[ i ].pessoa.nome );					

				this.setState( {} );
			} );
		}, this );
	}
					
	render() {
		const { infoMsg, erroMsg, formasPag, clientesNomeLista, incluirCliente } = this.state;
        const { valores, cliente, formaPagReferencia, clienteNomeReferencia } = this.props;
							
		return(	
			<div>	
				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />
																								
                <div className="bg-light">
                    <Form.Group>
                        <div style={{fontSize : '1.6em' }}>
                            <Card className="p-3">
                                <Row>
                                    <Col>
                                        <Form.Label>Subtotal: &nbsp; <span className="text-danger">{ sistema.formataReal( valores.subtotal ) }</span></Form.Label>
                                    </Col>
                                    <Col>
                                        <Form.Label>Desconto (%): &nbsp;</Form.Label>
                                        <Form>
                                            <Form.Control className="text-danger" type="text" onChange={ (e) => this.descontoOnChange( e ) } name="desconto" />										
                                        </Form>
                                    </Col>	
                                    <Col>
                                        <Form>
                                            <Form.Label>Valor pago (R$): </Form.Label> 
                                            <Form.Control className="text-danger" type="text" name="valorPago" onChange={ (e) => this.valorPagoOnChange( e ) } />									
                                        </Form>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col></Col>
                                    <Col>
                                        <Form.Label>Total: &nbsp; <span className="text-danger">{ sistema.formataReal( valores.total ) }</span></Form.Label>										
                                    </Col>								
                                    <Col>
                                        <Form.Label>Troco: &nbsp; <span className="text-danger">{ sistema.formataReal( valores.troco ) }</span></Form.Label>										
                                    </Col>
                                </Row>
                            </Card>							
                        </div>
                    </Form.Group>
                </div>
                
                <br />

                <Form>
                    <Row>						
                        <Col>												
                            <Card className="p-3">
                                <h4>Forma de pagamento</h4>									
                                <Row>									
                                    <Col>
                                        <Form.Group className="mb-2">
                                            <Form.Label>Formas de pagamento: &nbsp;</Form.Label>
                                            <select name="formaPag" ref={formaPagReferencia} onChange={(e) => this.formaPagOnChange( e )} className="form-control">
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
                        <Col>
                            <Card className="p-3">
                                <Form.Group className="mb-2">
                                    <input type="checkbox" ref={this.incluirCliente} defaultValue={cliente.incluir} onChange={ (e) => this.incluirClienteOnChange( e ) } /> 
                                        &nbsp; Incluir cliente
                                </Form.Group>

                                { cliente.incluir === true && (
                                    <Form.Group>
                                        <Form.Label>
                                            <h5>Informe um cliente</h5>
                                        </Form.Label>
                                        <InputDropdown 
                                            referencia={clienteNomeReferencia} 
                                            itens={clientesNomeLista} 
                                            carregaItens={ (item) => this.clienteNomeOnChange( item ) } />		
                                    </Form.Group>
                                ) }
                            </Card>
                        </Col>
                    </Row>
                </Form>											
			</div>
		);
	}
	
}