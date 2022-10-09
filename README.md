# Sistema ERP - como rodar o sistema

O sistema ERP de automação utiliza Java, Spring Boot/Data/Security e autenticação stateles e autorização com Java Web Token (JWT). 

Para executar o sistema do zero, é necessário, primeiramente, criar a base de dados de nome "siserp" no SGBD PostgreSQL e, então 
rodar o projeto no spring tool suite ou via linha de comando com maven. Claro, o servidor de banco de dados PostgreSQL deve estar em execução também.

Após o sistema executado a primeira vez no servidor tomcat embutido, as tabelas foram criadas na base de dados de nome "siserp" e, agora, 
é necessário configurar o usuário padrão e os recursos que devem ser restritos aos devidos usuários.

Para alimentar a base de dados com os dados dos recursos, tipos de usuários e as permissões por grupo de usuário, basta navegar até 
a pasta "recursos" do projeto e, então, utilizar o seguinte comando para se autenticar no PostgreSQL:

    psql -U postgres sgescolar
    Senha: postgres

Para executar o script contido na pasta "siserp/recursos" faça o seguinte:

    \i script.sql

Após executado o script de criação dos grupos, recursos e os usuários padrão. Você deve executar o sistema que está empacotado em formato WAR. Isto é, o arquivo 
da raiz do repositório de nome "siserp-1.0.war". Execute com o seguinte comando:

    java -jar siserp-1.0.war

    Tenha a certeza que está utilizando uma versão do Java igual ou superior a versão 11.

Você pode logar no sistema com os seguintes dados:

    Username    password
    admin       admin
    supervisor  supervisor
    gerente     gerente

Inclusive, você pode entrar como supervisor e registrar um funcionário (na tela e função "registrar funcionario") e registrar um funcionário com peril de caixa para
ter acesso ao sistema na versão de caixa!

Após isto, será iniciada a página de login no navegador acessível pela seguinte url:

    localhost:8080

Após logado como usuário supervisor, se pode criar as contas com perfis de SUPERVISOR e CAIXA. Esses são os quatro perfis
suportados pelo sistema e que têm associados os devidos recursos.
