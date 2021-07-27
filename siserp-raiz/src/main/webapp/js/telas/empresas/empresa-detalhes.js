
class FuncionarioDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			empresa : {} 
		};
	}
	
	componentDidMount() {
		let empresaId = this.props.empresaId;
		
		fetch( "/api/empresa/get/"+empresaId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			resposta.json().then( (dados) => {											
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					this.state.empresa = dados;		
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;	
				} else {
					this.state.erroMsg = "Erro desconhecido.";
				}
				this.setState( this.state );				
			} );			 
		} );
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<FuncionarioForm op="editar"
				empresaId={this.props.empresaId}  
				razaoSocial={this.state.empresa.razaoSocial}
			/>,
			document.getElementById( "pagina" ) );
	}
	
	paraSedesPagina( e, empresaId ) {
		e.preventDefault();
		
		ReactDOM.render( <Sedes empresaId={empresaId} />, sistema.paginaElemento() );
	}
	
	render() {
		const { empresa, erroMsg, infoMsg } = this.state;
		
		return( 
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados da empresa</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">Razao Social: </span>
								<span className="text-info">{empresa.razaoSocial}</span>
							</div>
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<a href="#" onClick={(e) => this.editar( e )}>Editar funcionario</a>
													
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