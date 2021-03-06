START TRANSACTION;

ALTER TABLE discovery_view ADD COLUMN IF NOT EXISTS ascending boolean;
UPDATE discovery_view SET ascending = true;
ALTER TABLE discovery_view ALTER COLUMN ascending SET NOT NULL;
ALTER TABLE discovery_view ALTER COLUMN filter TYPE character varying(2048);
ALTER TABLE discovery_view ADD COLUMN IF NOT EXISTS published boolean;
UPDATE discovery_view SET published = true;
ALTER TABLE discovery_view ALTER COLUMN published SET NOT NULL;
ALTER TABLE operator ALTER COLUMN value DROP NOT NULL;

COMMIT;