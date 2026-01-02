--changeset shohamr:init

CREATE TABLE chat_user
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    name        VARCHAR(255),
    password    VARCHAR(255),
    active      BOOLEAN DEFAULT TRUE,
    created_on  TIMESTAMP    NOT NULL,
    modified_on TIMESTAMP    NOT NULL
);

CREATE TABLE chat_group
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    group_name  VARCHAR(255) NOT NULL,
    group_code  VARCHAR(255) NOT NULL UNIQUE,
    active      BOOLEAN DEFAULT TRUE,
    created_on  TIMESTAMP    NOT NULL,
    modified_on TIMESTAMP    NOT NULL
);

CREATE TABLE chat
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    message     TEXT      NOT NULL,
    sender_id   BIGINT    REFERENCES chat_user (id) ON DELETE SET NULL,
    group_id    BIGINT REFERENCES chat_group (id) ON DELETE CASCADE,
    created_on  TIMESTAMP NOT NULL,
    modified_on TIMESTAMP NOT NULL
);

CREATE TABLE group_user_association
(
    group_id BIGINT NOT NULL REFERENCES chat_group (id) ON DELETE CASCADE,
    user_id  BIGINT NOT NULL REFERENCES chat_user (id) ON DELETE CASCADE,
    PRIMARY KEY (group_id, user_id)
);

