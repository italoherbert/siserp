package italo.siserp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuario_grupo")
public class UsuarioGrupo {

	public final static String ADMIN = "ADMIN";
	public final static String CAIXA = "CAIXA";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String nome;
	
	@OneToMany(mappedBy="grupo")
	private List<Usuario> usuario;
	
	@OneToMany(mappedBy="grupo", cascade=CascadeType.ALL)	
	private List<PermissaoGrupo> permissaoGrupos;
	
}
