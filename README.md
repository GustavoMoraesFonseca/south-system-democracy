# Gerenciador de Votos

> Um desafio da SouthSystem que deve Gerenciar Votos

### Notas da versão:

  O projeto foi desenvolvido utilizando: 
  - [Elipse IDE jee-2021-09](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2022-03/R/eclipse-inst-jre-win64.exe)
  
  No desenvolvimento foi utilizado:
  - [Docker](https://www.docker.com/)
  - [MySQL Community Server 8.0.29](https://dev.mysql.com/downloads/mysql/)
  - [JDK 11](https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html)
  - [Apache Maven 3.8.3](https://maven.apache.org/download.cgi#downloading-apache-maven-3-8-3)
  - [Apache Kafka 5.5.0](https://kafka.apache.org/)
  - [Apache Zookeeper 5.5.0](https://zookeeper.apache.org/)
  - [Spring-Boot:2.6.7.RELEASE](https://spring.io/projects/spring-boot)

# Montando o Ambiente

Dependências Externas

> Apache Kafka

> Apache ZooKeeper

> Apache Maven

> MySQL
#### - Desenvolvimento - Local

* [Repositório Git com o Código Fonte](https://github.com/GustavoMoraesFonseca/south-system-democracy)

> Para que a aplicação funcione você precisara definir 4(quatro) variáveis de ambiente.

> * [Tutorial de como definir variáveis de ambiente no WINDOWS](https://www.supertutoriais.com.br/pc/como-criar-variaveis-personalizadas-windows-10/)

> * Comando para criar uma nova variável de ambiente no LINUX: NOME_MINHA_VARIAVEL=valor_minha_variavel

> ##### Que são:
    - MYSQL_IP (O IP do Banco de Dados MySQL);
    - MYSQL_USER (O usuário do Banco de Dados MySQL);
    - MYSQL_PASSWORD (A senha do usuário do Banco de Dados MySQL);
    - BOOSTRAP_SERVER (O IP e porta do seu Broker Kafka (Ex: 127.0.0.1:9092));

> Após a declaração das variáveis precisamos baixar a aplicação.

> * $git clone -b develop https://github.com/GustavoMoraesFonseca/south-system-democracy

> Agora que já temos o código fonte basta executa-lo com comando:

> * $mvn install clean spring-boot:run -DskipTests

> #### - Desenvolvimento - Container

* [Repositório Docker com a imagem da aplicação](https://hub.docker.com/r/gustavomoraesfonseca/south-system-test)

> Para rodar deve apenas baixar o docker-compose file de acordo com seu sistema operacional.

> LINUX

> * $curl --silent --output docker-compose-run-linux.yml https://raw.githubusercontent.com/GustavoMoraesFonseca/south-system-democracy/develop/docker-composes/docker-compose-run-linux.yml

> * $docker-compose -f docker-compose-run-linux.yml up -d

> ou WINDOWS

> * $curl --silent --output docker-compose-run-linux.yml https://raw.githubusercontent.com/GustavoMoraesFonseca/south-system-democracy/develop/docker-composes/docker-compose-run-windows.yml

> * $docker-compose -f docker-compose-run-windows.yml up -d

#### Produção

  Na produção foi utilizado:
  
  

