import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import { Container, Navbar, NavDropdown, Nav} from 'react-bootstrap';

import SedeDetalhes from './telas/sede/sede-detalhes';
import Funcionarios from './telas/funcionarios/funcionarios';
import Fornecedores from './telas/fornecedores/fornecedores';
import Produtos from './telas/produtos/produtos';
import Categorias from './telas/categorias/categorias';
import Compras from './telas/compras/compras';

import sistema from './logica/sistema.js';

export default class NavegBar extends React.Component {
	
	constructor( props ) {
		super( props );
	}
	
	componentDidMound() {
		
	}
	
	mostraEscondeMenu( elementoId ) {
		sistema.showHide( elementoId );
		this.setState( this.state );
	}
	
	paraTelaSedeDetalhes() {
		ReactDOM.render( <SedeDetalhes />, sistema.paginaElemento() );
	}
	
	paraTelaFuncionarios() {
		ReactDOM.render( <Funcionarios />, sistema.paginaElemento() );
	}
	
	paraTelaFornecedores() {
		ReactDOM.render( <Fornecedores />, sistema.paginaElemento() );
	}
	
	paraTelaProdutos() {
		ReactDOM.render( <Produtos />, sistema.paginaElemento() );
	}
	
	paraTelaCategorias() {
		ReactDOM.render( <Categorias />, sistema.paginaElemento() );
	}
	
	paraTelaCompras() {
		ReactDOM.render( <Compras />, sistema.paginaElemento() );
	}
	
	render() {
		return(
			<Navbar bg="dark" variant="dark">
				<Container>
					<Navbar.Brand href="#">Sistema ERP</Navbar.Brand>
					<Navbar.Toggle aria-controls="basic-navbar-nav" />
					<Navbar.Collapse id="basic-navbar.nav">
						<Nav className="me-auto">
							<NavDropdown title="Financeiro" id="basic-nav-dropdown">
								<NavDropdown.Item href="#">Caixa</NavDropdown.Item>
								<NavDropdown.Item href="#">Fluxo de caixa</NavDropdown.Item>
								<NavDropdown.Item href="#">Contas a receber</NavDropdown.Item>
								<NavDropdown.Item href="#">Contas a pagar</NavDropdown.Item>
								<NavDropdown.Item href="#">Recebimento avulso</NavDropdown.Item>
								<NavDropdown.Item href="#">Relatórios</NavDropdown.Item>
							</NavDropdown>
							<Nav.Link href="#" onClick={ () => this.paraTelaSedeDetalhes() }>Sede</Nav.Link>
							<Nav.Link href="#">Vendas</Nav.Link>
							<Nav.Link href="#" onClick={ () => this.paraTelaCompras() }>Compras</Nav.Link>
							<NavDropdown title="Produtos" id="basic-nav-dropdown">
								<NavDropdown.Item href="#" onClick={ () => this.paraTelaProdutos() }>Produtos</NavDropdown.Item>
								<NavDropdown.Item href="#" onClick={ () => this.paraTelaCategorias() }>Categorias</NavDropdown.Item>
								<NavDropdown.Item href="#">Impostos</NavDropdown.Item>
							</NavDropdown>
							<Nav.Link href="#" onClick={ () => this.paraTelaFuncionarios() }>Funcionarios</Nav.Link>
							<Nav.Link href="#">Clientes</Nav.Link>
							{ ( sistema.usuario.tipo == 'ADMIN' || sistema.usuario.tipo == 'GERENTE' ) && (
								<Nav.Link href="#" onClick={ () => this.paraTelaFornecedores() }>Fornecedores</Nav.Link>
							) }
						</Nav>
					</Navbar.Collapse>
				</Container>						
			</Navbar>
		);
	}
	
}