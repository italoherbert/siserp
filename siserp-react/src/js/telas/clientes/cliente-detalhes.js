import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import ClienteForm from './cliente-form';
import Clientes from './clientes';

export default class ClienteDetalhes extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			primaryMsg: null,
			cliente: {
				pessoa: {
					endereco: {}
				}
			}
		};
	}

	componentDidMount() {
		sistema.wsGet("/api/cliente/get/" + this.props.clienteId, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ cliente: dados });
			});
		}, this);
	}

	editar(e) {
		e.preventDefault();

		ReactDOM.render(
			<ClienteForm op="editar" clienteId={this.props.clienteId} />,
			sistema.paginaElemento());
	}

	paraTelaClientes() {
		ReactDOM.render(<Clientes />, sistema.paginaElemento());
	}

	render() {
		const { cliente, erroMsg, primaryMsg } = this.state;

		return (
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h3>Dados do cliente</h3>

						<br />

						<Card className="p-3">
							<h4>Dados gerais</h4>

							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Nome: </span>
									<span className="text-primary">{cliente.pessoa.nome}</span>
								</Col>
							</Row>

							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Telefone: </span>
									<span className="text-primary">{cliente.pessoa.telefone}</span>
								</Col>
							</Row>

							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">E-Mail: </span>
									<span className="text-primary">{cliente.pessoa.email}</span>
								</Col>
							</Row>
						</Card>

						<br />

						<Card className="p-3">
							<h4 className="card-title">Endereço</h4>

							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Ender: </span>
									<span className="text-primary">{cliente.pessoa.endereco.ender}</span>
								</Col>
							</Row>

							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Numero: </span>
									<span className="text-primary">{cliente.pessoa.endereco.numero}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">Logradouro: </span>
									<span className="text-primary">{cliente.pessoa.endereco.logradouro}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">Bairro: </span>
									<span className="text-primary">{cliente.pessoa.endereco.bairro}</span>
								</Col>
							</Row>
							<Row className="mb-2">
								<Col>
									<span className="text-dark font-weight-bold">Cidade: </span>
									<span className="text-primary">{cliente.pessoa.endereco.cidade}</span>
								</Col>
								<Col>
									<span className="text-dark font-weight-bold">UF: </span>
									<span className="text-primary">{cliente.pessoa.endereco.uf}</span>
								</Col>
							</Row>
						</Card>

						<br />

						<Card className="p-3">
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={primaryMsg} />

							<Row>
								<Col>
									<Form>
										<Button variant="primary" onClick={(e) => this.editar(e)}>
											<i className="fa-solid fa-edit">&nbsp;</i>
											Editar cliente
										</Button>
									</Form>
									<br />
									<button className="btn btn-outline-primary" onClick={(e) => this.paraTelaClientes(e)}>
										<i className="fa-solid fa-circle-up">&nbsp;</i>
										Ir para clientes
									</button>
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>
			</Container>
		);
	}

}