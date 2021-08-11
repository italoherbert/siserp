package italo.siserp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table( name="permissao_grupo" )
public class PermissaoGrupo {

	public final static String PREFIXO_ESCRITA = "WRITE";
	public final static String PREFIXO_LEITURA = "READ";
	public final static String PREFIXO_REMOCAO = "DELETE";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="grupo_id")
	private UsuarioGrupo grupo;
	
	@ManyToOne
	@JoinColumn(name="recurso_id")
	private Recurso recurso;
	
	@Column
	private boolean leitura;
	
	@Column
	private boolean escrita;
	
	@Column
	private boolean remocao;
	
}
