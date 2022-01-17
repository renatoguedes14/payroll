# 1 - Como executar a aplicação 
<b>Para compilar e executar: </b> na raíz do projeto, execute o comando: mvn spring-boot:run

<b>Requisitos para executar a aplicação: </b> É necessário ter PostgreSQL instalado e um banco de dados configurado. Em 'application.properties', altere os parâmetros '<b>spring.datasource.url',  'spring.datasource.username'</b> e <b>'spring.datasource.password'</b> para conter os parâmetros do banco de dados local da máquina onde a aplicação será executada, além disso, é necessário criar, no datasource, o banco de dados com o nome contido no arquivo .properties. A criação das tabelas e colunas é feita automaticamente
# 2 - Como testar a funcionalidade da API: 
Utilizando a collection POSTMAN encontrada neste repositório, na pasta "Authentication", utilizar a requisição "Create user", inserindo suas credenciais. Após isso, utilizar a requisição "Login" utilizando as mesmas credenciais que a requisição anterior.
Uma vez obtido o retorno da requisição, nos Headers da response, pegar as informações do token e utilizá-lo para testar as demais funcionalidades da API.

# 3 - Informações adicionais:

O arquivo application.properties foi fornecido no anexo do e-mail contendo o link para este repositório. Por razões de segurança, removi o arquivo do repositório público.

<b>Tecnologias e bibliotecas utilizadas: </b> Java 11, Spring Boot, Spring Data, Spring Security, JWT, PostgreSQL, Project Lombok, Swagger UI e RabbitMQ.

## Swagger
http://localhost:8183/swagger-ui/index.html


<b>Este projeto foi desenvolvido por Renato Guedes para o processo seletivo da vaga de Desenvolvedor de back-end Sênior da Somapay.</b>
