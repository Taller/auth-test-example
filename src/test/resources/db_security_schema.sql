CREATE SCHEMA "SECURITY";

SET SCHEMA "SECURITY";
SET SCHEMA_SEARCH_PATH "SECURITY";

CREATE TABLE "CREDENTIALS" (
    "subj_id" bigint NOT NULL,
    "auth" VARCHAR(128) NOT NULL,
    "key" VARCHAR(128) NOT NULL,
    "has_ui_access" boolean DEFAULT false NOT NULL,
    "has_rpc_access" boolean DEFAULT false NOT NULL
);


CREATE TABLE "GROUPS4USERS" (
    "link_id" bigint NOT NULL,
    "group_id" bigint NOT NULL,
    "user_id" bigint NOT NULL
);

CREATE TABLE "OBJECTS" (
    "obj_id" bigint NOT NULL,
    "name" VARCHAR(128) NOT NULL,
    "type" VARCHAR(32) NOT NULL
);

CREATE TABLE "OPERATIONS" (
    "ops_id" bigint NOT NULL,
    "create" boolean NOT NULL,
    "read" boolean NOT NULL,
    "update" boolean NOT NULL,
    "delete" boolean NOT NULL
);

CREATE TABLE "PERMISSION" (
    "permission_id" bigint NOT NULL,
    "obj_id" bigint NOT NULL,
    "subj_id" bigint NOT NULL,
    "ops_id" bigint NOT NULL
);


CREATE TABLE "SUBJECTS" (
    "subj_id" bigint NOT NULL,
    "type" VARCHAR(32) NOT NULL,
    "name" VARCHAR(128) NOT NULL
);

ALTER TABLE "CREDENTIALS"
    ADD CONSTRAINT "credentials_auth_key" UNIQUE ("auth");

ALTER TABLE "CREDENTIALS"
    ADD CONSTRAINT "credentials_pkey" PRIMARY KEY ("subj_id");

ALTER TABLE "CREDENTIALS"
    ADD CONSTRAINT "credentials_subj_id_key" UNIQUE ("subj_id");


ALTER TABLE "GROUPS4USERS"
    ADD CONSTRAINT "groups4users_pkey" PRIMARY KEY ("link_id");

ALTER TABLE "OBJECTS"
    ADD CONSTRAINT "objects_name_type_key" UNIQUE ("name", "type");


ALTER TABLE "OBJECTS"
    ADD CONSTRAINT "objects_pkey" PRIMARY KEY ("obj_id");


ALTER TABLE "OPERATIONS"
    ADD CONSTRAINT "operations_create_read_update_delete_key" UNIQUE ("create", "read", "update", "delete");


ALTER TABLE "OPERATIONS"
    ADD CONSTRAINT "operations_pkey" PRIMARY KEY ("ops_id");

ALTER TABLE "PERMISSION"
    ADD CONSTRAINT "permission_obj_id_ops_id_key" UNIQUE ("obj_id", "ops_id");

ALTER TABLE "PERMISSION"
    ADD CONSTRAINT "permission_obj_id_subj_id_ops_id_key" UNIQUE ("obj_id", "subj_id", "ops_id");

ALTER TABLE "PERMISSION"
    ADD CONSTRAINT "permission_pkey" PRIMARY KEY ("permission_id");


ALTER TABLE "SUBJECTS"
    ADD CONSTRAINT "subjects_pkey" PRIMARY KEY ("subj_id");

CREATE INDEX "fki_link_to_group" ON "GROUPS4USERS" ("group_id");

CREATE INDEX "fki_link_to_user" ON "GROUPS4USERS" ("user_id");

CREATE INDEX "fki_permission_ops_id_fkey" ON "PERMISSION" ("ops_id");

CREATE INDEX "fki_permission_subj_id_fkey" ON "PERMISSION" ("subj_id");

ALTER TABLE "GROUPS4USERS"
    ADD CONSTRAINT "link_to_group" FOREIGN KEY ("group_id") REFERENCES "SUBJECTS"("subj_id");

ALTER TABLE "GROUPS4USERS"
    ADD CONSTRAINT "link_to_user" FOREIGN KEY ("user_id") REFERENCES "SUBJECTS"("subj_id");

ALTER TABLE "PERMISSION"
    ADD CONSTRAINT "permission_obj_id_fkey" FOREIGN KEY ("obj_id") REFERENCES "OBJECTS"("obj_id");

ALTER TABLE "PERMISSION"
    ADD CONSTRAINT "permission_ops_id_fkey" FOREIGN KEY ("ops_id") REFERENCES "OPERATIONS"("ops_id");

ALTER TABLE "PERMISSION"
    ADD CONSTRAINT "permission_subj_id_fkey" FOREIGN KEY ("subj_id") REFERENCES "SUBJECTS"("subj_id");


INSERT INTO "OPERATIONS"
("ops_id", "create", "read", "update", "delete") VALUES
(1,        true,     true,   true,     true),
(2,        true,     true,   true,     false),
(3,        true,     true,   false,    true),
(4,        true,     false,  true,     true),
(5,        false,    true,   true,     true),
(6,        true,     true,   false,    false),
(7,        true,     false,  false,    true),
(8,        false,    false,  true,     true),
(9,        false,    true,   true,     false),
(10,       false,    true,   false,    true),
(11,       true,     false,  true,     false),
(12,       true,     false,  false,    false),
(13,       false,    false,  false,    true),
(14,       false,    false,  true,     false),
(15,       false,    true,   false,    false),
(16,       false,    false,  false,    false);

INSERT INTO "CREDENTIALS"
("subj_id", "auth",  "key", "has_ui_access", "has_rpc_access") VALUES
(2,         'auth',  'key',  true,           true),
(4,         'auth1', 'key1', true,           true),
(5,         'auth2', 'key2', true,           true);

INSERT INTO "SUBJECTS"
("subj_id", "type",  "name") VALUES
(1,         'GROUP', 'admin'),
(2,         'USER',  'admin'),
(3,         'GROUP', 'all'),
(4,         'USER',  'user1'),
(5,         'USER',  'user2');


INSERT INTO "GROUPS4USERS"
("link_id", "group_id", "user_id") VALUES
(1,         1,          2),
(2,         3,          2),
(3,         3,          4),
(4,         3,          5);


INSERT INTO "OBJECTS"
("obj_id", "name",           "type") VALUES
(0,        'all',            'TABLE'),
(1,        'security',       'SCHEMA'),
(2,        'public.table11', 'TABLE'),
(3,        'public.table21', 'TABLE'),
(4,        'public.table31', 'TABLE'),
(5,        'public.table41', 'TABLE'),
(6,        'public.table51', 'TABLE'),
(7,        'public.table12', 'TABLE'),
(8,        'public.table22', 'TABLE'),
(9,        'public.table32', 'TABLE'),
(10,       'public.table42', 'TABLE'),
(11,       'public.table52', 'TABLE');

INSERT INTO "PERMISSION"
("permission_id", "obj_id", "subj_id", "ops_id") VALUES
(1,               1,        2,         16),
(2,               2,        4,         1),
(3,               3,        4,         1),
(4,               4,        4,         1),
(5,               5,        4,         1),
(6,               6,        4,         1),
(7,               7,        5,         1),
(8,               8,        5,         1),
(9,               9,        5,         1),
(10,              10,       5,         1),
(11,              11,       5,         1);