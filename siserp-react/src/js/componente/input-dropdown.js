
class InputDropdown extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { itemPadrao : "Nenhum item" }
	}
	
	componentDidMount() {		
		if ( this.props.itemPadrao != null && this.props.itemPadrao != undefined )
			this.state.itemPadrao = this.props.itemPadrao;
		
		$("#lista_dropdown").hide();
	}
	
	onChangeTexto( e ) {
		e.preventDefault();

		$("#lista_dropdown").show();
			
		let item = this.refs.texto.value;	
						
		if ( typeof( this.props.carregaItens ) == "function" ) {
			this.props.carregaItens.call( this, item );
			this.setState( this.state );
		}
	}
	
	escondeDropdown( e ) {
		e.preventDefault();
							
		this.refs.texto.value = "";
				
		$("#lista_dropdown").hide();														
	}
		
	onClickItem( e, item ) {
		e.preventDefault();
							
		$("#lista_dropdown").hide();							
							
		this.refs.texto.value = item;		
		this.setState( this.state );
	}
	
	render() {
		return (
			<div className="dropdown">
				<input id="texto" type="text" ref="texto" name="texto" 
			  			onChange={ (e) => this.onChangeTexto( e ) }			  			
			  			className="form-control" />
			  				
			  	<div id="lista_dropdown" className="dropdown-menu" aria-labelledby="texto">			  					  			
			  		{this.props.itens.map( (item, index) => {
						return( 
							<a key={index} className="dropdown-item" href="#" 
								onClick={ (e) => this.onClickItem( e, item ) }>
									{item}
							</a> 
						)
					} ) }		
					<a href="#" className="dropdown-item" 
			  			onClick={ (e) => this.escondeDropdown( e ) }>
			  				{this.state.itemPadrao}
			  		</a>	    	
			  	</div>
			</div>
		);
	}
	
}