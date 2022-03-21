-- ORCL
CREATE TABLE files
(
    id         NUMBER PRIMARY KEY,
    name       NVARCHAR2(10) UNIQUE NOT NULL,
    format     NVARCHAR2(20)        NOT NULL,
    file_size  NUMBER CHECK (file_size > 0),
    storage_id NUMBER,
    CONSTRAINT storage_fk FOREIGN KEY (storage_id) REFERENCES storage (id)
);

--POSGRES
/*
 CREATE TABLE files
(
    id         BIGINT PRIMARY KEY,
    name       VARCHAR(10) UNIQUE NOT NULL,
    format     VARCHAR(20)        NOT NULL,
    file_size  INT CHECK (file_size > 0),
    storage_id BIGINT,
    CONSTRAINT storage_fk FOREIGN KEY (storage_id) REFERENCES storage (id)
);
 */
