USE SOSUPA;
DROP TABLE estatisticas_pacientes_upa;
DROP TABLE estatisticas_diagnosticos;
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
# Estatisticas Pacientes
INSERT INTO estatisticas_pacientes_upa(nome_unidade, mes, ano, total, pacientes_12, pacientes_12_25, pacientes_25_60, pacientes_60, pacientes_mulheres, pacientes_homens)
Select nome_unidade,MONTH(dia_atendimento) as mes, YEAR(dia_atendimento) as ano, Count(*) as total,
COUNT(CASE WHEN (YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d))) > -1 AND YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d)))<13) then 1 END) as pacientes_12,
COUNT(CASE WHEN (YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d))) > 12 AND YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d)))<26) then 1 END) as pacientes_12_25,
COUNT(CASE WHEN (YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d))) > 25 AND YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d)))<61) then 1 END) as pacientes_25_60,
COUNT(CASE WHEN YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(data_nascimento_d))) > 60 then 1 END) as pacientes_60,
COUNT(CASE When sexo='F' then 1 END) as pacientes_mulheres,
COUNT(CASE When sexo='M' then 1 END) as pacientes_homens
from data_input 
where nome_unidade Like "%UPA%" 
AND (MONTH(dia_atendimento) = '3' OR MONTH(dia_atendimento) = '4' OR MONTH(dia_atendimento) = '5') AND 
YEAR(dia_atendimento) = '2018' group by nome_unidade, MONTH(dia_atendimento), YEAR(dia_atendimento);

# Diagnosticos
INSERT INTO estatisticas_diagnosticos(nome_unidade,diagnostico,mes,ano,numero_casos)
Select nome_unidade, descricao_cid,MONTH(dia_atendimento) as mes, YEAR(dia_atendimento) as ano, count(*) as total from data_input 
where nome_unidade Like "%UPA%" and 
YEAR(dia_atendimento) = '2018' 
AND (MONTH(dia_atendimento) = '3' OR MONTH(dia_atendimento) = '4' OR MONTH(dia_atendimento) = '5')
and codigo_cid!='Z532' and codigo_cid!='Z000' and codigo_cid!='Z539'
Group by nome_unidade, descricao_cid, MONTH(dia_atendimento), YEAR(dia_atendimento) order by total desc;
