
class CategoriaDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			categoria : {} 
		};
	}
	
	componentDidMount() {
		let categoriaId = this.props.categoriaId;
		
		fetch( "/api/categoria/get/"+categoriaId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status == 200 ) {						
				resposta.json().then( (dados) => {											
					this.state.categoria = dados;
					this.setState( this.state );				
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );				
			}			
		} );
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<CategoriaForm op="editar"
				categoriaId={this.props.categoriaId} 
				descricao={this.state.categoria.descricao} 
			/>,
			sistema.paginaElemento() );
	}
	
	render() {
		const { categoria, erroMsg, infoMsg } = this.state;
		
		return( 
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados do categoria</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">Descrição: </span>
								<span className="text-info">{categoria.descricao}</span>
																
								<MensagemPainel tipo="erro" msg={erroMsg} />
								<MensagemPainel tipo="info" msg={infoMsg} />																							
							</div>									
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<a href="#" onClick={(e) => this.editar( e )}>Editar categoria</a>																																																				
							</div>
						</div>												
					</div>
				</div>															
			</div>
		);
	}

}