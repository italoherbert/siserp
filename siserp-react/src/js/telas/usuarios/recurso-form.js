import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Recursos from './recursos';

export default class RecursoForm extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			titulo: ""
		};

		this.nome = React.createRef();
	}

	componentDidMount() {
		if (this.props.op === 'editar') {
			this.setState({ titulo: "Alteração de recurso" });
			this.carregar();
		} else {
			this.setState({ titulo: "Registro de recurso" });
		}
	}

	carregar() {
		sistema.wsGet('/api/recurso/get/' + this.props.recursoId, (resposta) => {
			resposta.json().then((dados) => {
				this.nome.current.value = dados.nome;
				this.setState({});
			});
		}, this);
	}

	salvar(e) {
		e.preventDefault();

		let url, method;
		if (this.props.op === 'editar') {
			url = "/api/recurso/atualiza/" + this.props.recursoId;
			method = 'PUT';
		} else {
			url = "/api/recurso/registra";
			method = 'POST';
		}

		sistema.wsSave(url, method, {
			"nome": this.nome.current.value
		}, (resposta) => {
			this.setState({ infoMsg: "Recurso salvo com sucesso." });
		}, this);
	}

	paraTelaRecursos() {
		ReactDOM.render(<Recursos />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, titulo } = this.state;

		return (
			<Container>
				<h3>{titulo}</h3>
				<br />
				<Row>
					<Col className="col-md-8">
						<Card className="p-3">
							<Form onSubmit={(e) => this.salvar(e)}>
								<h4 className="card-title">Dados gerais</h4>
								<Row>
									<Col>
										<Form.Group>
											<Form.Label>Nome: </Form.Label>
											<Form.Control type="text" ref={this.nome} name="nome" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group>
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<Button type="submit" variant="primary">
												<i className="fa-solid fa-floppy-disk">&nbsp;</i>
												Salvar
											</Button>
										</Form.Group>
									</Col>
								</Row>
							</Form>
						</Card>
					</Col>
				</Row>

				<br />

				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-outline-primary" onClick={(e) => this.paraTelaRecursos(e)}>
								<i className="fa-solid fa-circle-up">&nbsp;</i>
								Ir para recursos
							</button>
						</Col>
					</Row>
				</Card>

			</Container>
		);
	}

}