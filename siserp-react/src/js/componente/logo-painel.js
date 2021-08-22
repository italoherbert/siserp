import React from 'react';

export default class LogoPainel extends React.Component {
						
	render () {			
		const { src } = this.props;
		return (
			<img className="float-end mx-5" src={src} alt="Logomarca" />
		)		
	}					
		
}
		