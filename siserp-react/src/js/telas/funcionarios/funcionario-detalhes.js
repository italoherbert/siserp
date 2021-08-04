import React from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

import FuncionarioForm from './funcionario-form';

export default class FuncionarioDetalhes extends React.Component {

	constructor( props ) {
		super( props );
		
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			funcionario : {
				pessoa : {
					endereco : {}
				},
				usuario : {}
			} 
		};
	}
	
	componentDidMount() {
		let funcId = this.props.funcId;
		
		fetch( "/api/funcionario/get/"+funcId, {
			method : "GET",			
			headers : { 
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {
			if ( resposta.status === 200 ) {						
				resposta.json().then( (dados) => {											
					this.setState( { funcionario : dados } );
				} );		
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}			 
		} );
	}
	
	editar( e ) {
		e.preventDefault();
					
		ReactDOM.render( 
			<FuncionarioForm op="editar"
				funcId={this.props.funcId} 
				nome={this.state.funcionario.pessoa.nome} 
				telefone={this.state.funcionario.pessoa.telefone} 
				email={this.state.funcionario.pessoa.email} 
				ender={this.state.funcionario.pessoa.endereco.ender} 
				numero={this.state.funcionario.pessoa.endereco.numero} 
				bairro={this.state.funcionario.pessoa.endereco.bairro} 
				logradouro={this.state.funcionario.pessoa.endereco.logradouro} 
				cidade={this.state.funcionario.pessoa.endereco.cidade} 
				uf={this.state.funcionario.pessoa.endereco.uf} 
				username={this.state.funcionario.usuario.username} 
				tipo={this.state.funcionario.usuario.tipo}
			/>,
			sistema.paginaElemento() );
	}
	
	render() {
		const { funcionario, erroMsg, infoMsg } = this.state;
		
		return( 
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="text-center">Dados do funcionário</h4>																
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados gerais</h4>
								
								<span className="text-dark font-weight-bold">Nome: </span>
								<span className="text-info">{funcionario.pessoa.nome}</span>
								<br />
								
								<span className="text-dark font-weight-bold">Telefone: </span>
								<span className="text-info">{funcionario.pessoa.telefone}</span>
								<br />
								
								<span className="text-dark font-weight-bold">E-Mail: </span>
								<span className="text-info">{funcionario.pessoa.email}</span>
								<br />																
							</div>									
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Endereço</h4>
								
								<span className="text-dark font-weight-bold">Ender: </span>
								<span className="text-info">{funcionario.pessoa.endereco.ender}</span>
								<br />
								
								<span className="text-dark font-weight-bold">Numero: </span>
								<span className="text-info">{funcionario.pessoa.endereco.numero}</span>
								<br />
								
								<span className="text-dark font-weight-bold">Logradouro: </span>
								<span className="text-info">{funcionario.pessoa.endereco.logradouro}</span>
								<br />
								
								<span className="text-dark font-weight-bold">Bairro: </span>
								<span className="text-info">{funcionario.pessoa.endereco.bairro}</span>
								<br />
								
								<span className="text-dark font-weight-bold">Cidade: </span>
								<span className="text-info">{funcionario.pessoa.endereco.cidade}</span>
								<br />
								
								<span className="text-dark font-weight-bold">UF: </span>
								<span className="text-info">{funcionario.pessoa.endereco.uf}</span>
								<br />																
							</div>									
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<h4 className="card-title">Dados usuario</h4>
								
								<span className="text-dark font-weight-bold">Username: </span>
								<span className="text-info">{funcionario.usuario.username}</span>
								<br />
																
								<span className="text-dark font-weight-bold">Tipo: </span>
								<span className="text-info">{funcionario.usuario.tipo}</span>
								<br />																
							</div>									
						</div>
						
						<br />
						
						<div className="card border-1">								
							<div className="card-body">
								<button className="btn btn-link" onClick={(e) => this.editar( e )}>Editar funcionario</button>
													
								<MensagemPainel cor="danger" msg={erroMsg} />
								<MensagemPainel cor="primary" msg={infoMsg} />																																								
							</div>
						</div>
												
					</div>
				</div>															
			</div>
		);
	}

}