
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
		if ( el.style.visibility == 'hidden' ) {
			el.style.display = "block";
			el.style.visibility = "visible";
		} else {
			el.style.display = "none";
			el.style.visibility = "hidden";
		}
	}

	trataRespostaNaoOk( resposta, compRef ) {
		if ( resposta.status == 400 ) {
			resposta.json().then( (dados) => {											
				compRef.state.erroMsg = dados.mensagem;	
				compRef.setState( compRef.state );				
			} );
		} else {
			compRef.state.erroMsg = this.getMensagemErro( resposta.status );
			compRef.setState( compRef.state );				
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

let sistema;
export default sistema = new Sistema();