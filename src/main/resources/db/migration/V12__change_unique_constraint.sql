ALTER TABLE files_metadata
DROP CONSTRAINT files_metadata_full_path_key;

CREATE UNIQUE INDEX uq_files_user_id_full_path
ON files_metadata (owner_id, full_path);