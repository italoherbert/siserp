import React from 'react';

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
		
		this.nome = React.createRef();
		this.telefone = React.createRef();
		this.email = React.createRef();
		
		this.ender = React.createRef();
		this.numero = React.createRef();
		this.logradouro = React.createRef();
		this.bairro = React.createRef();
		this.cidade = React.createRef();
		this.uf = React.createRef();
		
		
		this.username = React.createRef();		
		this.password = React.createRef();		
		this.password2 = React.createRef();
	}
	
	componentDidMount() {			
		if ( this.props.op === 'editar' ) {				
			this.nome.current.value = this.props.nome;
			this.telefone.current.value = this.props.telefone;
			this.email.current.value = this.props.email;
			
			this.ender.current.value = this.props.ender;
			this.numero.current.value = this.props.numero;
			this.logradouro.current.value = this.props.logradouro;
			this.bairro.current.value = this.props.bairro;
			this.cidade.current.value = this.props.cidade;
			this.uf.current.value = this.props.uf;
			
			this.username.current.value = this.props.username;
		}
		
		fetch( "/api/usuario/tipos", {
			method : "GET",			
			headers : {
				"Authorization" : "Bearer "+sistema.token
			}
		} ).then( (resposta) => {	
			resposta.json().then( (dados) => {
				this.setState( { uTipos : dados } );
											
				if ( this.props.op === 'editar' )							
					this.setState( { uTipoSelecionado : this.props.tipo } );												
			} );			 
		} );
	}
	
	salvar( e ) {
		e.preventDefault();
		
		this.setState( { erroMsg : null, infoMsg : null } );
		
		let url;
		let metodo;
		if ( this.props.op === 'editar' ) {			
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
					"nome" : this.nome.current.value,
					"telefone" : this.telefone.current.value,				
					"email" : this.email.current.value,
					
					"endereco" : {
						"ender" : this.ender.current.value,
						"numero" : this.numero.current.value,
						"bairro" : this.bairro.current.value,
						"cidade" : this.cidade.current.value,
						"uf" : this.uf.current.value,
						"logradouro" : this.logradouro.current.value				
					}
				},
				
				"usuario" : {
					"username" : this.username.current.value,
					"password" : this.password.current.value,
					"tipo" : this.tipo.current.value	
				}
			} )		
		} ).then( (resposta) => {						
			if ( resposta.status === 200 ) {
				this.setState( { infoMsg : "Funcionario cadastrado com sucesso." } );
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
											<input type="text" ref={this.nome} name="nome" size="50" className="form-control" />
										</div>
									</div>
									<div className="row form-group">
										<div className="col-sm-6">
											<label className="control-label" for="telefone">Telefone: </label>
											<input type="tel" ref={this.telefone} name="tel" className="form-control" />
										</div>
										<div className="col-sm-6">						
											<label className="control-label" for="email">E-Mail: </label>
											<input type="email" ref={this.email} name="email" className="form-control" />						
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
											<input type="text" ref={this.ender} name="ender" className="form-control" />
										</div>
									</div>
									
									<div className="row form-group">
										<div className="col-sm-4">
											<label className="control-label" for="ender">Numero:</label>
											<input type="text" ref={this.numero} name="numero" size="8" className="form-control" />
										</div>
										<div className="col-sm-4">
											<label className="control-label" for="logradouro">Logradouro:</label>
											<input type="text" ref={this.logradouro} name="logradouro" size="10" className="form-control" />
										</div>
										<div className="col-sm-4">
											<label className="control-label" for="bairro">Bairro:</label>
											<input type="text" ref={this.bairro} name="bairro" size="20" className="form-control" />
										</div>
									</div>
																		
									<div className="row form-group">
										<div className="col-sm-8">
											<label for="cidade">Cidade:</label>
											<input type="text" ref={this.cidade} name="cidade" size="30" className="form-control" />
										</div>
										<div className="col-sm-4">
											<label for="uf">UF:</label>
											<input type="text" ref={this.uf} name="uf" size="10" className="form-control" />							
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
											<input type="text" ref={this.username} name="username" className="form-control" />
										</div>
										
										<div className="col-sm-4">
											<label className="control-label" for="tipo">Tipos:</label>
											<select name="tipo" ref={this.tipo} value={uTipoSelecionado} className="form-control">
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
											<input type="password" ref={this.password} name="password" className="form-control" />
										</div>
										<div className="col-sm-6">
											<label className="control-label" for="password2">Repita a senha:</label>
											<input type="password" ref={this.password2} name="password2" className="form-control" />							
										</div>
									</div>																		
								</div>
							</div>
							
							<br />
							
							<div className="card border-1">																
								<div className="card-body">
									<MensagemPainel cor="danger" msg={erroMsg} />
									<MensagemPainel cor="primary" msg={infoMsg} />
									
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