
class Inicial extends React.Component {

	constructor(props) {
		super(props);
		this.state = { erroMsg : null };
	}
	
	entrar(e) {		
		e.preventDefault();	
			
		
	}
	
	
	render() {
		const { erroMsg } = this.state;
						
		return (			
			<div>
				<h4>Bem vindo</h4>
				
				<p>Olá {sistema.usuario.username}, seja bem vindo</p>
																			
				<MensagemPainel tipo="erro" mensagem={erroMsg} />					
			</div>															
		);
	}
}
