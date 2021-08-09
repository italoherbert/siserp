import React from 'react';
import ReactDOM from 'react-dom';
import { Navbar, NavDropdown, Nav } from 'react-bootstrap';

import Login from './login';
import SedeDetalhes from './telas/sede/sede-detalhes';
import Funcionarios from './telas/funcionarios/funcionarios';
import Fornecedores from './telas/fornecedores/fornecedores';
import Produtos from './telas/produtos/produtos';
import Categorias from './telas/categorias/categorias';
import Compras from './telas/compras/compras';
import Clientes from './telas/clientes/clientes';
import Vendas from './telas/vendas/vendas';
import Caixa from './telas/caixa/caixa';
import CaixaFluxo from './telas/caixa/caixa-fluxo';

import sistema from './logica/sistema.js';

export default class NavegBar extends React.Component {
		
	mostraEscondeMenu( elementoId ) {
		sistema.showHide( elementoId );
		this.setState( this.state );
	}
	
	paraTelaLogin() {
		ReactDOM.render( <Login />, sistema.rootElemento() );
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
	
	paraTelaClientes() {
		ReactDOM.render( <Clientes />, sistema.paginaElemento() );
	}
	
	paraTelaVendas() {
		ReactDOM.render( <Vendas />, sistema.paginaElemento() );
	}
	
	paraTelaCaixa() {
		ReactDOM.render( <Caixa />, sistema.paginaElemento() );
	}
	
	paraTelaFluxoCaixa() {
		ReactDOM.render( <CaixaFluxo />, sistema.paginaElemento() );
	}
	
	render() {
		return(
			<Navbar bg="dark" variant="dark">
				<Navbar.Brand className="mx-3">Sistema ERP</Navbar.Brand>
				<Navbar.Toggle aria-controls="basic-navbar-nav" />
				<Navbar.Collapse id="basic-navbar.nav">
					<Nav className="me-auto">
						<NavDropdown title="Financeiro" id="basic-nav-dropdown">
							<NavDropdown.Item onClick={ () => this.paraTelaCaixa() }>Caixa</NavDropdown.Item>
							<NavDropdown.Item onClick={ () => this.paraTelaFluxoCaixa() }>Fluxo de caixa</NavDropdown.Item>
							<NavDropdown.Item>Contas a receber</NavDropdown.Item>
							<NavDropdown.Item>Contas a pagar</NavDropdown.Item>
							<NavDropdown.Item>Recebimento avulso</NavDropdown.Item>
							<NavDropdown.Item>Relatórios</NavDropdown.Item>
						</NavDropdown>
						<Nav.Link onClick={ () => this.paraTelaSedeDetalhes() }>Sede</Nav.Link>
						<Nav.Link onClick={ () => this.paraTelaVendas() }>Vendas</Nav.Link>
						<Nav.Link onClick={ () => this.paraTelaCompras() }>Compras</Nav.Link>
						<NavDropdown title="Produtos" id="basic-nav-dropdown">
							<NavDropdown.Item onClick={ () => this.paraTelaProdutos() }>Produtos</NavDropdown.Item>
							<NavDropdown.Item onClick={ () => this.paraTelaCategorias() }>Categorias</NavDropdown.Item>
							<NavDropdown.Item>Impostos</NavDropdown.Item>
						</NavDropdown>
						<Nav.Link onClick={ () => this.paraTelaFuncionarios() }>Funcionarios</Nav.Link>
						<Nav.Link onClick={ () => this.paraTelaClientes() }>Clientes</Nav.Link>
						{ ( sistema.usuario.tipo === 'ADMIN' || sistema.usuario.tipo === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaFornecedores() }>Fornecedores</Nav.Link>
						) }
						<Nav.Link className="float-end" onClick={ () => this.paraTelaLogin() }>Sair</Nav.Link>
					</Nav>
				</Navbar.Collapse>
			</Navbar>
		);
	}
	
}