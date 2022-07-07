import React from 'react';
import ReactDOM from 'react-dom';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';

import sistema from './../../logica/sistema';
import MensagemPainel from './../../componente/mensagem-painel';

import ProdutoForm from './produto-form';
import Produtos from './produtos';

export default class ProdutoDetalhes extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			erroMsg: null,
			primaryMsg: null,
			produto: { categoriaMaps: [] }
		};
	}

	componentDidMount() {
		sistema.wsGet("/api/produto/get/" + this.props.produtoId, (resposta) => {
			resposta.json().then((dados) => {
				this.setState({ produto: dados });
			});
		}, this);
	}

	editar(e) {
		e.preventDefault();

		ReactDOM.render(
			<ProdutoForm op="editar" titulo="Altere a produto" produtoId={this.props.produtoId} />, sistema.paginaElemento());
	}

	paraTelaProdutos() {
		ReactDOM.render(<Produtos />, sistema.paginaElemento());
	}

	render() {
		const { produto, erroMsg, primaryMsg } = this.state;

		return (
			<Container>
				<Row>
					<Col className="col-md-2"></Col>
					<Col className="col-md-8">
						<h3>Dados do produto</h3>

						<br />

						<Card className="p-3">
							<Row className="p-2">
								<Col>
									<div className="display-inline h6">
										<span className="text-dark">Descrição: </span>
										<span className="text-danger">{produto.descricao}</span>
									</div>
								</Col>
								<Col>
									<div className="display-inline h6">
										<span className="text-dark">Codigo barras: </span>
										<span className="text-danger">{produto.codigoBarras}</span>
									</div>
								</Col>
							</Row>


							<Row className="p-2">
								<Col>
									<div className="display-inline h6">
										<span className="text-dark font-weight-bold">Preço compra: </span>
										<span className="text-danger">{produto.precoUnitCompra}</span>
									</div>
								</Col>
								<Col>
									<div className="display-inline h6">
										<span className="text-dark">Preço venda: </span>
										<span className="text-danger">{produto.precoUnitVenda}</span>
									</div>
								</Col>
							</Row>

							<Row className="p-2">
								<Col>
									<div className="display-inline h6">
										<span className="text-dark">Unidade: </span>
										<span className="text-danger">{produto.unidade}</span>
									</div>
								</Col>
							</Row>
							<Row className="p-2">
								<Col>
									<div className="display-inline h6">
										<span className="text-dark">Categorias: &nbsp;</span>
										<select className="form-select display-inline w-50">
											{produto.categoriaMaps.map((map, index2) => {
												return (
													<option key={index2} value={map.subcategoria}>{map.categoria} - {map.subcategoria}</option>
												)
											})}
										</select>
									</div>
								</Col>
							</Row>

							<br />
							<Row>
								<Col>
									<Form>
										<Button type="submit" variant="primary" onClick={(e) => this.editar(e)}>
											<i className="fa-solid fa-edit">&nbsp;</i>
											Editar produto
										</Button>
										<br />
										<br />
										<button className="btn btn-outline-primary" onClick={(e) => this.paraTelaProdutos(e)}>
											<i className="fa-solid fa-circle-up">&nbsp;</i>
											Ir para produtos
										</button>
									</Form>
								</Col>
							</Row>
						</Card>
					</Col>
				</Row>

				<br />

				<MensagemPainel cor="danger" msg={erroMsg} />
				<MensagemPainel cor="primary" msg={primaryMsg} />
			</Container>
		);
	}

}