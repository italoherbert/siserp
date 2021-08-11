package italo.siserp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
			
	@Column
	private String username;
	
	@Column
	private String password;
	
	@ManyToOne
	@JoinColumn(name="grupo_id") 
	private UsuarioGrupo grupo;
	
	@OneToOne(mappedBy="usuario", cascade=CascadeType.REMOVE, optional = true)
	private Funcionario funcionario;
						
}
