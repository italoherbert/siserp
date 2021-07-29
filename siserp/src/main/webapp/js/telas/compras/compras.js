
class Compras extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null,
			infoMsg : null,
			compras : [], 
			itens : []
		};			
	}
	
	componentDidMount() {
		
	}
	
	carregaItens( item ) {		
		let vet = [ 'aaa', 'aab', 'bca', 'abc', 'cab' ];
								
		let vet2;
		if ( item == null ) {
			vet2 = vet;
		} else {	
			vet2 = new Array();
			for( let i = 0; i < vet.length; i++ ) {
				if ( vet[i].startsWith( item ) )
					vet2.push( vet[i] );
			}
		}
		
		this.state.itens = vet2;		
		this.setState( this.state );		
	}
		
		
	render() {		
		const { erroMsg, infoMsg, itens } = this.state;
		
		return (
			<div className="container">
				<form onSubmit={ (e) => this.salvar( e ) }>	
					<InputDropdown itens={itens} 						
						carregaItens={ (item) => this.carregaItens( item ) } />
																						
					<MensagemPainel tipo="erro" msg={erroMsg} />																											
					<MensagemPainel tipo="info" msg={infoMsg} />
				</form>																											
			</div>					
		);
	}
	
}