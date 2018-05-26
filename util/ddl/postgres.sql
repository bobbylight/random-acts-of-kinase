CREATE SCHEMA IF NOT EXISTS :schema;
SET SCHEMA :'schema';

DROP TABLE IF EXISTS :schema.compound CASCADE;
CREATE TABLE :schema.compound (
  compound_nm character varying(100) NOT NULL,
  chemotype character varying(100),
  s_10 numeric,
  source character varying(12),
  smiles character varying(2048),
  reference_1 character varying(512),
  reference_1_url character varying(2048),
  CONSTRAINT compound_pkey PRIMARY KEY (compound_nm)
)
WITH ( OIDS = FALSE );

CREATE INDEX compound_compound_nm_idx ON :schema.compound (compound_nm);
CREATE INDEX compound_compound_nm_lower_idx ON :schema.compound (lower(compound_nm));


DROP TABLE IF EXISTS :schema.kinase CASCADE;
CREATE TABLE :schema.kinase (
  id serial,
  discoverx_gene_symbol character varying(100) NOT NULL,
  entrez_gene_symbol character varying(100) NOT NULL,
  CONSTRAINT kinase_pkey PRIMARY KEY (id),
  CONSTRAINT kinase_discoverx_gene_symbol_entrez_gene_symbol_key UNIQUE (discoverx_gene_symbol, entrez_gene_symbol)
)
WITH ( OIDS = FALSE );

CREATE INDEX kinase_discoverx_gene_symbol_idx ON :schema.kinase (discoverx_gene_symbol);
CREATE INDEX kinase_discoverx_gene_symbol_lower_idx ON :schema.kinase (lower(discoverx_gene_symbol));


DROP TABLE IF EXISTS :schema.kinase_activity_profile CASCADE;
CREATE TABLE :schema.kinase_activity_profile (
  id serial,
  compound_nm character varying(100) NOT NULL,
  kinase integer NOT NULL,
  percent_control numeric,
  compound_concentration integer,
  kd numeric,
  CONSTRAINT kinase_activity_profile_pkey PRIMARY KEY (id),
  CONSTRAINT compoundfk FOREIGN KEY (compound_nm)
  REFERENCES :schema.compound (compound_nm) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT kinasefk FOREIGN KEY (kinase)
  REFERENCES :schema.kinase (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH ( OIDS = FALSE );

CREATE INDEX kinase_activity_profile_compound_nm_idx ON :schema.kinase_activity_profile (compound_nm);
CREATE INDEX kinase_activity_profile_compound_nm_lower_idx ON :schema.kinase_activity_profile (lower(compound_nm));
CREATE INDEX kinase_activity_profile_kinase_idx on :schema.kinase_activity_profile (kinase);
CREATE INDEX kinase_activity_profile_percent_control_idx on :schema.kinase_activity_profile (percent_control);


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
  title character varying(128) NOT NULL,
  body character varying(16384) NOT NULL,
  create_dttm TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT feedback_pkey PRIMARY KEY (feedback_id)
)
WITH ( OIDS = FALSE );

CREATE INDEX feedback_create_dttm_idx ON :schema.blog_post (create_dttm);
