import React, {Component} from 'react';
import ReactDOM from 'react-dom';

import MensagemPainel from './../../componente/mensagem-painel';
import sistema from './../../logica/sistema';

export default class Inicial extends React.Component {

	constructor(props) {
		super(props);
		this.state = { erroMsg : null };
	}
		
	render() {
		const { erroMsg } = this.state;
						
		return (			
			<div>
				<h4>Bem vindo</h4>
				
				<p>Olá {sistema.usuario.username}, seja bem vindo</p>
																			
				<MensagemPainel color="danger">{erroMsg}</MensagemPainel>					
			</div>															
		);
	}
}
