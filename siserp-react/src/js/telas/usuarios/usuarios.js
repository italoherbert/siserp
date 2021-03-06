import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Grupos from './grupos';
import Recursos from './recursos';

export default class Usuarios extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			usuarios: []
		};

		this.usernameIni = React.createRef();
	}

	componentDidMount() {
		this.usernameIni.current.value = "*";

		this.filtrar(null, false);
	}

	filtrar(e) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/usuario/filtra", {
			"usernameIni": this.usernameIni.current.value
		}, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ usuarios: dados });

				if (dados.length === 0)
					this.setState({ infoMsg: "Nenhum usuario registrado!" });
			});
		}, this);
	}

	paraTelaGrupos(e) {
		ReactDOM.render(<Grupos />, sistema.paginaElemento());
	}

	paraTelaRecursos(e) {
		ReactDOM.render(<Recursos />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, usuarios } = this.state;

		return (
			<Container>
				<Row>
					<Col>
						<Form className="float-start">
							<Button variant="primary" onClick={(e) => this.paraTelaGrupos(e)}>
								<i className="fa-solid fa-user-group">&nbsp;</i>
								Grupos de usuário
							</Button>
							<Button variant="primary" className="mx-2" onClick={(e) => this.paraTelaRecursos(e)}>
								<i className="fa-solid fa-box">&nbsp;</i>
								Recursos
							</Button>
						</Form>
					</Col>
				</Row>

				<Row>
					<Col>
						<h4 className="text-center">Lista de Usuarios</h4>
						<div className="tbl-pnl">
							<Table striped hover>
								<thead>
									<tr>
										<th>ID</th>
										<th>Username</th>
										<th>Grupo</th>
									</tr>
								</thead>
								<tbody>
									{usuarios.map((usuario, index) => {
										return (
											<tr key={index}>
												<td>{usuario.id}</td>
												<td>{usuario.username}</td>
												<td>{usuario.grupo.nome}</td>
											</tr>
										);
									})}
								</tbody>
							</Table>
						</div>
					</Col>
				</Row>

				<br />

				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Row>
					<Col className="col-md-8">
						<Card className="p-3">
							<h4>Filtrar usuarios</h4>

							<Form onSubmit={(e) => this.filtrar(e, true)}>
								<Form.Group className="mb-3">
									<Form.Label>Nome de usuário: </Form.Label>
									<Form.Control type="text" ref={this.usernameIni} name="usernameIni" />
								</Form.Group>

								<Button type="submit" variant="primary">
									<i className="fa-solid fa-filter">&nbsp;</i>
									Filtrar
								</Button>
							</Form>
						</Card>
					</Col>
				</Row>
			</Container>
		);
	}

}