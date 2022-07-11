
import React from 'react';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import DatePicker from 'react-datepicker';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class ContasReceber extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,

			dataIni: new Date(),
			dataFim: new Date(),

			contasObj: { contas: [] },
			debitoPeriodo: 0,
			debitoTotal: 0
		};

		this.incluirContasPagas = React.createRef();
		this.incluirCliente = React.createRef();
		this.clienteNomeIni = React.createRef();
	}

	componentDidMount() {
		this.filtrar(null, false);

		this.setState({ dataFim: new Date().getTime() + 365 * 24 * 60 * 60 * 1000 });
	}


	filtrar(e, filtrarBTClicado) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/conta/receber/filtra", {
			"incluirPagas": this.incluirContasPagas.current.checked,
			"incluirCliente": this.incluirCliente.current.checked,
			"clienteNomeIni": this.clienteNomeIni.current.value,
			"dataIni": sistema.formataData(this.state.dataIni),
			"dataFim": sistema.formataData(this.state.dataFim)
		}, (resposta) => {
			resposta.json().then((dados) => {
				let contasObj = dados;

				if (dados.length === 0 && filtrarBTClicado === true)
					this.setState({ infoMsg: "Nenhuma conta encontrada pelos critérios de filtro informados!" });

				this.setState({ contasObj: contasObj });
			});
		}, this);
	}

	restauraDebito(e, parcelaId) {
		e.preventDefault();

		sistema.wsPost('/api/conta/receber/restauradebito/' + parcelaId, {}, (resposta) => {
			this.filtrar(e, false);
		}, this);
	}

	restauraRecebimento(e, parcelaId) {
		e.preventDefault();

		sistema.wsPost('/api/conta/receber/restaurarecebimento/' + parcelaId, {}, (resposta) => {
			this.filtrar(e, false);
		}, this);
	}

	changeDataIni(date) {
		this.setState({ dataIni: date });
	}

	changeDataFim(date) {
		this.setState({ dataFim: date });
	}

	render() {
		const { erroMsg, infoMsg, contasObj, dataIni, dataFim } = this.state;

		return (
			<Container>
				<h4 className="text-center">Lista de contas a receber</h4>
				<div className="tbl-pnl">
					<Table striped hover>
						<thead>
							<tr>
								<th>Data de pagamento</th>
								<th>Data de vencimento</th>
								<th>Cliente</th>
								<th>Valor</th>
								<th>Débito</th>
								<th>Restaurar débito</th>
							</tr>
						</thead>
						<tbody>
							{contasObj.contas.map((conta, index) => {
								return (
									<tr key={index}>
										<td>{conta.parcela.dataPagamento}</td>
										<td>{conta.parcela.dataVencimento}</td>
										<td>{conta.clienteNome}</td>
										<td>{sistema.formataReal(conta.parcela.valor)}</td>
										<td>{sistema.formataReal(conta.parcela.debito)}</td>
										<td>
											<button type="button" className="btn btn-primary"
												onClick={conta.parcela.debitoRestaurado === 'true' ?
													(e) => this.restauraRecebimento(e, conta.parcela.id) :
													(e) => this.restauraDebito(e, conta.parcela.id)}>
												{conta.parcela.debitoRestaurado === 'true' ? 'desfazer' : 'restaurar'}
											</button>
										</td>
									</tr>
								);
							})}
						</tbody>
					</Table>
				</div>

				<br />

				<Card className="p-3">
					<Form.Group style={{ fontSize: '1.6em' }}>
						<Row>
							<Col>
								<Form.Label>Valor a receber: &nbsp;<span className="text-danger">{sistema.formataReal(contasObj.totalPeriodo)}</span></Form.Label>
							</Col>
							<Col>
								<Form.Label>Valor a receber total: &nbsp;<span className="text-danger">{sistema.formataReal(contasObj.totalCompleto)}</span></Form.Label>
							</Col>
						</Row>
					</Form.Group>
				</Card>

				<br />

				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Row>
					<Col className="col-md-8">
						<Card className="p-3">
							<h4>Filtrar contas receber</h4>
							<Form onSubmit={(e) => this.filtrar(e, true)}>
								<Row>
									<Col>
										<Form.Group className="mb-2">
											<Form.Label>Data de início: </Form.Label>
											<br />
											<DatePicker selected={dataIni}
												onChange={(date) => this.changeDataIni(date)}
												locale="pt"
												name="dataIni"
												dateFormat="dd/MM/yyyy" className="form-control" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group className="mb-2">
											<Form.Label>Data de fim: </Form.Label>
											<br />
											<DatePicker selected={dataFim}
												onChange={(date) => this.changeDataFim(date)}
												locale="pt"
												name="dataFim"
												minDate={dataIni}
												dateFormat="dd/MM/yyyy" className="form-control" />
										</Form.Group>
									</Col>
									<Col>
										<Form.Group className="mb-2">
											<Form.Label>&nbsp;</Form.Label>
											<br />
											<input className="my-2" type="checkbox" ref={this.incluirContasPagas} />
											&nbsp; Incluir contas quitadas
										</Form.Group>
									</Col>
								</Row>
								<Row>
									<Col>
										<Form.Group className="mb-2">
											<Row>
												<Col>
													<Form.Label>Cliente: </Form.Label>
													<Form.Control type="text" ref={this.clienteNomeIni} name="clienteNomeIni" />

													<input className="my-2" type="checkbox" ref={this.incluirCliente} />Incluir cliente no filtro
												</Col>
											</Row>
										</Form.Group>
									</Col>
								</Row>

								<Button type="submit" variant="primary">Filtrar</Button>
							</Form>
						</Card>
					</Col>
				</Row>
			</Container>
		);
	}

}