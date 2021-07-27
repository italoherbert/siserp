
class Tabela extends React.Component {
	
	constructor( props ) {
		super( props );
	}
		
	render() {		
		const { cols, dados } = this.props;
		
		return(
			<div>
				<table className="table-striped table-bordered">
					<thead>
						{cols.map( (item) => {
							return <th>{item}</th>;
						} )}
					</thead>
					<tbody>
						{dados.map( (linha) => {
							return <tr>
								{ linha.map( (col) => {
									return <td>{col}</td>;
								} ) }
							</tr>
						} )}
					</tbody>
				</table>				
			</div>
		);
	}
	
}