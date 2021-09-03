insert into usuario_grupo ( nome ) values ( 'ADMIN' );
insert into usuario_grupo ( nome ) values ( 'GERENTE' );
insert into usuario_grupo ( nome ) values ( 'SUPERVISOR' );
insert into usuario_grupo ( nome ) values ( 'CAIXA' );

insert into usuario ( username, password, grupo_id ) values ( 'admin', sha256( 'admin' ), (select id from usuario_grupo where nome='ADMIN') );
update usuario set password=substring( password, 3 ) where username='admin';

insert into usuario ( username, password, grupo_id ) values ( 'supervisor', sha256( 'supervisor' ), (select id from usuario_grupo where nome='SUPERVISOR') );
update usuario set password=substring( password, 3 ) where username='supervisor';

insert into usuario ( username, password, grupo_id ) values ( 'gerente', sha256( 'gerente' ), (select id from usuario_grupo where nome='GERENTE') );
update usuario set password=substring( password, 3 ) where username='gerente';

insert into recurso ( nome ) values 
( 'usuario' ),
( 'usuarioGrupo' ),
( 'recurso' ),
( 'permissaoGrupo' ),
( 'sede' ),
( 'caixa' ),
( 'lancamento' ),
( 'produto' ),
( 'categoria' ),
( 'subcategoria' ),
( 'cliente' ),
( 'funcionario' ),
( 'fornecedor' ),
( 'compra' ),
( 'venda' ),
( 'contasPagar' ),
( 'contasReceber' ),
( 'balancoHoje' ),
( 'config' );

insert into permissao_grupo ( grupo_id, recurso_id, leitura, escrita, remocao ) values 
( (select id from usuario_grupo where nome='ADMIN'), (select id from recurso where nome='usuario'), true, true, true ),
( (select id from usuario_grupo where nome='ADMIN'), (select id from recurso where nome='usuarioGrupo'), true, true, true ),
( (select id from usuario_grupo where nome='ADMIN'), (select id from recurso where nome='recurso'), true, true, true ),
( (select id from usuario_grupo where nome='ADMIN'), (select id from recurso where nome='permissaoGrupo'), true, true, true ),
( (select id from usuario_grupo where nome='ADMIN'), (select id from recurso where nome='config'), true, true, true ),

( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='sede'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='caixa'), true, false, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='lancamento'), true, false, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='produto'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='categoria'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='subcategoria'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='cliente'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='funcionario'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='fornecedor'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='compra'), true, true, true ),
( (select id from usuario_grupo where nome='SUPERVISOR'), (select id from recurso where nome='venda'), true, false, true ),
 
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='sede'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='caixa'), true, false, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='lancamento'), true, false, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='produto'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='categoria'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='subcategoria'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='cliente'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='funcionario'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='fornecedor'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='compra'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='venda'), true, false, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='contasPagar'), true, true, true ),
( (select id from usuario_grupo where nome='GERENTE'), (select id from recurso where nome='contasReceber'), true, true, true ),

( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='sede'), true, false, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='caixa'), true, true, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='lancamento'), true, true, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='produto'), true, false, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='categoria'), true, false, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='subcategoria'), true, false, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='cliente'), true, false, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='venda'), true, true, false ),
( (select id from usuario_grupo where nome='CAIXA'), (select id from recurso where nome='contasReceber'), true, true, false );
