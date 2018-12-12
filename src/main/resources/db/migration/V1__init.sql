-- Table: cliente

-- DROP TABLE cliente;

--CREATE SEQUENCE foo_seq;
--CREATE DOMAIN foo_seq_dom AS BIGINT NOT NULL DEFAULT nextval('foo_seq');

CREATE TABLE cliente
(
  id BIGINT NOT NULL,
  cpf character varying(255) NOT NULL,
  email character varying(255) NOT NULL,
  nome character varying(255) NOT NULL,
  telefone character varying(255) NOT NULL,
  CONSTRAINT cliente_pkey PRIMARY KEY (id)
);

-- Table: endereco

-- DROP TABLE endereco;

CREATE TABLE endereco
(
  id bigint NOT NULL,
  cep character varying(255) NOT NULL,
  bairro character varying(255) NOT NULL,
  cidade character varying(255) NOT NULL,
  estado character varying(255) NOT NULL,
  rua character varying(255) NOT NULL,
  CONSTRAINT endereco_pkey PRIMARY KEY (id)
);


-- Table: tecnico

-- DROP TABLE tecnico;

CREATE TABLE tecnico
(
  id bigint NOT NULL,
  nome character varying(255) NOT NULL,
  CONSTRAINT tecnico_pkey PRIMARY KEY (id)
);



-- Table: ordemservico

-- DROP TABLE ordemservico;

CREATE TABLE ordemservico
(
  id bigint NOT NULL,
  data_aberta timestamp without time zone NOT NULL,
  data_finalizada timestamp without time zone,
  especificacao character varying(255) NOT NULL,
  status character varying(255) NOT NULL,
  cliente_id bigint NOT NULL,
  endereco_id bigint NOT NULL,
  tecnico_id bigint NOT NULL,
  CONSTRAINT ordemservico_pkey PRIMARY KEY (id),
  CONSTRAINT fk_tecnico_id FOREIGN KEY (tecnico_id)
      REFERENCES tecnico (id),
  CONSTRAINT fk_cliente_id FOREIGN KEY (cliente_id)
      REFERENCES cliente (id),
  CONSTRAINT fk_endereco_id FOREIGN KEY (endereco_id)
      REFERENCES endereco (id)
);

CREATE SEQUENCE seq_cliente
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE SEQUENCE seq_endereco
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE SEQUENCE seq_tecnico
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE SEQUENCE seq_ordemservico
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;


ALTER  TABLE cliente ALTER COLUMN id set default nextval('seq_cliente');
ALTER  TABLE endereco ALTER COLUMN id set default nextval('seq_endereco');
ALTER TABLE tecnico ALTER COLUMN id set default nextval('seq_tecnico');
ALTER TABLE ordemservico ALTER COLUMN id set default nextval('seq_ordemservico');