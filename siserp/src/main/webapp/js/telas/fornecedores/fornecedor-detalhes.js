
class FornecedorDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			fornecedor : {} 
		};
	}
	
	componentDidMount() {
		let fornecedorId = this.props.fornecedorId;
		
		fetch( "/api/fornecedor/get/"+fornecedorId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			resposta.json().then( (dados) => {											
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					this.state.fornecedor = dados;		
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;	
				} else {
					this.state.erroMsg = sistema.getMensagemErro( resposta.status );
				}
				this.setState( this.state );				
			} );			 
		} );
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<FornecedorForm op="editar"
				fornecedorId={this.props.fornecedorId} 
				empresa={this.state.fornecedor.empresa} 
			/>,
			sistema.paginaElemento() );
	}
	
	render() {
		const { fornecedor, erroMsg, infoMsg } = this.state;
		
		return( 
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados do fornecedor</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">Empresa: </span>
								<span className="text-info">{fornecedor.empresa}</span>																							
							</div>									
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<a href="#" onClick={(e) => this.editar( e )}>Editar fornecedor</a>
													
								<MensagemPainel tipo="erro" msg={erroMsg} />
								<MensagemPainel tipo="info" msg={infoMsg} />																																								
							</div>
						</div>
												
					</div>
				</div>															
			</div>
		);
	}

}