import React from 'react';

export default class InputDropdown extends React.Component {
	
	constructor( props ) {
		super( props );
		
		this.state = { id : 'input-dropdown'+new Date().getTime()+Math.random() };
		this.texto = React.createRef();
	}
	
	componentDidMount() {						
		document.getElementById( this.state.id ).style.display = 'none';
		document.getElementById( this.state.id ).style.visibility = 'hidden';				

		let referencia;
		if ( this.props.referencia !== null && this.props.referencia !== undefined )
			referencia = this.props.referencia;	
		else referencia = this.texto;

		referencia.current.value = '';
	}
	
	onChangeTexto( e ) {
		e.preventDefault();

		document.getElementById( this.state.id ).style.display = 'block';
		document.getElementById( this.state.id ).style.visibility = 'visible';
					
		let item;
		if ( this.props.referencia !== null && this.props.referencia !== undefined )
			item = this.props.referencia.current.value;	
		else item = this.texto.current.value;
						
		if ( typeof( this.props.carregaItens ) === "function" ) {
			this.props.carregaItens.call( this, item );
			this.setState( {} );
		}
	}
	
	onClickMostraEscondeDropdown( e ) {
		e.preventDefault();
		
		var el = document.getElementById( this.state.id );
		if ( el.style.visibility === 'visible' ) {
			el.style.display = 'none';
			el.style.visibility = 'hidden';																						
		} else {
			el.style.display = 'block';
			el.style.visibility = 'visible';

			if ( typeof( this.props.carregaItens ) === "function" ) {
				let item;
				if ( this.props.referencia !== null && this.props.referencia !== undefined )
					item = this.props.referencia.current.value;	
				else item = this.texto.current.value;
			
				this.props.carregaItens.call( this, item );
				this.setState( {} );
			}
		}
	}
		
	onClickItem( e, item, index ) {
		e.preventDefault();
							
		document.getElementById( this.state.id ).style.display = 'none';
		document.getElementById( this.state.id ).style.visibility = 'hidden';							
							
		if ( this.props.referencia !== null && this.props.referencia !== undefined )
			this.props.referencia.current.value = item;	
		else this.texto.current.value = item;	
		
		if ( typeof( this.props.carregaItens ) === "function" ) {
			this.props.carregaItens.call( this, item );
			this.setState( {} );
		}
		
		if ( typeof( this.props.itemClicado ) === "function" ) {
			this.props.itemClicado.call( this, item, index );
			this.setState( {} );
		}
	}		
		
	render() {
		const { id } = this.state;		
				
		let referencia;
		if ( this.props.referencia !== null && this.props.referencia !== undefined )
			referencia = this.props.referencia;	
		else referencia = this.texto;	
				
		return (
			<div className="dropdown">
				<input type="text" ref={referencia} name="texto" 
			  			onChange={ (e) => this.onChangeTexto( e ) } 
						onClick={ (e) => this.onClickMostraEscondeDropdown( e ) }
			  			className="form-control" />
			  				
			  	<div id={id} className="dropdown-menu" aria-labelledby="texto">			  					  			
			  		{this.props.itens.map( (item, index) => {
						return( 
							<button key={index} className="dropdown-item"  
								onClick={ (e) => this.onClickItem( e, item, index ) }>
									{item}
							</button> 
						)
					} ) }		 	
			  	</div>
			</div>
		);
	}
	
}