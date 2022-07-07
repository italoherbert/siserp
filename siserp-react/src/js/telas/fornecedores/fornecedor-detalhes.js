import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FornecedorForm from './fornecedor-form';
import Fornecedores from './fornecedores';

export default class FornecedorDetalhes extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			fornecedor: {}
		};
	}

	componentDidMount() {
		sistema.wsGet('/api/fornecedor/get/' + this.props.fornecedorId, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ fornecedor: dados });
			});
		}, this);
	}

	editar(e) {
		e.preventDefault();

		ReactDOM.render(
			<FornecedorForm op="editar" fornecedorId={this.props.fornecedorId} />, sistema.paginaElemento());
	}

	paraTelaFornecedores() {
		ReactDOM.render(<Fornecedores />, sistema.paginaElemento());
	}

	render() {
		const { fornecedor, erroMsg, infoMsg } = this.state;

		return (
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h3>Dados do fornecedor</h3>
						<br />

						<Card className="p-3">
							<h4>Dados gerais</h4>

							<div className="display-inline">
								<span className="text-dark font-weight-bold">Empresa: </span>
								<span className="text-primary">{fornecedor.empresa}</span>
							</div>
						</Card>

						<br />

						<Card className="p-3">
							<MensagemPainel cor="danger" msg={erroMsg} />
							<MensagemPainel cor="primary" msg={infoMsg} />

							<Form>
								<Button variant="primary" onClick={(e) => this.editar(e)}>
									<i className="fa-solid fa-edit">&nbsp;</i>
									Editar fornecedor
								</Button>
								<br />
								<br />
								<button className="btn btn-outline-primary" onClick={(e) => this.paraTelaFornecedores(e)}>
									<i className="fa-solid fa-circle-up">&nbsp;</i>
									Ir para fornecedores
								</button>
							</Form>
						</Card>
					</Col>
				</Row>
			</Container>
		);
	}

}