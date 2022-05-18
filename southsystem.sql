CREATE SCHEMA `southsystem_votos` DEFAULT CHARACTER SET latin1;

CREATE USER 'backend-service'@'%' IDENTIFIED BY 'Back@Service';
GRANT SELECT, DELETE, INSERT, UPDATE  ON *.* TO 'backend-service'@'%';
FLUSH PRIVILEGES;

CREATE TABLE `southsystem_votos`.`tab_pauta` (
  `pauta_id` INT NOT NULL AUTO_INCREMENT,
  `pauta_descricao` VARCHAR(45) NOT NULL,
  `pauta_qtd_votos_positivos` INT,
  `pauta_qtd_votos_negativos` INT,
  `pauta_is_close` TINYINT NOT NULL,
  `dthr_criacao` DATETIME NOT NULL,
  `dthr_atualizacao` DATETIME NOT NULL,
  PRIMARY KEY (`pauta_id`),
  UNIQUE INDEX `pauta_descricao` (`pauta_descricao` ASC) VISIBLE);

CREATE TABLE `southsystem_votos`.`tab_associado` (
  `associado_id` INT NOT NULL AUTO_INCREMENT,
  `associado_nome` VARCHAR(45) NOT NULL,
  `associado_cpf` VARCHAR(45) NOT NULL,
  `associado_email` VARCHAR(45) NOT NULL,
  `dthr_criacao` DATETIME NOT NULL,
  `dthr_atualizacao` DATETIME NOT NULL,
  PRIMARY KEY (`associado_id`),
  UNIQUE INDEX `associado_cpf_UNIQUE` (`associado_cpf` ASC) VISIBLE,
  UNIQUE INDEX `associado_email_UNIQUE` (`associado_email` ASC) VISIBLE);
  
CREATE TABLE `southsystem_votos`.`tab_voto` (
  `voto_id` INT NOT NULL AUTO_INCREMENT,
  `voto_pauta_id` INT NOT NULL,
  `voto_associado_id` INT NOT NULL,
  `voto_is_favor` TINYINT NOT NULL,
  `voto_is_close` TINYINT NOT NULL,
  `dthr_criacao` DATETIME NOT NULL,
  `dthr_atualizacao` DATETIME NOT NULL,
  PRIMARY KEY (`voto_id`),
  INDEX `FK_VOTO_TO_PAUTA_idx` (`voto_pauta_id` ASC) VISIBLE,
  INDEX `FK_VOTO_TO_ASSOCIADO_idx` (`voto_associado_id` ASC) VISIBLE,
  CONSTRAINT `FK_VOTO_TO_PAUTA`
    FOREIGN KEY (`voto_pauta_id`)
    REFERENCES `southsystem_votos`.`tab_pauta` (`pauta_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_VOTO_TO_ASSOCIADO`
    FOREIGN KEY (`voto_associado_id`)
    REFERENCES `southsystem_votos`.`tab_associado` (`associado_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);