
class Funcionarios extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			offsetId : 0, 
			paglen : 10,
			funcionarios : [] };			
	}
	
	componentDidMount() {
		this.refs.nomeIni.value = "*";
		this.refs.usernameIni.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		fetch( "/api/funcionario/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				offsetId : this.state.offsetId,
				paglen : this.state.paglen,
				nomeIni : this.refs.nomeIni.value,
				usernameIni : this.refs.usernameIni.value
			} )
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					this.state.funcionarios = dados;
						
					if ( dados.length == 0 ) {
						this.state.infoMsg = "Nenhum funcionário registrado!";						
					}							
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;	
				} else {
					this.state.erroMsg = "Erro desconhecido.";
				}
				this.setState( this.state );				
			} );			 
		} );
	}
		
	detalhes( e, funcId ) {
		e.preventDefault();
		
		ReactDOM.render( <FuncionarioDetalhes funcId={funcId}/>, sistema.paginaElemento() );
	}
	
	remover( e, funcId ) {
		e.preventDefault();
		
		fetch( "/api/funcionario/deleta/"+funcId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					This.state.infoMsg = "Funcionario removido com êxito!";
					this.filtrar();																	
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;	
				} else {
					this.state.erroMsg = "Erro desconhecido.";
				}
				this.setState( this.state );				
			} );			 
		} );
	}
	
	paraTelaRegistroFuncionario() {
		ReactDOM.render( <FuncionarioForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, funcionarios } = this.state;
		
		return (
			<div className="container">
				<div className="row">
					<h4 className="text-center col-md-12">Lista de Funcionarios</h4>
					<div className="tbl-pnl col-md-12">
						<table id="tabela_funcionarios" className="table table-striped table-bordered col-md-12">
							<thead>
								<tr>
									<th>ID</th>
									<th>Nome</th>
									<th>Nome de usuário</th>
									<th>Tipo</th>
									<th>Detalhes</th>
									<th>Remover</th>
								</tr>
							</thead>
							<tbody>
								{funcionarios.map( ( func, index ) => {
									return (
										<tr>
											<td>{func.id}</td>
											<td>{func.pessoa.nome}</td>
											<td>{func.usuario.username}</td>
											<td>{func.usuario.tipo}</td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, func.id )}>detalhes</button></td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, func.id )}>remover</button></td>
										</tr>
									);
								} ) }	
							</tbody>							
						</table>
					</div>
				</div>
								
				<MensagemPainel tipo="erro" msg={erroMsg} />				
				<MensagemPainel tipo="info" msg={infoMsg} />
				
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<form onSubmit={ (e) => this.filtrar( e ) }>
							<div className="form-group">
								<label className="control-label" for="nomeIni">Nome:</label>
								<input type="text" ref="nomeIni" name="nomeIni" className="form-control" />						
							</div>
							<div className="form-group">
								<label className="control-label" for="usernameIni">Nome do usuário:</label>
								<input type="text" ref="usernameIni" name="usernameIni" className="form-control" />						
							</div>
														
							<div className="form-group">
								<input type="submit" value="Filtrar" className="btn btn-primary" />				
							</div>		
						</form>	
					</div>
				</div>	
				<div className="row">
					<div className="col-md-3"></div>
					<div className="col-md-6">
						<button className="btn btn-link" style={{ padding : 0 }} onClick={ (e) => this.paraTelaRegistroFuncionario(e)}>Registrar novo funcionário</button>	
					</div>
				</div>				 
			</div>					
		);
	}
	
}