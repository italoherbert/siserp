import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FornecedorDetalhes from './fornecedor-detalhes';
import FornecedorForm from './fornecedor-form';

export default class Fornecedores extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			fornecedores: [],

			remocaoModalVisivel: false,
			remocaoModalOkFunc: () => { },
			remocaoModalCancelaFunc: () => { }
		};

		this.empresaIni = React.createRef();
	}

	componentDidMount() {
		this.empresaIni.current.value = "*";

		this.filtrar(null, false);
	}

	filtrar(e, filtrarBTClicado) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/fornecedor/filtra", {
			"empresaIni": this.empresaIni.current.value,
		}, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ fornecedores: dados });

				if (dados.length === 0 && filtrarBTClicado === true)
					this.setState({ infoMsg: "Nenhum fornecedor registrado!" });
			});
		}, this);
	}

	detalhes(e, id) {
		e.preventDefault();

		ReactDOM.render(<FornecedorDetalhes fornecedorId={id} />, sistema.paginaElemento());
	}

	removerSeConfirmado(e, id) {
		this.setState({
			remocaoModalVisivel: true,
			remocaoModalOkFunc: () => {
				this.setState({ remocaoModalVisivel: false });
				this.remover(e, id);
			},
			remocaoModalCancelaFunc: () => {
				this.setState({ remocaoModalVisivel: false });
			}
		})
	}

	remover(e, id) {
		e.preventDefault();

		sistema.wsDelete("/api/fornecedor/deleta/" + id, (resposta) => {
			this.filtrar(null, false);
			this.setState({ infoMsg: "Fornecedor removido com êxito!" });
		}, this);
	}

	paraTelaRegistro() {
		ReactDOM.render(<FornecedorForm op="cadastrar" />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, fornecedores, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;

		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de fornecedor</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover o fornecedor selecionado?</Modal.Body>
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
								Registrar novo fornecedor
							</Button>
						</Form>
					</Col>
				</Row>

				<h3>Lista de Fornecedores</h3>
				<br />

				<div className="tbl-pnl">
					<Table striped hover>
						<thead>
							<tr>
								<th>ID</th>
								<th>Nome da empresa</th>
								<th>Detalhes</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{fornecedores.map((fornecedor, index) => {
								return (
									<tr key={index}>
										<td>{fornecedor.id}</td>
										<td>{fornecedor.empresa}</td>
										<td>
											<button className="btn btn-success" onClick={(e) => this.detalhes(e, fornecedor.id)}>
												<i className="fa-solid fa-circle-info">&nbsp;</i>
												Detalhes
											</button>
										</td>
										<td>
											<button className="btn btn-danger" onClick={(e) => this.removerSeConfirmado(e, fornecedor.id)}>
												<i className="fa-solid fa-x">&nbsp;</i>
												Remover
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
					<Col className="col-md-8">
						<Card className="p-3">
							<h4>Filtrar fornecedores</h4>

							<Form onSubmit={(e) => this.filtrar(e, true)}>
								<Form.Group className="mb-3">
									<Form.Label>Empresa:</Form.Label>
									<Form.Control type="text" ref={this.empresaIni} name="empresaIni" />
								</Form.Group>

								<Button type="submit" variant="primary" className="mb-3">
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