import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './logica/sistema';
import MensagemPainel from './componente/mensagem-painel';

import Layout from './layout.js';
import NavegBar from './naveg-bar.js';
import Inicial from './telas/inicial/inicial.js';

import loginImagem from './../imgs/login.png';

export default class Login extends React.Component {

	constructor(props) {
		super(props);
		this.state = { erroMsg: null };

		this.username = React.createRef();
		this.password = React.createRef();
	}

	entrar(e) {
		e.preventDefault();

		sistema.wsPostNoAuthorization("/api/login/entrar", {
			username: this.username.current.value,
			password: this.password.current.value
		}, (resposta) => {
			resposta.json().then((dados) => {
				sistema.token = dados.token;
				sistema.usuario = dados.usuario;

				ReactDOM.render(<Layout />, sistema.rootElemento());
				ReactDOM.render(<NavegBar />, sistema.menuNavegElemento());
				ReactDOM.render(<Inicial />, sistema.paginaElemento());
			});
		}, this);
	}


	render() {
		const { erroMsg } = this.state;

		return (
			<div>
				<h1 className="text-center bg-dark text-white p-4">Sistema ERP</h1>
				<Container className="my-5">
					<Row>
						<Col className="col-md-3"></Col>
						<Col className="col-md-6">
							<Card className="p-3">
								<h4>Tela de login</h4>
								<Form onSubmit={(e) => this.entrar(e)}>
									<Row>
										<Col className="col-md-4 py-4 px-5">
											<img src={loginImagem} alt="Imagem de login" />
										</Col>
										<Col className="col-md-7">
											<Form.Group className="mb-3">
												<Form.Label>Nome de usuário</Form.Label>
												<Form.Control type="text" ref={this.username} id="username" name="username" />
											</Form.Group>
											<Form.Group className="mb-3">
												<Form.Label>Senha</Form.Label>
												<Form.Control type="password" ref={this.password} id="password" name="password" />
											</Form.Group>
											<Form.Group className="mb-3">
												<Button type="submit" color="primary">
													<i className="fa-solid fa-arrow-right-to-bracket">&nbsp;</i>
													Entrar
												</Button>
											</Form.Group>

											<Form.Group className="mb-3">
												<MensagemPainel cor="danger" msg={erroMsg} />
											</Form.Group>
										</Col>
									</Row>
								</Form>
							</Card>
						</Col>
					</Row>
				</Container>
			</div>
		);
	}
}
