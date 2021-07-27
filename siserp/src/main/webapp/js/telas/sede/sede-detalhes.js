
class SedeDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			sede : {} 
		};
	}
	
	componentDidMount() {		
		fetch( "/api/sede/get", {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			resposta.json().then( (dados) => {											
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					this.state.sede = dados;		
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
			<SedeForm op="editar" 
				cnpj={this.state.sede.cnpj} 
				inscricaoEstadual={this.state.sede.inscricaoEstadual}
			/>,
			sistema.paginaElemento() );
	}
		
	render() {
		const { sede, erroMsg, infoMsg } = this.state;
		
		return( 
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados da sede</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">CNPJ: </span>
								<span className="text-info">{sede.cnpj}</span>
								<br />
								<span className="text-dark font-weight-bold">Inscrição Estadual: </span>
								<span className="text-info">{sede.inscricaoEstadual}</span>
							</div>
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<a href="#" onClick={(e) => this.editar( e )}>Editar sede</a>
													
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