
class Empresas extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null,
			empresas : [] 
		};			
	}
	
	componentDidMount() {
		this.refs.razaoSocialIni.value = "*";
		
		this.filtrar( null );		
	}
	
	filtrar( e ) {
		if ( e != null )
			e.preventDefault();
					
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );

		fetch( "/api/empresa/filtra", {
			method : "POST",			
			headers : { 
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},			
			body : JSON.stringify( { 
				razaoSocialIni : this.refs.razaoSocialIni.value,
			} )
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					this.state.empresas = dados;
						
					if ( dados.length == 0 ) {
						this.state.infoMsg = "Nenhuma empresa registrada!";						
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
		
	detalhes( e, empresaId ) {
		e.preventDefault();
		
		ReactDOM.render( <EmpresaDetalhes empresaId={empresaId} />, sistema.paginaElemento() );
	}
	
	remover( e, empresaId ) {
		e.preventDefault();
		
		fetch( "/api/empresa/deleta/"+empresaId, {
			method : "DELETE",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.status = resposta.status;
				if ( resposta.status == 200 ) {						
					This.state.infoMsg = "Empresa removida com êxito!";
					this.filtrar( e );																	
				} else if ( resposta.status == 400 ) {
					this.state.erroMsg = dados.mensagem;	
				} else {
					this.state.erroMsg = "Erro desconhecido.";
				}
				this.setState( this.state );				
			} );			 
		} );
	}
	
	paraEmpresaForm() {
		ReactDOM.render( <EmpresaForm op="cadastrar" />, sistema.paginaElemento() );
	}
	
	render() {
		const { erroMsg, infoMsg, empresas } = this.state;
		
		return (
			<div className="container">
				<div className="row">
					<h4 className="text-center col-md-12">Lista de Empresas</h4>
					<div className="tbl-pnl col-md-12">
						<table id="tabela_funcionarios" className="table table-striped table-bordered col-md-12">
							<thead>
								<tr>
									<th>ID</th>
									<th>Razao social</th>
									<th>Detalhes</th>
									<th>Remover</th>
								</tr>
							</thead>
							<tbody>
								{empresas.map( ( emp, index ) => {
									return (
										<tr>
											<td>{emp.id}</td>
											<td>{emp.razaoSocial}</td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.detalhes( e, emp.id )}>detalhes</button></td>
											<td><button className="btn btn-link" style={{ padding : 0 }} onClick={(e) => this.remover( e, emp.id )}>remover</button></td>
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
								<label className="control-label" for="razaoSocialIni">Razão social:</label>
								<input type="text" ref="razaoSocialIni" name="razaoSocialIni" className="form-control" />						
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
						<button className="btn btn-link" style={{ padding : 0 }} onClick={ (e) => this.paraEmpresaForm(e)}>Registrar nova empresa</button>	
					</div>
				</div>				 
			</div>					
		);
	}
	
}