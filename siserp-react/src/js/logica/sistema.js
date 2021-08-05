import * as moment from 'moment';

class Sistema {

	constructor() {
		this.token = null;
		this.usuario = null;
	}

	paginaElemento() {
		return document.getElementById( "pagina" );
	}

	menuNavegElemento() {
		return document.getElementById( "menu-naveg" );
	}

	rootElemento() {
		return document.getElementById( "root" );
	}
		
	scrollTo( strEL ) {
		let el = document.querySelector( "#"+strEL );
		if ( el )
			el.scrollIntoView();			
	}

	showHide( strEl ) {
		let el = document.getElementById( strEl );
		if ( el.style.visibility === 'hidden' ) {
			el.style.display = "block";
			el.style.visibility = "visible";
		} else {
			el.style.display = "none";
			el.style.visibility = "hidden";
		}
	}
	
	formataData( date ) {
		return moment( date ).format( "DD/MM/YYYY" );
	}
	
	formataReal( valor ) {
		if ( valor === 0 ) 
			return "R$ 0,00";
		
		let n = parseInt( parseFloat( valor ) * 100 );
		let s = ""+n;
		return "R$ "+s.substring( 0, s.length-2 ) + ',' + s.substring( s.length-2, s.length );		
	}

	trataRespostaNaoOk( resposta, compRef ) {
		if ( resposta.status === 400 ) {
			resposta.json().then( (dados) => {											
				compRef.setState( { erroMsg : dados.mensagem } );				
			} );
		} else {
			compRef.setState( { erroMsg : this.getMensagemErro( resposta.status ) } );				
		}
	}
	
	trataRespostaNaoOkCBK( resposta, callback ) {
		if ( resposta.status === 400 ) {
			resposta.json().then( (dados) => {			
				if ( typeof( callback ) == 'function' )
					callback.call( this, dados.mensagem );
			} );
		} else {
			if ( typeof( callback ) == 'function' )
				callback.call( this, this.getMensagemErro( resposta.status ) );				
		}	
	}

	getMensagemErro( status ) {						
		switch( status ) {
			case 401:
			case 403:
				return "Você não tem permissão para acessar esse recurso";
				
			case 404:
				return "Recurso não encontrado no servidor.";	
								
			case 500:
				return "Erro interno no servidor.";
				
			default:
				return "Erro desconhecido";
		}
	}
	
}

let sistema = new Sistema();
export default sistema;