import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Table, Button } from 'react-bootstrap';
import Modal from 'react-bootstrap/Modal';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import CategoriaMapDetalhes from './categoria-map-detalhes';
import CategoriaMapForm from './categoria-map-form';

export default class CategoriaMaps extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			addErroMsg: null,
			addInfoMsg: null,
			erroMsg: null,
			infoMsg: null,
			categoriaMaps: [],

			remocaoModalVisivel: false,
			remocaoModalOkFunc: () => { },
			remocaoModalCancelaFunc: () => { }
		};

		this.categoriaIni = React.createRef();
		this.subcategoriaIni = React.createRef();
	}

	componentDidMount() {
		this.categoriaIni.current.value = "*";
		this.subcategoriaIni.current.value = "*";

		this.filtrar(null, true);
	}

	filtrar(e, filtrarBTClicado) {
		if (e != null)
			e.preventDefault();

		sistema.wsPost("/api/categoriamap/filtra", {
			"categoriaIni": this.categoriaIni.current.value,
			"subcategoriaIni": this.subcategoriaIni.current.value
		}, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ categoriaMaps: dados });

				if (dados.length === 0 && filtrarBTClicado === true)
					this.setState({ infoMsg: "Nenhuma categoria encontrada pelos critérios de busca informados!" });
			});
		}, this);
	}

	detalhes(e, id) {
		e.preventDefault();

		ReactDOM.render(<CategoriaMapDetalhes categoriaMapId={id} />, sistema.paginaElemento());
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

		sistema.wsDelete("/api/categoriamap/deleta/" + id, (resposta) => {
			this.filtrar(null, false);
			this.setState({ infoMsg: "Mapeamento de categoria removida com êxito!" });
		}, this);
	}

	render() {
		const { erroMsg, infoMsg, categoriaMaps, remocaoModalVisivel, remocaoModalCancelaFunc, remocaoModalOkFunc } = this.state;

		return (
			<Container>
				<Modal show={remocaoModalVisivel}>
					<Modal.Header>
						<Modal.Title>Remoção de categoria</Modal.Title>
					</Modal.Header>
					<Modal.Body>Tem certeza que deseja remover a categoria selecionada?</Modal.Body>
					<Modal.Footer>
						<Form>
							<Button variant="primary" onClick={(e) => remocaoModalCancelaFunc()}>Cancelar</Button>
							<Button variant="danger" className="mx-2" onClick={(e) => remocaoModalOkFunc()}>Remover</Button>
						</Form>
					</Modal.Footer>
				</Modal>

				<h3>Lista de Categorias</h3>

				<br />

				<div className="tbl-pnl">
					<Table striped hover>
						<thead>
							<tr>
								<th>ID</th>
								<th>Categoria</th>
								<th>Subcategoria</th>
								<th>Detalhes</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{categoriaMaps.map((map, index) => {
								return (
									<tr key={index}>
										<td>{map.id}</td>
										<td>{map.categoria}</td>
										<td>{map.subcategoria}</td>
										<td>
											<button className="btn btn-success" onClick={(e) => this.detalhes(e, map.id)}>
												<i className="fa-solid fa-circle-info">&nbsp;</i>
												detalhes
											</button>
										</td>
										<td>
											<button className="btn btn-danger" onClick={(e) => this.removerSeConfirmado(e, map.id)}>
												<i className="fa-solid fa-x">&nbsp;</i>
												remover
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

				<Row>
					<Col>
						<Card className="p-3">
							<h4>Filtro de categorias</h4>
							<Form onSubmit={(e) => this.filtrar(e, true)}>
								<Form.Group className="mb-2">
									<Form.Label>Categoria:</Form.Label>
									<Form.Control type="text" ref={this.categoriaIni} name="categoriaIni" />
								</Form.Group>

								<Form.Group className="mb-2">
									<Form.Label>Subcategoria:</Form.Label>
									<Form.Control type="text" ref={this.subcategoriaIni} name="subcategoriaIni" />
								</Form.Group>

								<Button type="submit" variant="primary">
									<i className="fa-solid fa-filter">&nbsp;</i>
									Filtrar
								</Button>
							</Form>
						</Card>
					</Col>
					<Col>
						<CategoriaMapForm
							op="cadastrar"
							titulo="Cadastre nova categoria"
							registrou={() => this.filtrar(null, false)} />
					</Col>
				</Row>

			</Container>
		);
	}

}