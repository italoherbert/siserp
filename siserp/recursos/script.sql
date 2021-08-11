insert into usuario_grupo ( nome ) values ( 'ADMIN' );
insert into usuario_grupo ( nome ) values ( 'CAIXA' );

insert into usuario ( username, password, grupo_id) values ( 'admin', sha256( 'admin' ), 1 );
update usuario set password=substring( password, 3 ) where username='admin';

insert into recurso ( nome ) values ( 'usuario' );
insert into recurso ( nome ) values ( 'usuarioGrupo' );
insert into recurso ( nome ) values ( 'recurso' );
insert into recurso ( nome ) values ( 'permissaoGrupo' );
insert into recurso ( nome ) values ( 'sede' );
insert into recurso ( nome ) values ( 'caixa' );
insert into recurso ( nome ) values ( 'lancamento' );
insert into recurso ( nome ) values ( 'produto' );
insert into recurso ( nome ) values ( 'categoria' );
insert into recurso ( nome ) values ( 'subcategoria' );
insert into recurso ( nome ) values ( 'cliente' );
insert into recurso ( nome ) values ( 'funcionario' );
insert into recurso ( nome ) values ( 'fornecedor' );
insert into recurso ( nome ) values ( 'compra' );
insert into recurso ( nome ) values ( 'venda' );

insert into permissao_grupo ( grupo_id, recurso_id, leitura, escrita, remocao ) values ( 1, 1, true, true, true );
insert into permissao_grupo ( grupo_id, recurso_id, leitura, escrita, remocao ) values ( 1, 2, true, true, true );
insert into permissao_grupo ( grupo_id, recurso_id, leitura, escrita, remocao ) values ( 1, 3, true, true, true );
insert into permissao_grupo ( grupo_id, recurso_id, leitura, escrita, remocao ) values ( 1, 4, true, true, true );