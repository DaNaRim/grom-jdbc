CREATE TABLE product
(
    id          NUMBER,
    name        NVARCHAR2(20) NOT NULL,
    description CLOB,
    price       NUMBER        NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (id)
);
