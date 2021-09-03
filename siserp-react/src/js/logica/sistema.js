import * as moment from 'moment';
import $ from 'jquery';

class Sistema {

	constructor() {
		this.token = null;
		this.usuario = null;	
		this.logo = null;
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
	
	logoElemento() {
		return document.getElementById( "logo" );
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
		if ( date === null || date === undefined || date === '' )
			return "Formato de data não reconhecido";
		
		return moment( date ).format( "DD/MM/YYYY" );
	}
	
	formataReal( valor ) {
		let v = parseFloat( parseFloat( valor ).toFixed( 2 ) );
		if ( v === 0 ) 
			return "R$ 0,00";
				
		if ( Math.abs( v ) >= 1.0 ) {
			let n = parseInt( v * 100 );
			let s = ""+n;
			return "R$ "+s.substring( 0, s.length-2 ) + ',' + s.substring( s.length-2, s.length );		
		} else {
			return"R$ " +( ( ""+v ).replace( '.', ',' ) );			
		}				
	}
	
	paraFloat( valor ) {
		if ( isNaN( (""+valor).replace( ',', '.' ) ) === true )
			return valor;
		
		if ( (""+valor) === "0" ) 
			return 0;
		
		return parseFloat( parseFloat( (""+valor).replace( ',', '.' ) ).toFixed( 2 ) );
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
	
	wsPostNoAuthorization( url, requestDados, responseOk, compRef ) {
		this.ws( url, {
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8'
			},
			body : JSON.stringify( requestDados )
		}, responseOk, compRef );
	}
	
	wsGetPDF( url, responseOk, compRef ) {
		this.ws( url, {
			method : 'GET',
			headers : {
				'Accept' : 'application/pdf',
				'Authorization' : 'Bearer '+this.token
			}
		}, responseOk, compRef );
	}
	
	wsGet( url, responseOk, compRef ) {
		this.wsGetOrDelete( url, 'GET', responseOk, compRef );
	}
	
	wsDelete( url, responseOk, compRef ) {
		this.wsGetOrDelete( url, 'DELETE', responseOk, compRef );
	}
	
	wsPost( url, requestDados, responseOk, compRef ) {
		this.wsSave( url, 'POST', requestDados, responseOk, compRef );
	}
	
	wsPut( url, requestDados, responseOk, compRef ) {
		this.wsSave( url, 'PUT', requestDados, responseOk, compRef );
	}
	
	wsPatch( url, requestDados, responseOk, compRef ) {
		this.wsSave( url, 'PATCH', requestDados, responseOk, compRef );
	}
			
	wsSave( url, method, requestDados, responseOk, compRef ) {
		this.ws( url, {
			method : method,
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8',
				'Authorization' : 'Bearer '+this.token
			},
			body : JSON.stringify( requestDados )
		}, responseOk, compRef );
	}
	
	wsGetOrDelete( url, method, responseOk, compRef ) {
		this.ws( url, {
			method : method,
			headers : {
				'Authorization' : 'Bearer '+this.token
			}
		}, responseOk, compRef );
	}
		
	ws( url, request, responseOk, compRef ) {
		compRef.setState( { erroMsg : null, infoMsg : null } );
		
		this.showLoadingSpinner();						
				
		fetch( url, request ).then( ( resposta ) => {
			if ( resposta.status === 200 ) {
				if ( typeof( responseOk ) === 'function' )
					responseOk.call( this, resposta );
			} else {
				this.trataRespostaNaoOk( resposta, compRef );
			}
			this.hideLoadingSpinner();
		} ).catch( (error) => {
			this.hideLoadingSpinner();
			compRef.setState( { erroMsg : "Servidor indisponível." } );
		} );		
	}		

	trataRespostaNaoOk( resposta, compRef ) {
		if ( resposta.status === 400 ) {
			resposta.json().then( (dados) => {
				if ( dados.mensagem === null ) {
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