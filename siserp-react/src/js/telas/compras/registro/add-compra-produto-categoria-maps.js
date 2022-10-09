import React from 'react';
import { Row, Col, Card, Table, Form, Button } from 'react-bootstrap';

import sistema from './../../../logica/sistema';
import MensagemPainel from './../../../componente/mensagem-painel';
import InputDropdown from './../../../componente/input-dropdown';

export default class AddCompraProdutoCategorias extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			categoriasLista: [],
			subcategoriasLista: []
		};
		this.categoria = React.createRef();
		this.subcategoria = React.createRef();
	}

	addCategoria(item) {
		item.preventDefault();

		this.setState({ erroMsg: null });

		let catdesc = this.state.categoria;
		let subcatdesc = this.state.subcategoria;

		let catlenzero = false;
		let subcatlenzero = false;

		if (catdesc) {
			if (catdesc.length === 0)
				catlenzero = true;
		} else {
			catlenzero = true;
		}

		if (subcatdesc) {
			if (subcatdesc.length === 0)
				subcatlenzero = true;
		} else {
			subcatlenzero = true;
		}

		if (catlenzero === true) {
			this.setState({ erroMsg: "A categoria é um campo obrigatório" });
			return;
		}

		if (subcatlenzero === 0) {
			this.setState({ erroMsg: "A subcategoria (valor) é um campo obrigatório" });
			return;
		}

		let jaInserida = false;
		for (let i = 0; jaInserida === false && i < this.props.categoriaMaps.length; i++) {
			let map = this.props.categoriaMaps[i];
			if (map.categoria.toLowerCase() === catdesc.toLowerCase() && map.subcategoria.toLowerCase() === subcatdesc)
				jaInserida = true;
		}

		if (jaInserida === false) {
			this.props.categoriaMaps.push({
				categoria: this.categoria.current.value,
				subcategoria: this.subcategoria.current.value
			});


			this.categoria.current.value = '';
			this.subcategoria.current.value = '';

			this.setState({ categoria: '', subcategoria: '' });
		} else {
			this.setState({ erroMsg: 'Já existe inserida combinação de categoria e subcategoria informadas.' });
		}
	}

	removeCategoria(item, cat, subcat) {
		item.preventDefault();

		let fim = false;
		let size = this.props.categoriaMaps.length;

		for (let i = 0; fim === false && i < size; i++) {
			let catObj = this.props.categoriaMaps[i];
			if (catObj.categoria.toLowerCase() === cat.toLowerCase() && catObj.subcategoria.toLowerCase() === subcat.toLowerCase()) {
				this.props.categoriaMaps.splice(i, 1);
				fim = true;
			}
		}
		this.setState({});
	}

	categoriaOnChange(item) {
		let cat = item;
		if (cat.trim().length === 0)
			cat = "*";

		this.setState({ categoria: item });

		sistema.wsGet('/api/categoriamap/filtra/categoria/limit/' + cat + '/5', (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ categoriasLista: [] });

				for (let i = 0; i < dados.length; i++)
					this.state.categoriasLista.push(dados[i].categoria);

				this.setState({});
			});
		}, this);
	}

	subcategoriaOnChange(item) {
		let cat = this.state.categoria;
		if (cat.trim().length === 0)
			cat = "!";

		let subcat = item;
		if (subcat.trim().length === 0)
			subcat = "*";

		this.setState({ subcategoria: item });

		sistema.wsGet('/api/categoriamap/filtra/subcategoria/limit/' + cat + '/' + subcat + '/5', (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ subcategoriasLista: [] });

				for (let i = 0; i < dados.length; i++)
					this.state.subcategoriasLista.push(dados[i].subcategoria);

				this.setState({});
			});
		}, this);
	}

	render() {
		const { erroMsg, categoriasLista, subcategoriasLista } = this.state;

		return (
			<div>
				<h4 className="text-center">Lista de categorias</h4>
				<div className="tbl-pnl-pequeno">
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Categoria</th>
								<th>Subcategoria</th>
								<th>Remover</th>
							</tr>
						</thead>
						<tbody>
							{this.props.categoriaMaps.map((map, index) => {
								return (
									<tr key={index}>
										<td>{map.categoria}</td>
										<td>{map.subcategoria}</td>
										<td>
											<button className="btn btn-danger" onClick={(item) => this.removeCategoria(item, map.categoria, map.subcategoria)}>
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

				<Card className="p-3 my-2">
					<MensagemPainel cor="danger" msg={erroMsg} />

					<Form.Group className="my-2">
						<Row>
							<Col className="col-sm-4">
								<Form.Label>Categoria</Form.Label>
								<InputDropdown referencia={this.categoria} itens={categoriasLista} carregaItens={(item) => this.categoriaOnChange(item)} />
							</Col>
							<Col className="col-sm-4">
								<Form.Label>Subcategoria (Valor)</Form.Label>
								<InputDropdown referencia={this.subcategoria} itens={subcategoriasLista} carregaItens={(item) => this.subcategoriaOnChange(item)} />
							</Col>
							<Col className="col-sm-4">
								<Form.Label>+</Form.Label>
								<br />
								<Button variant="primary" onClick={(item) => this.addCategoria(item)}>
									<i className="fa-solid fa-circle-plus">&nbsp;</i>
									Adicionar categoria
								</Button>
							</Col>
						</Row>
					</Form.Group>
				</Card>
			</div>
		);
	}

}