insert into compound (compound_nm, chemotype, smiles, s_10, hidden) values
  ('compoundC', 'chemotypeC', 'smilesC', 0.3, false),
  ('compoundB', 'chemotypeB', NULL, 0.4, true),
  ('compoundA', 'chemotypeA', 'smilesA', NULL, false)
;


insert into kinase (id, discoverx_gene_symbol, entrez_gene_symbol, discoverx_url) values
  (1, 'kinaseA', 'kinaseA', 'http://discoverx.com/kinaseA'),
  (2, 'kinaseB', 'kinaseB', 'http://discoverx.com/kinaseB'),
  (3, 'kinaseC', 'kinaseC', 'http://discoverx.com/kinaseC')
;


insert into kinase_activity_profile (compound_nm, kinase, percent_control, compound_concentration, kd) values
  ('compoundA', 1, 0.3, 54, 100),
  ('compoundB', 2, 0.4, 54, 100),
  ('compoundC', 3, 0.5, 54, 100)
;
