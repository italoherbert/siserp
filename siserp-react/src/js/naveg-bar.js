import React from 'react';
import ReactDOM from 'react-dom';
import { Navbar, Nav, NavDropdown, Row, Col } from 'react-bootstrap';

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

import caixaImagem from './../imgs/caixa.png';
import vendasImagem from './../imgs/vendas.png';
import produtosImagem from './../imgs/produtos.png';
import financeiroImagem from './../imgs/financas.png';
import empresaImagem from './../imgs/empresa.png';
import comprasImagem from './../imgs/compras.png';
import funcionariosImagem from './../imgs/funcionarios.png';
import clientesImagem from './../imgs/clientes.png';
import fornecedoresImagem from './../imgs/fornecedores.png';
import usuariosImagem from './../imgs/usuarios.png';

import sairImagem from './../imgs/sair.png';

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
							<Nav.Link onClick={ () => this.paraTelaCaixa() }>
								<Row className="text-center">
									<img src={caixaImagem} />
								</Row>
								<Row className="p-2">									
									<span className="text-center">Caixa</span>
								</Row>
							</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<div className="m-2">
								<Row>
									<Col className="text-center">
										<img src={financeiroImagem} />
									</Col>
								</Row>
								<Row className="m-0 p-0">							
									<NavDropdown title="Financeiro">																
										<NavDropdown.Item onClick={ () => this.paraTelaFluxoCaixa() }>
											Fluxo de Caixa
										</NavDropdown.Item>
										{ (  sistema.usuario.grupo.nome === 'GERENTE' ) && (
											<NavDropdown.Item onClick={ () => this.paraTelaContasPagar() }>
												Contas a pagar
											</NavDropdown.Item>
										) }
										{ (  sistema.usuario.grupo.nome === 'GERENTE' ) && (
											<NavDropdown.Item onClick={ () => this.paraTelaContasReceber() }>
												Contas a receber
											</NavDropdown.Item>
										) }
									</NavDropdown>						
								</Row>
							</div>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaSedeDetalhes() }>
								<Row>
									<Col className="text-center">
										<img src={empresaImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Sede</span>
								</Row>
							</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome !== 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaVendas() }>
								<Row>
									<Col className="text-center">
										<img src={vendasImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Vendas</span>
								</Row>
							</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaCompras() }>
								<Row>
									<Col className="text-center">
										<img src={comprasImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Compras</span>
								</Row>
							</Nav.Link>
						) }					
						{ ( sistema.usuario.grupo.nome !== 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaProdutos() }>
								<Row>
									<Col className="text-center">
										<img src={produtosImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Produtos</span>
								</Row>
							</Nav.Link>
						) }							
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaFuncionarios() }>
								<Row>
									<Col className="text-center">
										<img src={funcionariosImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Funcionarios</span>
								</Row>
							</Nav.Link>						
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaClientes() }>
								<Row>
									<Col className="text-center">
										<img src={clientesImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Clientes</span>
								</Row>
							</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'SUPERVISOR' || sistema.usuario.grupo.nome === 'GERENTE' ) && (
							<Nav.Link onClick={ () => this.paraTelaFornecedores() }>
								<Row>
									<Col className="text-center">
										<img src={fornecedoresImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Fornecedores</span>
								</Row>
							</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaUsuarios() }>
								<Row>
									<Col className="text-center">
										<img src={usuariosImagem} />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Usuarios</span>
								</Row>
							</Nav.Link>
						) }
						<Nav.Link onClick={ () => this.paraTelaLogin() }>
							<Row>
								<Col className="text-center">
									<img src={sairImagem} />
								</Col>
							</Row>
							<Row className="p-2 text-center">									
								<span>Sair</span>
							</Row>
						</Nav.Link>
					</Nav>
				</Navbar.Collapse>
			</Navbar>
		);
	}
	
}