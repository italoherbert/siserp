
class Navbar extends React.Component {
	
	constructor( props ) {
		super( props );
	}
	
	componentDidMound() {
		
	}
	
	mostraEscondeMenu( elementoId ) {
		sistema.showHide( elementoId );
		this.setState( this.state );
	}
	
	paraTelaSedeDetalhes() {
		ReactDOM.render( <SedeDetalhes />, sistema.paginaElemento() );
	}
	
	paraTelaFuncionarios() {
		ReactDOM.render( <Funcionarios />, sistema.paginaElemento() );
	}
	
	render() {
		return(
			<nav className="navbar navbar-expand-sm navbar-dark bg-dark">				
				<a className="navbar-brand" href="#">Sistema ERP</a>

				<div className="collapse navbar-collapse" id="nav-content">   
					<ul className="navbar-nav">
						<li className="nav-item">
							<div className="dropdown show">
							  	<a className="btn btn-link nav-link dropdown-toggle" href="#" role="button" id="financeiro_menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							    	Financeiro
							  	</a>
							
							  	<div className="dropdown-menu" aria-labelledby="financeiro_menu">
							    	<a className="dropdown-item" href="#">Caixa</a>
							    	<a className="dropdown-item" href="#">Fluxo de caixa</a>
							    	<a className="dropdown-item" href="#">Contas a receber</a>
							    	<a className="dropdown-item" href="#">Contas a pagar</a>
							    	<a className="dropdown-item" href="#">Recebimento avulso</a>
							    	<a className="dropdown-item" href="#">Relatórios</a>
							  	</div>
							</div>
						</li>
						
											
						<li className="nav-item">
							<a className="nav-link" href="#" onClick={ () => this.paraTelaSedeDetalhes() }>Sedes</a>
						</li>
												
						<li className="nav-item">
							<a className="nav-link" href="#">Vendas</a>
						</li>
						<li className="nav-item">
							<a className="nav-link" href="#">Compras</a>
						</li>						
						<li className="nav-item">
							<a className="nav-link" href="#">Produtos</a>
						</li>
						<li className="nav-item">
							<a className="nav-link" href="#" onClick={ () => this.paraTelaFuncionarios() }>Funcionarios</a>
						</li>
						<li className="nav-item">
							<a className="nav-link" href="#">Clientes</a>
						</li>
						
						{ ( sistema.usuario.tipo == 'ADMIN' || 
								sistema.usuario.tipo == 'GERENTE' ) &&  (
							<li className="nav-item">
								<a className="nav-link" href="#">Fornecedores</a>
							</li>
						) }
												
					</ul>
				</div>
			</nav>
		);
	}
	
}