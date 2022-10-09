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

Para executar o script contido na pasta "recursos" faça o seguinte:

    \i script.sql

Após isto, execute o seguinte comando para configurar o charset, isto é, a codificação padrão do banco de dados:

    set client_encoding=ISO88591

Após executado o script de criação dos grupos, recursos e o super usuário padrão, se pode logar no sistema com os seguintes dados:

    Username: admin
    Senha: admin

Claro, para isto, é necessário rodar o sistema no servidor tomcat embutido. 

Feito isto, você deve ter o npm instalado para executar o react em modo dev, navegando até a pasta siserp-react do projeto e executando o seguinte comando:

    npm start

Após isto, será iniciada a página de login no navegador acessível pela seguinte url:

    localhost:3000

Após logado como usuário admin, se pode criar as contas de nível ADMIN, GERENTE, SUPERVISOR e CAIXA. Esses são os quatro perfis
suportados pelo sistema e que têm associados os devidos recursos.
