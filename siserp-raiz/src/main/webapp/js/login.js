
class Login extends React.Component {

	constructor(props) {
		super(props);
		this.state = { erroMsg : null };
		this.entrar = this.entrar.bind(this);
	}
	
	entrar(e) {		
		e.preventDefault();	
			
		this.state.erroMsg = null;
		this.setState( this.state );

		fetch( "/api/login/entrar", {
			method : "POST",			
			credentials : "include",
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
			},				
			body : JSON.stringify( { 
				username : this.refs.username.value,
				password : this.refs.password.value
			} )
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					sistema.token = dados.token;
					sistema.usuario = dados.usuario;
										
					ReactDOM.render( <Layout />, sistema.rootElemento() );				
					ReactDOM.render( <Navbar />, sistema.menuNavegElemento() );				
					ReactDOM.render( <Inicial />, sistema.paginaElemento() );				
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;	
				}
				this.setState( this.state )
			} );			 
		} );
	}
	
	
	render() {
		const { erroMsg } = this.state;
								
		return (
			<div>				
				<h1 className="text-center bg-dark text-white p-4">Sistema ERP</h1>						
				<div className="container">
					<div className="row my-5">
						<div className="col-md-4"></div>
						<div className="card col-md-4">
							<div className="card-body">
								<h4 className="card-title">Tela de login</h4>
														
								<form onSubmit={this.entrar}>
									<div className="form-group">
										<label className="control-label" for="username">Nome de usuário</label>
										<input type="text" ref="username" name="username" className="form-control" />						
									</div>
									<div className="form-group">
										<label className="control-label" for="password">password</label>
										<input type="password" ref="password" name="password" className="form-control" />						
									</div>
										
									<div className="form-group">
										<input type="submit" value="Entrar" className="btn btn-primary col-sm-offset-2" />				
									</div>
					
									<MensagemPainel tipo="erro" msg={erroMsg} />									
								</form>	
							</div>
						</div>
					</div>			
				</div>	
			</div>
		);
	}
}
