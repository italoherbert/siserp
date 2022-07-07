import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import DatePicker from 'react-datepicker';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import CaixaFluxoDia from './caixa-fluxo-dia';

export default class CaixaFluxo extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			dataIni: new Date(),
			dataFim: new Date(),
			balancosDiarios: []
		};
	}

	componentDidMount() {
		this.filtrar(null);
	}

	filtrar(e) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/caixa/gera/balancos/diarios", {
			"dataIni": sistema.formataData(this.state.dataIni),
			"dataFim": sistema.formataData(this.state.dataFim)
		}, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ balancosDiarios: dados });

				if (dados.length === 0)
					this.setState({ infoMsg: "Nenhum fluxo de caixa encontrado pelos critérios de busca informados" });
			});
		}, this);
	}

	dataIniOnChange(data) {
		this.setState({ dataIni: data });
	}

	dataFimOnChange(data) {
		this.setState({ dataFim: data });
	}

	visualizar(e, dataAbertura) {
		ReactDOM.render(<CaixaFluxoDia dataDia={dataAbertura} />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, balancosDiarios, dataIni, dataFim } = this.state;

		return (
			<Container>
				<Row>
					<Col>
						<h3>Fluxo de caixa</h3>
						<br />
						<div className="tbl-pnl">
							<Table striped hover>
								<thead>
									<tr>
										<th>Data de abertura</th>
										<th>Entrada</th>
										<th>Saida</th>
										<th>Saldo</th>
										<th>Visualizar</th>
									</tr>
								</thead>
								<tbody>
									{balancosDiarios.map((balancoDia, index) => {
										return (
											<tr key={index}>
												<td>{balancoDia.dataAbertura}</td>
												<td>{sistema.formataReal(balancoDia.credito)}</td>
												<td>{sistema.formataReal(balancoDia.debito)}</td>
												<td>{sistema.formataReal(balancoDia.saldo)}</td>
												<td>
													<button className="btn btn-success" onClick={(e) => this.visualizar(e, balancoDia.dataAbertura)}>
														<i className="fa-solid fa-search">&nbsp;</i>
														Visualizar
													</button>
												</td>
											</tr>
										)
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
					<Col className="col-sm-10">
						<Card className="p-3">
							<h4>Filtrar</h4>
							<Form onSubmit={(e) => this.filtrar(e)}>
								<Row>
									<Col>
										<Form.Group className="pb-2">
											<Form.Label>Data de início: </Form.Label>
											<br />
											<DatePicker selected={dataIni}
												onChange={(date) => this.dataIniOnChange(date)}
												locale="pt"
												name="dataIni"
												dateFormat="dd/MM/yyyy" className="form-control" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group className="pb-2">
											<Form.Label>Data de fim: </Form.Label>
											<br />
											<DatePicker selected={dataFim}
												onChange={(date) => this.dateFimOnChange(date)}
												locale="pt"
												name="dataFim"
												minDate={dataIni}
												dateFormat="dd/MM/yyyy" className="form-control" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group className="pb-2">
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<Button type="submit" variant="primary">
												<i className="fa-solid fa-filter">&nbsp;</i>
												Filtrar
											</Button>
										</Form.Group>
									</Col>
								</Row>
							</Form>
						</Card>
					</Col>
				</Row>
			</Container >
		)
	}

}