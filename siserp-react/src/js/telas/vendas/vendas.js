import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';
import DatePicker from 'react-datepicker';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import VendaRegistro from './venda-registro';
import VendaDetalhes from './venda-detalhes';

import VendasPagamento from './vendas-pagamento';

export default class Vendas extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			vendas: [],
			dataIni: new Date(),
			dataFim: new Date(),

			remocaoModalVisivel: false,
			remocaoModalOkFunc: () => { },
			remocaoModalCancelaFunc: () => { }
		};

		this.incluirCliente = React.createRef();
		this.clienteNomeIni = React.createRef();
		this.incluirVendasPagas = React.createRef();
	}

	componentDidMount() {
		this.incluirCliente.current.checked = false;
		this.incluirVendasPagas.current.checked = true;
		this.filtrar(null, false);
	}

	filtrar(e, filtrarBTClicado) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/venda/filtra", {
			"dataIni": sistema.formataData(this.state.dataIni),
			"dataFim": sistema.formataData(this.state.dataFim),
			"incluirCliente": this.incluirCliente.current.checked,
			"clienteNomeIni": this.clienteNomeIni.current.value,
			"incluirPagas": this.incluirVendasPagas.current.checked
		}, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ vendas: dados });
				if (dados.length === 0 && filtrarBTClicado === true)
					this.setState({ infoMsg: "Nenhuma venda encontrada pelos critérios de busca informados!" });
			});
		}, this);
	}

	changeDataIni(date) {
		this.setState({ dataIni: date });
	}

	changeDataFim(date) {
		this.setState({ dataFim: date });
	}

	detalhes(e, vendaId) {
		ReactDOM.render(<VendaDetalhes vendaId={vendaId} />, sistema.paginaElemento());
	}

	removerSeConfirmado(e, vendaId) {
		this.setState({
			remocaoModalVisivel: true,
			remocaoModalOkFunc: () => {
				this.setState({ remocaoModalVisivel: false });
				this.remover(e, vendaId);
			},
			remocaoModalCancelaFunc: () => {
				this.setState({ remocaoModalVisivel: false });
			}
		})
	}

	remover(e, vendaId, instance) {
		e.preventDefault();

		sistema.wsDelete('/api/venda/deleta/' + vendaId, (resposta) => {
			this.filtrar(null, false);
			this.setState({ infoMsg: 'Venda deletada com êxito.' });
		}, this);
	}

	paraTelaPagamentos(e) {
		ReactDOM.render(<VendasPagamento />, sistema.paginaElemento());
	}

	paraRegistroForm(e) {
		ReactDOM.render(<VendaRegistro />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, vendas, dataIni, dataFim, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;

		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de venda</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						Tem certeza que deseja remover a venda selecionada? <br />
						<br />
						<b>Atenção:</b> Ao escolher remover a venda, esteja ciente que será
						atualizado o estoque com a adição das unidades de produto componentes
						da venda!
					</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc()}>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc()}>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>

				{sistema.usuario.grupo.nome === 'CAIXA' && (
					<Row>
						<Col>
							<Form>
								<Button variant="primary" className="float-start" onClick={(e) => this.paraTelaPagamentos(e)}>
									<i className="fa-solid fa-pager">&nbsp;</i>
									Efetue um pagamento
								</Button>
								<Button variant="primary" className="float-end" onClick={(e) => this.paraRegistroForm(e)}>
									<i className="fa-solid fa-file">&nbsp;</i>
									Registre uma nova venda
								</Button>
							</Form>
						</Col>
					</Row>
				)}

				<h3 className="text-center">Lista de Vendas</h3>
				<div className="tbl-pnl">
					<Table striped hover>
						<thead>
							<tr>
								<th>ID</th>
								<th>Data venda</th>
								<th>Cliente</th>
								<th>Total</th>
								<th>Debito</th>
								<th>Detalhes</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{vendas.map((venda, index) => {
								return (
									<tr key={index}>
										<td>{venda.id}</td>
										<td>{venda.dataVenda}</td>
										<td>{venda.cliente.pessoa.nome}</td>
										<td>{sistema.formataReal(venda.subtotal * (1.0 - ((sistema.paraFloat(venda.desconto) / 100.0))))}</td>
										<td>{sistema.formataReal(venda.debito)}</td>
										<td>
											<button className="btn btn-success" onClick={(e) => this.detalhes(e, venda.id)}>
												<i className="fa-solid fa-circle-info">&nbsp;</i>
												Detalhes
											</button>
										</td>
										<td>
											<button className="btn btn-danger" onClick={(e) => this.removerSeConfirmado(e, venda.id)}>
												<i className="fa-solid fa-x">&nbsp;</i>
												Remover
											</button>
										</td>
									</tr>
								)
							})}
						</tbody>
					</Table>
				</div>
				<br />

				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={infoMsg} />

				<Card className="p-3">
					<h4>Filtrar vendas</h4>
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
									<input className="my-2" type="checkbox" ref={this.incluirVendasPagas} /> &nbsp; Incluir vendas pagas
								</Form.Group>
							</Col>
						</Row>
						<Row>
							<Col className="col-md-6">
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
						<Row>
							<Col>
								<Button type="submit" variant="primary">
									<i className="fa-solid fa-filter">&nbsp;</i>
									Filtrar
								</Button>
							</Col>
						</Row>
					</Form>
				</Card>

			</Container>
		)
	}

}