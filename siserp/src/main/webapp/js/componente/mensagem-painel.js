
class MensagemPainel extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = { msgId : "msg-"+new Date().getTime() };
	}		
	
	componentDidMount() {
		sistema.scrollTo( this.state.msgId );
	}
			
	render () {				
		const {msgId} = this.state;
		
		let className = "alert ";		
			
		let mensagem = null;
		if ( this.props.tipo == "info" ) {
			mensagem = this.props.msg;
			className += "alert-info";
		} else if ( this.props.tipo == "erro" ) {
			mensagem = this.props.msg;
			className += "alert-danger";
		}
		
		if ( mensagem == null ) {
			return (<span></span>);
		} else {
			return (
				<div id={msgId} className={className}>
					{mensagem}
				</div>
			);
		}
	}					
		
}
		