USE SOSUPA;
DROP TABLE upa_tempo_estimado;
DROP TABLE upa_atendimentos;

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

Insert into upa_tempo_estimado(nome_unidade,ano,mes,dia_semana,estimado_periodo1,estimado_periodo2,estimado_periodo3,atendimentos_periodo1,atendimentos_periodo2, atendimentos_periodo3)
Select T3.nome_unidade, T3.ano, T3.mes, T3.dia_semana,
ROUND(((MAX(T3.atendimentos_periodo1)/(MAX(T3.atendimentos_periodo1)+1/(MAX(T3.atendimentos_periodo1)*0.05)))/(1 - MAX(T3.atendimentos_periodo1)/(MAX(T3.atendimentos_periodo1)+1/(MAX(T3.atendimentos_periodo1)*0.05)))/MAX(T3.atendimentos_periodo1)),2) as estimado_periodo1,
ROUND(((MAX(T3.atendimentos_periodo2)/(MAX(T3.atendimentos_periodo2)+1/(MAX(T3.atendimentos_periodo2)*0.065)))/(1 - MAX(T3.atendimentos_periodo2)/(MAX(T3.atendimentos_periodo2)+1/(MAX(T3.atendimentos_periodo2)*0.065)))/MAX(T3.atendimentos_periodo2)),2) as estimado_periodo2,
ROUND(((MAX(T3.atendimentos_periodo3)/(MAX(T3.atendimentos_periodo3)+1/(MAX(T3.atendimentos_periodo3)*0.06)))/(1 - MAX(T3.atendimentos_periodo3)/(MAX(T3.atendimentos_periodo3)+1/(MAX(T3.atendimentos_periodo3)*0.06)))/MAX(T3.atendimentos_periodo3)),2) as estimado_periodo3,
MAX(T3.atendimentos_periodo1) as atendimentos_periodo1, MAX(T3.atendimentos_periodo2) as atendimentos_periodo2, MAX(T3.atendimentos_periodo3) as atendimentos_periodo3
FROM (Select T2.nome_unidade, T2.ano, T2.mes, T2.dia_semana,
(CASE WHEN ( T2.hora = 0 or (T2.hora > 0 and T2.hora < 9)) then MAX(T2.numero_atendimentos) END) as atendimentos_periodo1,
(CASE WHEN ( T2.hora = 9 or (T2.hora > 9 and T2.hora < 18)) then MAX(T2.numero_atendimentos) END) as atendimentos_periodo2,
(CASE WHEN ( T2.hora = 18 or (T2.hora > 18 and T2.hora < 24)) then MAX(T2.numero_atendimentos) END) as atendimentos_periodo3
FROM (Select T1.nome_unidade, T1.ano, T1.mes, T1.semana,T1.dia_semana,T1.hora,(ceiling(SUM(atendimentos)/count(T1.dia_semana))) as numero_atendimentos 
from (select nome_unidade,HOUR(dia_atendimento) as hora ,WEEKDAY(dia_atendimento) as dia_semana,WEEK(dia_atendimento) as semana,count(*) as atendimentos, MONTH(dia_atendimento) as mes, YEAR(dia_atendimento) as ano
	FROM data_input 
	where nome_unidade LIKE 'UPA%'
	GROUP BY nome_unidade, YEAR(dia_atendimento), MONTH(dia_atendimento),WEEK(dia_atendimento),WEEKDAY(dia_atendimento),HOUR(dia_atendimento)) AS T1
group by T1.nome_unidade,T1.ano, T1.mes, T1.hora, T1.dia_semana, T1.semana) AS T2
group by T2.nome_unidade, T2.ano, T2.mes, T2.dia_semana, T2.hora) AS T3
group by T3.nome_unidade, T3.ano, T3. mes, T3.dia_semana;


Insert into upa_atendimentos(nome_unidade, ano, mes, semana, dia_semana, hora, numero_atendimentos)
Select T1.nome_unidade, T1.ano, T1.mes, T1.semana,T1.dia_semana,T1.hora,(ceiling(SUM(atendimentos)/count(T1.dia_semana))) as numero_atendimentos 
from (select nome_unidade,HOUR(dia_atendimento) as hora ,WEEKDAY(dia_atendimento) as dia_semana,WEEK(dia_atendimento) as semana,count(*) as atendimentos, MONTH(dia_atendimento) as mes, YEAR(dia_atendimento) as ano
	FROM data_input 
	where nome_unidade LIKE 'UPA%'
	GROUP BY nome_unidade, YEAR(dia_atendimento), MONTH(dia_atendimento),WEEK(dia_atendimento),WEEKDAY(dia_atendimento),HOUR(dia_atendimento)) AS T1
group by T1.nome_unidade,T1.ano, T1.mes, T1.hora, T1.dia_semana, T1.semana;
