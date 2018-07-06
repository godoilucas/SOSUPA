USE SOSUPA;

LOAD DATA INFILE '/var/lib/mysql-files/dados'
INTO TABLE data_input 
FIELDS TERMINATED BY ';' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(data_atendimento,
    data_nascimento,
    sexo,
    codigo_tipo_unidade,
    tipo_unidade,
    codigo_unidade,
    nome_unidade,
    codigo_procedimento,
    desc_procedimento,
    codigo_cbo,
    descricao_cbo,
    codigo_cid,
    descricao_cid,
    solicitado_exame);

SET SQL_SAFE_UPDATES = 0;
update data_input set dia_atendimento=str_to_date(data_atendimento,'%d/%m/%Y %T');
update data_input set data_nascimento_d=str_to_date(data_nascimento,'%d/%m/%Y %T');
