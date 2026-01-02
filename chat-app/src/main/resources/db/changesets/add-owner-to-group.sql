-- liquibase formatted sql

-- changeset shoham:add-owner-to-group
ALTER TABLE chat_group
    ADD COLUMN owner_id BIGINT NOT NULL;

ALTER TABLE chat_group
    ADD CONSTRAINT fk_group_owner
        FOREIGN KEY (owner_id) REFERENCES chat_user(id) ON DELETE RESTRICT;
