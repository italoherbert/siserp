import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {Alert} from 'reactstrap';

import sistema from './../logica/sistema';

export default class MensagemPainel extends React.Component {
	
	constructor(props) {
		super(props);
		this.state = { msgId : "msg-"+new Date().getTime() };
	}		
	
	componentDidMount() {
		sistema.scrollTo( this.state.msgId );
	}
			
	render () {				
		const {msgId} = this.state;
				
		if ( this.props.msg == null ) {
			return (<span></span>);
		} else {
			return (
				<Alert color={this.props.cor}>{this.props.msg}</Alert>
			);
		}
	}					
		
}
		