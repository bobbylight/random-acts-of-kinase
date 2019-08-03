CREATE SCHEMA IF NOT EXISTS :schema;
SET SCHEMA :'schema';

DROP TABLE IF EXISTS :schema.compound CASCADE;
CREATE TABLE :schema.compound (
  compound_nm character varying(100) NOT NULL,
  chemotype character varying(100),
  s_10 numeric,
  source character varying(80),
  smiles character varying(2048),
  reference_1 character varying(768),
  reference_1_url character varying(2048),
  hidden boolean NOT NULL DEFAULT FALSE,
  solubility_ug_ml numeric,
  CONSTRAINT compound_pkey PRIMARY KEY (compound_nm)
)
WITH ( OIDS = FALSE );

CREATE INDEX compound_compound_nm_idx ON :schema.compound (compound_nm);
CREATE INDEX compound_compound_nm_lower_idx ON :schema.compound (lower(compound_nm));
CREATE INDEX compound_hidden_idx on :schema.compound (hidden);


CREATE VIEW :schema.compound_with_kinase_activity_profile as
  SELECT * from :schema.compound
  WHERE compound_nm IN (SELECT DISTINCT compound_nm FROM :schema.kinase_activity_profile);


CREATE VIEW :schema.compound_with_nanobret_activity_profile as
  SELECT * from :schema.compound
  WHERE compound_nm IN (SELECT DISTINCT compound_nm FROM :schema.nanobret_activity_profile);


DROP TABLE IF EXISTS :schema.kinase CASCADE;
CREATE TABLE :schema.kinase (
  id serial,
  discoverx_gene_symbol character varying(100),
  entrez_gene_symbol character varying(100) NOT NULL,
  nanosyn_gene_symbol character varying(100),
  discoverx_url varchar(2048) UNIQUE,
  CONSTRAINT kinase_pkey PRIMARY KEY (id),
  CONSTRAINT kinase_discoverx_gene_symbol_entrez_gene_symbol_key UNIQUE (discoverx_gene_symbol, entrez_gene_symbol)
)
WITH ( OIDS = FALSE );

CREATE INDEX kinase_entrez_gene_symbol_idx ON :schema.kinase (entrez_gene_symbol);
CREATE INDEX kinase_entrez_gene_symbol_lower_idx ON :schema.kinase (lower(entrez_gene_symbol));


DROP TABLE IF EXISTS :schema.kinase_activity_profile CASCADE;
CREATE TABLE :schema.kinase_activity_profile (
  id serial,
  compound_nm character varying(100) NOT NULL,
  kinase integer NOT NULL,
  percent_control numeric,
  compound_concentration integer,
  kd numeric,
  CONSTRAINT kinase_activity_profile_pkey PRIMARY KEY (id),
  CONSTRAINT kinase_activity_profile_compound_fk FOREIGN KEY (compound_nm)
    REFERENCES :schema.compound (compound_nm) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT kinase_activity_profile_kinase_fk FOREIGN KEY (kinase)
    REFERENCES :schema.kinase (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH ( OIDS = FALSE );

CREATE INDEX kinase_activity_profile_compound_nm_idx ON :schema.kinase_activity_profile (compound_nm);
CREATE INDEX kinase_activity_profile_compound_nm_lower_idx ON :schema.kinase_activity_profile (lower(compound_nm));
CREATE INDEX kinase_activity_profile_kinase_idx on :schema.kinase_activity_profile (kinase);
CREATE INDEX kinase_activity_profile_percent_control_idx on :schema.kinase_activity_profile (percent_control);


DROP TABLE IF EXISTS :schema.nanobret_activity_profile CASCADE;
CREATE TABLE :schema.nanobret_activity_profile (
  id serial,
  compound_nm character varying(100) NOT NULL,
  kinase integer NOT NULL,
  nluc_orientation character varying(10) NOT NULL,
  modifier character varying(1) NOT NULL,
  ic50 numeric NOT NULL,
  nanobret_percent_inhibition numeric NOT NULL,
  concentration integer NOT NULL,
  points integer NOT NULL,
  comment character varying(512),
  run_date date NOT NULL,
  CONSTRAINT nanobret_activity_profile_pkey PRIMARY KEY (id),
  CONSTRAINT nanobret_activity_profile_compound_fk FOREIGN KEY (compound_nm)
    REFERENCES :schema.compound (compound_nm) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT nanobret_activity_profile_kinase_fk FOREIGN KEY (kinase)
    REFERENCES :schema.kinase (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT nanobret_activity_profile_daily_run_key UNIQUE (compound_nm, kinase, run_date)
)
WITH ( OIDS = FALSE );

CREATE INDEX nanobret_activity_profile_compound_nm_idx ON :schema.nanobret_activity_profile (compound_nm);
CREATE INDEX nanobret_activity_profile_compound_nm_lower_idx ON :schema.nanobret_activity_profile (lower(compound_nm));
CREATE INDEX nanobret_activity_profile_kinase_idx on :schema.nanobret_activity_profile (kinase);


DROP TABLE IF EXISTS :schema.blog_post CASCADE;
CREATE TABLE :schema.blog_post (
  blog_post_id serial,
  title character varying(128) NOT NULL,
  body character varying(1048576) NOT NULL,
  create_dttm TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  view_count integer NOT NULL DEFAULT 0,
  CONSTRAINT blog_post_pkey PRIMARY KEY (blog_post_id)
)
WITH ( OIDS = FALSE );

CREATE INDEX blog_post_create_dttm_idx ON :schema.blog_post (create_dttm);


DROP TABLE IF EXISTS :schema.feedback CASCADE;
CREATE TABLE :schema.feedback (
  feedback_id serial,
  email character varying(254),
  ip_address character varying(39),
  title character varying(128) NOT NULL,
  body character varying(8000) NOT NULL,
  create_dttm TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT feedback_pkey PRIMARY KEY (feedback_id)
)
WITH ( OIDS = FALSE );

CREATE INDEX feedback_create_dttm_idx ON :schema.blog_post (create_dttm);


DROP TABLE IF EXISTS :schema.partner CASCADE;
CREATE TABLE :schema.partner (
  id serial,
  name character varying(64) UNIQUE NOT NULL,
  url character varying(2048) UNIQUE NOT NULL,
  image character varying(80) UNIQUE NOT NULL,
  CONSTRAINT partner_pkey PRIMARY KEY (id)
)
WITH ( OIDS = FALSE );

CREATE INDEX partner_name_idx ON :schema.partner (name);


DROP TABLE IF EXISTS :schema.audit CASCADE;
CREATE TABLE :schema.audit (
  id serial,
  user_nm character varying(40) NOT NULL,
  action character varying(40) NOT NULL,
  ip_address character varying(39),
  create_dttm TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  success boolean NOT NULL DEFAULT TRUE,
  details character varying(500),
  CONSTRAINT audit_pk PRIMARY KEY (id)
)
WITH ( OIDS = FALSE );

CREATE INDEX audit_user_nm_idx ON :schema.audit (user_nm);
CREATE INDEX audit_action_idx ON :schema.audit (action);
CREATE INDEX audit_create_dttm_idx ON :schema.audit (create_dttm);
CREATE INDEX success ON :schema.audit (success);
