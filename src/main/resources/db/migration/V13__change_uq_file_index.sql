DROP INDEX uq_files_no_parent;

DROP INDEX uq_files_with_parent;

CREATE UNIQUE INDEX uq_files_no_parent
ON files_metadata (name, owner_id, file_type)
WHERE parent_id IS NULL;

CREATE UNIQUE INDEX uq_files_with_parent
ON files_metadata (name, owner_id, parent_id, file_type)
WHERE parent_id IS NOT NULL;