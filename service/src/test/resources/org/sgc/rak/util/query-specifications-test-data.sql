insert into audit(user_nm, action, ip_address, create_dttm, success, details) values
  ('gclooney', 'LOGIN', '1.2.3.4', '2019-02-06T17:42:00Z', true, NULL),
  ('capplegate', 'UPDATE_COMPOUND', '5.6.7.8', '2019-08-03T00:00:00Z', false, NULL)
;

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
