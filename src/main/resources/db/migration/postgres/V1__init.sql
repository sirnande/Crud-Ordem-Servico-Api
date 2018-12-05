-- Table: cliente

-- DROP TABLE cliente;

CREATE TABLE cliente
(
  id bigint NOT NULL,
  cpf character varying(255) NOT NULL,
  email character varying(255) NOT NULL,
  nome character varying(255) NOT NULL,
  telefone character varying(255) NOT NULL,
  CONSTRAINT cliente_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cliente
  OWNER TO postgres;

-- Table: endereco

-- DROP TABLE endereco;

CREATE TABLE endereco
(
  id bigint NOT NULL,
  bairro character varying(255) NOT NULL,
  cidade character varying(255) NOT NULL,
  estado character varying(255) NOT NULL,
  rua character varying(255) NOT NULL,
  CONSTRAINT endereco_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE endereco
  OWNER TO postgres;

-- Table: tecnico

-- DROP TABLE tecnico;

CREATE TABLE tecnico
(
  id bigint NOT NULL,
  nome character varying(255) NOT NULL,
  CONSTRAINT tecnico_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tecnico
  OWNER TO postgres;


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
  CONSTRAINT fk9qm4jj1iai5qhs5dikuq79965 FOREIGN KEY (tecnico_id)
      REFERENCES tecnico (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkqerkb8u5oinbncan96j1kw9wi FOREIGN KEY (cliente_id)
      REFERENCES cliente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fksqdxlecpx8ks9sep84v8kgfgv FOREIGN KEY (endereco_id)
      REFERENCES endereco (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ordemservico
  OWNER TO postgres;


