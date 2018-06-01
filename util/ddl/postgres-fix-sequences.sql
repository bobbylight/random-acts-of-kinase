SET SCHEMA :'schema';
--
-- Previously exported data includes serial PK values, so we bump our
-- sequences manually.
--

SELECT setval(pg_get_serial_sequence('kinase_activity_profile', 'id'),
              COALESCE((SELECT MAX(id)+1 FROM :schema.kinase_activity_profile), 1), false);

SELECT setval(pg_get_serial_sequence('blog_post', 'blog_post_id'),
              COALESCE((SELECT MAX(blog_post_id)+1 FROM :schema.blog_post), 1), false);

SELECT setval(pg_get_serial_sequence('feedback', 'feedback_id'),
              COALESCE((SELECT MAX(feedback_id)+1 FROM :schema.feedback), 1), false);

SELECT setval(pg_get_serial_sequence('partner', 'id'),
              COALESCE((SELECT MAX(id)+1 FROM :schema.partner), 1), false);
