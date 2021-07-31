import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class FuncionarioForm extends React.Component {
	
	constructor( props ) {
		super( props );
				
		this.state = { 
			erroMsg : null, 
			infoMsg : null, 
			uTipos : [],
			uTipoSelecionado : null
		};
	}
	
	componentDidMount() {			
		if ( this.props.op == 'editar' ) {				
			this.refs.nome.value = this.props.nome;
			this.refs.telefone.value = this.props.telefone;
			this.refs.email.value = this.props.email;
			
			this.refs.ender.value = this.props.ender;
			this.refs.numero.value = this.props.numero;
			this.refs.logradouro.value = this.props.logradouro;
			this.refs.bairro.value = this.props.bairro;
			this.refs.cidade.value = this.props.cidade;
			this.refs.uf.value = this.props.uf;
			
			this.refs.username.value = this.props.username;
		}
		
		fetch( "/api/usuario/tipos", {
			method : "GET",			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.state.uTipos = dados;
											
				if ( this.props.op == 'editar' )							
					this.state.uTipoSelecionado = this.props.tipo;				
								
				this.setState( this.state )
			} );			 
		} );
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.state.erroMsg = null;
		this.state.infoMsg = null;
		this.setState( this.state );
		
		let url;
		let metodo;
		if ( this.props.op == 'editar' ) {			
			url = "/api/funcionario/atualiza/"+this.props.funcId;
			metodo = 'PUT';									
		} else {
			url = "/api/funcionario/registra";
			metodo = 'POST';
		}
				
		fetch( url, {
			method : metodo,			
			headers : {
				"Content-Type" : "application/json; charset=UTF-8",
				"Authorization" : "Bearer "+sistema.token
			},
			body : JSON.stringify( {
				"pessoa" : {
					"nome" : this.refs.nome.value,
					"telefone" : this.refs.telefone.value,				
					"email" : this.refs.email.value,
					
					"endereco" : {
						"ender" : this.refs.ender.value,
						"numero" : this.refs.numero.value,
						"bairro" : this.refs.bairro.value,
						"cidade" : this.refs.cidade.value,
						"uf" : this.refs.uf.value,
						"logradouro" : this.refs.logradouro.value				
					}
				},
				
				"usuario" : {
					"username" : this.refs.username.value,
					"password" : this.refs.password.value,
					"tipo" : this.refs.tipo.value	
				}
			} )		
		} ).then( (resposta) => {						
			if ( resposta.status == 200 ) {
				this.state.infoMsg = "Funcionario cadastrado com sucesso.";		
				this.setState( this.state );
			} else {
				sistema.trataRespostaNaoOk( resposta, this );
			}
		} );				
	}
	
	render() {
		const { erroMsg, infoMsg, uTipos, uTipoSelecionado } = this.state;
				
		return(
			<div className="container">
				<div className="row">
					<div className="col-md-2"></div>
					<div className="col-md-8">
						<h4 className="card-title text-center">Registro de funcionário</h4>
																
						<form onSubmit={(e) => this.salvar( e ) } className="container">
							<div className="card border-1">								
								<div className="card-body">
									<h4 className="card-title">Dados gerais</h4>
									
									<div className="row">
										<div className="form-group col-sm-12">
											<label className="control-label" for="nome">Nome: </label>
											<input type="text" ref="nome" name="nome" size="50" className="form-control" />
										</div>
									</div>
									<div className="row form-group">
										<div className="col-sm-6">
											<label className="control-label" for="telefone">Telefone: </label>
											<input type="tel" ref="telefone" name="tel" className="form-control" />
										</div>
										<div className="col-sm-6">						
											<label className="control-label" for="email">E-Mail: </label>
											<input type="email" ref="email" name="email" className="form-control" />						
										</div>
									</div>
								</div>
							</div>
							
							<br />
												
							<div className="card border-1">								
								<div className="card-body">
									<h4 className="card-title">Endereço</h4>
									
									<div className="row form-group">
										<div className="col-sm-12">
											<label className="control-label" for="ender">Ender:</label>
											<input type="text" ref="ender" name="ender" className="form-control" />
										</div>
									</div>
									
									<div className="row form-group">
										<div className="col-sm-4">
											<label className="control-label" for="ender">Numero:</label>
											<input type="text" ref="numero" name="numero" size="8" className="form-control" />
										</div>
										<div className="col-sm-4">
											<label className="control-label" for="logradouro">Logradouro:</label>
											<input type="text" ref="logradouro" name="logradouro" size="10" className="form-control" />
										</div>
										<div className="col-sm-4">
											<label className="control-label" for="bairro">Bairro:</label>
											<input type="text" ref="bairro" name="bairro" size="20" className="form-control" />
										</div>
									</div>
																		
									<div className="row form-group">
										<div className="col-sm-8">
											<label for="cidade">Cidade:</label>
											<input type="text" ref="cidade" name="cidade" size="30" className="form-control" />
										</div>
										<div className="col-sm-4">
											<label for="uf">UF:</label>
											<input type="text" ref="uf" name="uf" size="10" className="form-control" />							
										</div>										
									</div>
								</div>
							</div>
							
							<br />
							
							<div className="card border-1">
								<div className="card-body">
									<h4 className="card-title">Usuario:</h4>
									
									<div className="row form-group">
										<div className="col-sm-8">
											<label className="control-label" for="username">Nome de usuário:</label>
											<input type="text" ref="username" name="username" className="form-control" />
										</div>
										
										<div className="col-sm-4">
											<label className="control-label" for="tipo">Tipos:</label>
											<select name="tipo" ref="tipo" value={uTipoSelecionado} className="form-control">
												<option key="0" value="NONE">Selecione um tipo!</option>
												{ uTipos.map( (item, i) => {
													return <option key={i} value={item}>{item}</option>;
												} )	}
											</select>
										</div>																				
									</div>
									<div className="row form-group">
										<div className="col-sm-6">																			
											<label className="control-label" for="password">Senha:</label>
											<input type="password" ref="password" name="password" className="form-control" />
										</div>
										<div className="col-sm-6">
											<label className="control-label" for="password2">Repita a senha:</label>
											<input type="password" ref="password2" name="password2" className="form-control" />							
										</div>
									</div>																		
								</div>
							</div>
							
							<br />
							
							<div className="card border-1">																
								<div className="card-body">
									<MensagemPainel color="danger">{erroMsg}</MensagemPainel>
									<MensagemPainel color="info">{infoMsg}</MensagemPainel>
									
									<div class="form-group">
										<button type="submit" className="btn btn-primary">Salvar</button>
									</div>																												
								</div>
							</div>									
						</form>
					</div>
				</div>
			</div>
		);
	}
	
}