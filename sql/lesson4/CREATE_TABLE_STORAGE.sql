--ORCL
CREATE TABLE storage
(
    id                NUMBER PRIMARY KEY,
    formats_supported NVARCHAR2(50) NOT NULL,
    country           NVARCHAR2(50) NOT NULL,
    storage_size      NUMBER CHECK (storage_size > 0)
);

--POSGRES
/*
 CREATE TABLE storage
(
    id                BIGINT PRIMARY KEY,
    formats_supported VARCHAR(50) NOT NULL,
    country           VARCHAR(50) NOT NULL,
    storage_size      INT CHECK (storage_size > 0)
);
 */
