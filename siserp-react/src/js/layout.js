import React from 'react';
	
export default class Layout extends React.Component {
					
	render() {
		return( 
			<div>
				<div id="carregando" style={{ display: 'none', position: 'fixed', margin: '10em 46%', zIndex: '1000' }}>
					<div className="spinner-border">
						<span className="sr-only">Carregando...</span>
					</div>
				</div>
						
				<div id="menu-naveg"></div>
				<div className="mx-5 my-2 p-4 bg-light rounded" style={{minHeight: '30em'}}>							
					<div id="pagina"></div>
				</div>
								
				<div className="bg-dark p-3 text-center text-light">
					<h5>Produzido por Ítalo Herbert</h5>
				</div>
			</div>
		);
	}
			
}