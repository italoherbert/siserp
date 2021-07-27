
class Sistema {
	
	constructor() {
		this.token = null;
		this.usuario = null;
		this.datatableCFG = {
			paging : false,
			searching : false,
			language : { url: "/Portuguese-Brasil.json" }
		}
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
			
}

const sistema = new Sistema();