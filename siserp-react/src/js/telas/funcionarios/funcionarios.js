import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FuncionarioDetalhes from './funcionario-detalhes';
import FuncionarioForm from './funcionario-form';

export default class Funcionarios extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			funcionarios: [],

			remocaoModalVisivel: false,
			remocaoModalOkFunc: () => { },
			remocaoModalCancelaFunc: () => { }
		};

		this.nomeIni = React.createRef();
		this.usernameIni = React.createRef();
	}

	componentDidMount() {
		this.nomeIni.current.value = "*";
		this.usernameIni.current.value = "*";

		this.filtrar(null, false);
	}

	filtrar(e, filtrarBTClicado) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/funcionario/filtra", {
			nomeIni: this.nomeIni.current.value,
			usernameIni: this.usernameIni.current.value
		}, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ funcionarios: dados });
				if (dados.length === 0 && filtrarBTClicado === true)
					this.setState({ infoMsg: "Nenhum funcionário encontrado!" });
			});
		}, this);
	}

	detalhes(e, funcId) {
		e.preventDefault();

		ReactDOM.render(<FuncionarioDetalhes funcId={funcId} />, sistema.paginaElemento());
	}

	removerSeConfirmado(e, funcId) {
		this.setState({
			remocaoModalVisivel: true,
			remocaoModalOkFunc: () => {
				this.setState({ remocaoModalVisivel: false });
				this.remover(e, funcId);
			},
			remocaoModalCancelaFunc: () => {
				this.setState({ remocaoModalVisivel: false });
			}
		})
	}

	remover(e, funcId) {
		e.preventDefault();

		sistema.wsDelete("/api/funcionario/deleta/" + funcId, (resposta) => {
			this.filtrar(null, false);
			this.setState({ infoMsg: "Funcionario removido com êxito!" });
		}, this);
	}

	paraTelaRegistro() {
		ReactDOM.render(<FuncionarioForm op="cadastrar" />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, funcionarios, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;

		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de funcionario</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o funcionario selecionado?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc()}>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc()}>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>

				<Row>
					<Col>
						<Form className="float-end">
							<Button variant="primary" onClick={(e) => this.paraTelaRegistro(e)}>
								<i className="fa-solid fa-file">&nbsp;</i>
								Registrar novo funcionário
							</Button>
						</Form>
					</Col>
				</Row>

				<h4 className="text-center col-md-12">Lista de Funcionarios</h4>
				<div className="tbl-pnl">
					<Table striped hover>
						<thead>
							<tr>
								<th>ID</th>
								<th>Nome</th>
								<th>Nome de usuário</th>
								<th>Tipo</th>
								<th>Detalhes</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{funcionarios.map((func, index) => {
								return (
									<tr key={index}>
										<td>{func.id}</td>
										<td>{func.pessoa.nome}</td>
										<td>{func.usuario.username}</td>
										<td>{func.usuario.grupo.nome}</td>
										<td>
											<button className="btn btn-success" onClick={(e) => this.detalhes(e, func.id)}>
												<i className="fa-solid fa-circle-info">&nbsp;</i>
												detalhes
											</button>
										</td>
										<td>
											<button className="btn btn-danger" onClick={(e) => this.removerSeConfirmado(e, func.id)}>
												<i className="fa-solid fa-x">&nbsp;</i>
												remover
											</button>
										</td>
									</tr>
								);
							})}
						</tbody>
					</Table>
				</div>

				<br />

				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Row>
					<Col className="col-md-6">
						<Card className="p-3">
							<h4>Filtrar funcionarios</h4>
							<Form onSubmit={(e) => this.filtrar(e, true)}>
								<Form.Group className="mb-3">
									<Form.Label>Nome:</Form.Label>
									<Form.Control type="text" ref={this.nomeIni} name="nomeIni" />
								</Form.Group>
								<Form.Group className="mb-3">
									<Form.Label>Nome de usuário:</Form.Label>
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