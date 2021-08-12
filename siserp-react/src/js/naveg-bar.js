import React from 'react';
import ReactDOM from 'react-dom';
import { Navbar, Nav, NavDropdown } from 'react-bootstrap';

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
import Usuarios from './telas/usuarios/usuarios';
import ContasPagar from './telas/contas/contas-pagar';
import ContasReceber from './telas/contas/contas-receber';

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
	
	paraTelaContasPagar() {
		ReactDOM.render( <ContasPagar />, sistema.paginaElemento() );
	}
	
	paraTelaContasReceber() {
		ReactDOM.render( <ContasReceber />, sistema.paginaElemento() );
	}
	
	paraTelaUsuarios() {
		ReactDOM.render( <Usuarios />, sistema.paginaElemento() );
	}
	
	render() {
		return(
			<Navbar bg="dark" variant="dark">
				<Navbar.Brand className="mx-3">Sistema ERP</Navbar.Brand>
				<Navbar.Toggle aria-controls="basic-navbar-nav" />
				<Navbar.Collapse id="basic-navbar.nav">
					<Nav className="mr-auto">
						{ ( sistema.usuario.grupo.nome === 'CAIXA' ) && (
							<Nav.Link onClick={ () => this.paraTelaCaixa() }>Caixa</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<NavDropdown title="Financeiro">
								<NavDropdown.Item onClick={ () => this.paraTelaFluxoCaixa() }>Fluxo de Caixa</NavDropdown.Item>
								{ (  sistema.usuario.grupo.nome === 'GERENTE' ) && (
									<NavDropdown.Item onClick={ () => this.paraTelaContasPagar() }>Contas a pagar</NavDropdown.Item>
								) }
								{ (  sistema.usuario.grupo.nome === 'GERENTE' ) && (
									<NavDropdown.Item onClick={ () => this.paraTelaContasReceber() }>Contas a receber</NavDropdown.Item>
								) }
							</NavDropdown>						
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaSedeDetalhes() }>Sede</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome !== 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaVendas() }>Vendas</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaCompras() }>Compras</Nav.Link>
						) }					
						{ ( sistema.usuario.grupo.nome !== 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaProdutos() }>Produtos</Nav.Link>
						) }							
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaFuncionarios() }>Funcionarios</Nav.Link>						
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaClientes() }>Clientes</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaFornecedores() }>Fornecedores</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaUsuarios() }>Usuarios</Nav.Link>
						) }
						<Nav.Link className="float-end" onClick={ () => this.paraTelaLogin() }>Sair</Nav.Link>
					</Nav>
				</Navbar.Collapse>
			</Navbar>
		);
	}
	
}