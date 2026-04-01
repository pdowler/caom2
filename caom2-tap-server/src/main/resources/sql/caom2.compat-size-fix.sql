-- backwards compatible: fill "size" column with values from arraysize set above
-- where arraysize is a possibly variable-length 1-dimensional value
update tap_schema.columns11 SET "size" = replace(arraysize::varchar,'*','')::int 
WHERE table_name LIKE 'caom2.%'
  AND arraysize IS NOT NULL
  AND arraysize NOT LIKE '%x%'
  AND arraysize != '*';
