DROP TABLE IF EXISTS public.compound CASCADE;
CREATE TABLE public.compound (
    compound_nm character varying(100) NOT NULL,
    chemotype character varying(100),
    s_10 numeric,
    source character varying(12),
    smiles character varying(2048),
    CONSTRAINT compound_pkey PRIMARY KEY (compound_nm)
)
WITH ( OIDS = FALSE );

CREATE INDEX compound_lower_idx
    ON public.compound USING btree (lower(compound_nm::text));


DROP TABLE IF EXISTS public.kinase CASCADE;
CREATE TABLE public.kinase (
  id serial,
  discoverx_gene_symbol character varying(100) NOT NULL,
  entrez_gene_symbol character varying(100) NOT NULL,
  CONSTRAINT kinase_pkey PRIMARY KEY (id),
  CONSTRAINT kinase_discoverx_gene_symbol_entrez_gene_symbol_key UNIQUE (discoverx_gene_symbol, entrez_gene_symbol)
)
WITH ( OIDS = FALSE );


DROP TABLE IF EXISTS public.kinase_activity_profile CASCADE;
CREATE TABLE public.kinase_activity_profile (
  id serial,
  compound_nm character varying(100) NOT NULL,
  kinase integer NOT NULL,
  percent_control numeric,
  compound_concentration integer,
  kd numeric,
  CONSTRAINT kinase_activity_profile_pkey PRIMARY KEY (id),
  CONSTRAINT compoundfk FOREIGN KEY (compound_nm)
  REFERENCES public.compound (compound_nm) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT kinasefk FOREIGN KEY (kinase)
  REFERENCES public.kinase (id) ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH ( OIDS = FALSE );

CREATE INDEX kinase_activity_profile_lower_idx
  ON public.kinase_activity_profile USING btree (lower(compound_nm::text));
