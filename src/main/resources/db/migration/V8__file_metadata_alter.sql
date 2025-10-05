ALTER TABLE files_metadata
DROP COLUMN file_url;

ALTER TABLE files_metadata
ADD COLUMN parent_id UUID
NULL REFERENCES files_metadata(id) ON DELETE CASCADE;

ALTER TABLE files_metadata
ADD CHECK (file_type = 'DIRECTORY');

ALTER TABLE files_metadata
ADD COLUMN size BIGINT NOT NULL;

ALTER TABLE files_metadata
ADD COLUMN creation_time TIMESTAMP DEFAULT now();

ALTER TABLE files_metadata
ADD CONSTRAINT uq_name_user_id_parent_id UNIQUE(name, owner_id, parent_id);