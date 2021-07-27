
class FuncionarioForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null
		};
	}
	
	componentDidMount() {			
		if ( this.props.op == 'editar' )
			this.refs.razaoSocial.value = this.props.nome;										
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );
		
		let url;
		let metodo;
		if ( this.props.op == 'editar' ) {			
			url = "/api/empresa/atualiza/"+this.props.funcId;
			metodo = 'PUT';									
		} else {
			url = "/api/empresa/registra";
			metodo = 'POST';
		}
				
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"razaoSocial" : this.refs.razaoSocial.value,					
			} )		
		} ).then( (resposta) => {				
			resposta.json().then( (dados) => {
				if ( resposta.status == 200 ) {
					this.state.infoMsg = "Empresa cadastrada com sucesso.";		
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;
				} else {
					this.state.erroMsg = "Erro desconhecido.";
				}
				this.setState( this.state );
			} );			 
		} );				
	}
	
	render() {
		const { erroMsg, infoMsg } = this.state;
				
		return(
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="card-title text-center">Registro de empresa</h4>
																
						<form onSubmit={(e) => this.salvar( e ) } className="container">
							<div className="card border-1">								
								<div className="card-body">
									<h4 className="card-title">Dados gerais</h4>
									
									<div className="row form-group">
										<div className="col-sm-12">
											<label className="control-label" for="razaoSocial">Razão social: </label>
											<input type="text" ref="razaoSocial" name="razaoSocial" size="50" className="form-control" />
										</div>
									</div>									
								</div>
							</div>
							
							<br />
														
							<div className="card border-1">																
								<div className="card-body">
									<MensagemPainel tipo="erro" msg={erroMsg} />
									<MensagemPainel tipo="info" msg={infoMsg} />
									<div class="form-group">
										<button type="submit" className="btn btn-primary">Salvar</button>
									</div>																												
								</div>
							</div>									
						</form>
					</div>
				</div>
			</div>
		);
	}
	
}