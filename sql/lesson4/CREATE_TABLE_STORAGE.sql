CREATE TABLE storage
(
    id                NUMBER PRIMARY KEY,
    formats_supported NVARCHAR2(50) NOT NULL,
    country           NVARCHAR2(50) NOT NULL,
    storage_size      NUMBER CHECK (storage_size > 0)
);
