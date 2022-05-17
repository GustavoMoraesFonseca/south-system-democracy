# Montando o Ambiente

Dependências Externas

> Apache Kafka

> Apache ZooKeeper

> MySQL

#### - Desenvolvimento - Local

* [Repositório Git com o Código Fonte](https://github.com/GustavoMoraesFonseca/south-system-democracy)

> Para que a aplicação funcione você precisara definir 4(quatro) variáveis de ambiente.

> * [Tutorial de como definir variáveis de ambiente no WINDOWS](https://www.supertutoriais.com.br/pc/como-criar-variaveis-personalizadas-windows-10/)

> * Comando para criar uma nova variável de ambiente no LINUX: NOME_MINHA_VARIAVEL=valor_minha_variavel

> ##### Que são:
* MYSQL_IP (O IP do Banco de Dados MySQL);
* MYSQL_USER (O usuário do Banco de Dados MySQL);
* MYSQL_PASSWORD (A senha do usuário do Banco de Dados MySQL);
* BOOSTRAP_SERVER (O IP e porta do seu Broker Kafka (Ex: 127.0.0.1:9092));

> Após a declaração das variáveis precisamos baixar a aplicação.

> Agora que já temos o código fonte basta executa-lo com comando:

> * $mvn install clean spring-boot:run -DskipTests

* [Repositório Docker com a imagem da aplicação](https://hub.docker.com/r/gustavomoraesfonseca/south-system-test)

#### - Produção

* [Spring Boot Maven Plugin Reference Guide](https://hub.docker.com/r/gustavomoraesfonseca/south-system-test)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.7/maven-plugin/reference/html/#build-image)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-kafka)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.7/reference/htmlsingle/#boot-features-developing-web-applications)


### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

