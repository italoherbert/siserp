insert into usuario ( username, password, tipo ) values ( 'admin', sha256( 'admin' ), 'ADMIN' );
update usuario set password=substring( password, 3 ) where username='admin';