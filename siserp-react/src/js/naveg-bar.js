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
import Configs from './telas/configs/configs';

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
import configsImagem from './../imgs/configs.png';

import sairImagem from './../imgs/sair.png';

import sistema from './logica/sistema';
import LogoPainel from './componente/logo-painel';

export default class NavegBar extends React.Component {
		
	constructor( props ) {
		super( props );
		
		this.state = {
			logoBase64 : null
		};
	}		
		
	mostraEscondeMenu( elementoId ) {
		sistema.showHide( elementoId );
		this.setState( this.state );
	}
	
	componentDidMount() {
		sistema.wsGet( '/api/config/logo/get', (resposta) => {
			resposta.json().then( (dados) => {
				if ( dados.logoBase64 !== null && dados.logoBase64 !== undefined )
					ReactDOM.render( <LogoPainel src={dados.logoBase64} />, sistema.logoElemento() );
			} );
		}, this );
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
	
	paraTelaConfigs() {
		ReactDOM.render( <Configs />, sistema.paginaElemento() );
	}
	
	render() {
		return(
			<Navbar bg="dark" variant="dark">
				<Navbar.Toggle aria-controls="basic-navbar-nav" />
				<Navbar.Collapse id="basic-navbar.nav" className="mx-3">
					<Nav className="mr-auto">						
						{ ( sistema.usuario.grupo.nome === 'CAIXA' ) && (
							<Nav.Link onClick={ () => this.paraTelaCaixa() }>
								<Row className="text-center">
									<img src={caixaImagem} alt="Imagem de caixa" />
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
										<img src={financeiroImagem} alt="Imagem de finanças" />
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
										<img src={empresaImagem} alt="Imagem de sede de empresa" />
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
										<img src={vendasImagem} alt="Imagem de vendas" />
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
										<img src={comprasImagem} alt="Imagem de compras" />
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
										<img src={produtosImagem} alt="Imagem de produtos" />
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
										<img src={funcionariosImagem} alt="Imagem de funcionario" />
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
										<img src={clientesImagem} alt="Imagem de cliente" />
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
										<img src={fornecedoresImagem} alt="Imagem de fornecedor" />
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
										<img src={usuariosImagem} alt="Imagem de usuario" />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Usuarios</span>
								</Row>
							</Nav.Link>
						) }
						{ ( sistema.usuario.grupo.nome === 'ADMIN' ) && (
							<Nav.Link onClick={ () => this.paraTelaConfigs() }>
								<Row>
									<Col className="text-center">
										<img src={configsImagem} alt="Imagem de configurações" />
									</Col>
								</Row>
								<Row className="p-2 text-center">									
									<span>Configurações</span>
								</Row>
							</Nav.Link>
						) }
						<Nav.Link onClick={ () => this.paraTelaLogin() }>
							<Row>
								<Col className="text-center">
									<img src={sairImagem} alt="Imagem de sair" />
								</Col>
							</Row>
							<Row className="p-2 text-center">									
								<span>Sair</span>
							</Row>
						</Nav.Link>
					</Nav>
				</Navbar.Collapse>

				<span id="logo"></span>
			</Navbar>				
		);
	}
	
}