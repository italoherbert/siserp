import * as moment from 'moment';
import $ from 'jquery';

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
	
	showLoadingSpinner() {
		$("#carregando").show();
	}
	
	hideLoadingSpinner() {
		$("#carregando").hide();
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
	
	paraFloat( valor ) {
		if ( (""+valor) === "0" ) 
			return 0;
		
		return parseFloat( (""+valor).replace( ',', '.' ) );
	}
	
	formataFloat( valor ) {
		if ( (""+valor) === "0" ) 
			return "0";
		
		let v = (""+valor).split( '.' );
		
		let frac = 0;
		if ( v.length === 2 )
			frac = parseInt( v[1] );
		
		return ( frac === 0 ? v[0] : v[0]+','+v[1] );				
	}

	trataRespostaNaoOk( resposta, compRef ) {
		if ( resposta.status === 400 ) {
			resposta.json().then( (dados) => {
				if ( dados.mensagem == null ) {
					compRef.setState( { erroMsg : 'Mensagem de erro não encontrada pelo código recebido. Codigo='+dados.codigo } );
				} else {
					compRef.setState( { erroMsg : dados.mensagem } );				
				}
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