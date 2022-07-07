import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import Grupos from './grupos';

export default class GrupoFormEdit extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			infoMsg: null,
			grupo: { permissaoGrupos: [] }
		};

		this.nome = React.createRef();
	}

	componentDidMount() {
		this.carregar();
	}

	carregar() {
		sistema.wsGet('/api/usuario/grupo/get/' + this.props.grupoId, (resposta) => {
			resposta.json().then((dados) => {
				this.nome.current.value = dados.nome;
				this.setState({ grupo: dados });
			});
		}, this);
	}

	salvar(e) {
		e.preventDefault();

		sistema.wsPut("/api/usuario/grupo/atualiza/" + this.props.grupoId, {
			"nome": this.nome.current.value
		}, (resposta) => {
			this.setState({ infoMsg: "Grupo salvo com sucesso." });
		}, this);
	}

	sincronizarRecursos(e) {
		e.preventDefault();

		sistema.wsPost('/api/usuario/grupo/recursos/sincroniza/' + this.props.grupoId, {
		}, (resposta) => {
			this.carregar();
			this.setState({ infoMsg: "Recursos sincronizados com sucesso." });
		}, this);
	}

	permissaoLeituraOnChange(e, id) {
		this.permissaoOnChange(e, id, 'LEITURA');
	}

	permissaoEscritaOnChange(e, id) {
		this.permissaoOnChange(e, id, 'ESCRITA');
	}

	permissaoRemocaoOnChange(e, id) {
		this.permissaoOnChange(e, id, 'REMOCAO');
	}

	permissaoOnChange(e, id, tipo) {
		e.preventDefault();

		sistema.wsPatch('/api/permissao/salva/' + id, {
			tipo: tipo,
			valor: e.target.checked
		}, (resposta) => {
			this.carregar();
		}, this);
	}

	paraTelaGrupos() {
		ReactDOM.render(<Grupos />, sistema.paginaElemento());
	}

	render() {
		const { erroMsg, infoMsg, grupo } = this.state;

		return (
			<Container>
				<h3>Registro de grupos</h3>
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

				<Row>
					<Col>
						<Form>
							<Button variant="primary" className="float-end" onClick={(e) => this.sincronizarRecursos(e)}>
								<i className="fa-solid fa-rotate">&nbsp;</i>
								Sincronizar Recursos
							</Button>
						</Form>
					</Col>
				</Row>

				<Row>
					<Col>
						<h4 className="text-center">Permissões sobre recursos</h4>

						<div className="tbl-pnl">
							<Table striped bordered hover>
								<thead>
									<tr>
										<th>Recurso</th>
										<th>Leitura</th>
										<th>Escrita</th>
										<th>Remoção</th>
									</tr>
								</thead>
								<tbody>
									{grupo.permissaoGrupos.map((item, index) => {
										return (
											<tr key={index}>
												<td>{item.recurso}</td>
												<td><input type="checkbox" checked={item.leitura === "true"} onChange={(e) => this.permissaoLeituraOnChange(e, item.id)} /></td>
												<td><input type="checkbox" checked={item.escrita === "true"} onChange={(e) => this.permissaoEscritaOnChange(e, item.id)} /></td>
												<td><input type="checkbox" checked={item.remocao === "true"} onChange={(e) => this.permissaoRemocaoOnChange(e, item.id)} /></td>
											</tr>
										)
									})}
								</tbody>
							</Table>
						</div>
					</Col>
				</Row>

				<br />

				<Card className="p-3">
					<Row>
						<Col>
							<button className="btn btn-outline-primary" onClick={(e) => this.paraTelaGrupos(e)}>
								<i className="fa-solid fa-circle-up">&nbsp;</i>
								Ir para grupos
							</button>
						</Col>
					</Row>
				</Card>

			</Container>
		);
	}

}