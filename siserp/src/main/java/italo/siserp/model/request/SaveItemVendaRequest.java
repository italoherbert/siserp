package italo.siserp.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SaveItemVendaRequest {
	
	private String codigoBarras;

	private String quantidade;
					
}
