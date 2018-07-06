CREATE DATABASE SOSUPA;
USE SOSUPA;

CREATE TABLE data_input(
	data_atendimento varchar(40),
    data_nascimento varchar(40),
    sexo varchar(1),
    codigo_tipo_unidade integer,
    tipo_unidade varchar(10),
    codigo_unidade varchar(15),
    nome_unidade varchar(100),
    codigo_procedimento varchar(15),
    desc_procedimento text,
    codigo_cbo varchar(20),
    descricao_cbo varchar(40),
    codigo_cid varchar(15),
    descricao_cid text,
    solicitado_exame varchar(5),
    id bigint NOT NULL AUTO_INCREMENT,
	dia_atendimento datetime,
    data_nascimento_d datetime,
    primary key(id)
);

CREATE TABLE estatisticas_pacientes_upa(
	nome_unidade varchar(40),
	pacientes_homens float,
    pacientes_mulheres float,
    pacientes_12 float,
	pacientes_12_25 float,
	pacientes_25_60 float,
    pacientes_60 float,
    total integer,
    mes varchar(5),
    ano varchar(5),
    id bigint NOT NULL AUTO_INCREMENT,
	primary key(id)
);

CREATE TABLE estatisticas_diagnosticos(
	nome_unidade varchar(40),
	diagnostico varchar(200),
    mes varchar(5),
    ano varchar(5),
    numero_casos integer,
    id bigint NOT NULL AUTO_INCREMENT,
	primary key(id)
);

CREATE TABLE bi(
	id bigint NOT NULL AUTO_INCREMENT,
    latlong varchar(30),
    primary key(id)
);

CREATE TABLE upas(
	id bigint NOT NULL AUTO_INCREMENT,
    nome varchar(30),
    telefone varchar(20),
    endereco varchar(100),
    primary key(id)
);

INSERT INTO upas (nome, telefone, endereco) value ('UPA CAMPO COMPRIDO','3373-4955','Rua Monsenhor Ivo Zanlorenzi, 3495 - Campo Comprido, Curitiba - PR, 81210-000');
INSERT INTO upas (nome, telefone, endereco) value ('UPA BOQUEIRAO','3217-1259','Rua Professora Maria de Assumpcao, 2590 - Boqueirao, Curitiba - PR, 81670-040');
INSERT INTO upas (nome, telefone, endereco) value ('UPA FAZENDINHA','3314-5112','Rua Carlos Klemtz, 1883 - Fazendinha, Curitiba - PR, 81020-430');
INSERT INTO upas (nome, telefone, endereco) value ('UPA PINHEIRINHO','3212-1471','Rua Leon Nícolas - Capao Raso, Curitiba - PR, 81150-140');
INSERT INTO upas (nome, telefone, endereco) value ('UPA BOA VISTA','3251-1011','Av. Parana, 3654 - Boa Vista, Curitiba - PR, 82510-000');
INSERT INTO upas (nome, telefone, endereco) value ('UPA CAJURU','3261-4099','R. Eng. Benedito Mario da Silva, 555 - Cajuru, Curitiba - PR, 80050-540');
INSERT INTO upas (nome, telefone, endereco) value ('UPA SITIO CERCADO','3378-6052','Rua Dr. Levy Buquera, 158 - Sitio Cercado, Curitiba - PR, 81910-190');
INSERT INTO upas (nome, telefone, endereco) value ('UPA CIC','3314-5121','Rua Senador Accioly Filho, 3370 - Cidade Industrial, Curitiba - PR, 81350-200');

INSERT INTO upas (nome, telefone, endereco) value ('UPA TATUQUARA','3348-0616','R. Jorn. Emílio Zola Florenzano, 835 - Tatuquara, Curitiba - PR, 81470-300');


CREATE TABLE upa_tempo_estimado(
	id bigint NOT NULL AUTO_INCREMENT,
    nome_unidade varchar(30),
    ano varchar(5),
    mes varchar(5),
    dia_semana varchar(5),
    estimado_periodo1 varchar(5),
	estimado_periodo2 varchar(5),
	estimado_periodo3 varchar(5),
    atendimentos_periodo1 integer,
    atendimentos_periodo2 integer,
    atendimentos_periodo3 integer,
    primary key(id)
);

CREATE TABLE upa_atendimentos(
	id bigint NOT NULL AUTO_INCREMENT,
    nome_unidade varchar(30),
    ano varchar(5),
    mes varchar(5),
	semana varchar(5),
    dia_semana varchar(5),
    hora varchar(5),
	numero_atendimentos integer,
	primary key(id)
);
