ALTER TABLE files_metadata
DROP CONSTRAINT uq_name_user_id_parent_id;

CREATE UNIQUE INDEX uq_files_no_parent
ON files_metadata (name, owner_id)
WHERE parent_id IS NULL;

CREATE UNIQUE INDEX uq_files_with_parent
ON files_metadata (name, owner_id, parent_id)
WHERE parent_id IS NOT NULL;