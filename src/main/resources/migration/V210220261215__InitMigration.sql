CREATE TABLE factura
(
    id_factura       BIGINT AUTO_INCREMENT NOT NULL,
    id_mantenimiento BIGINT NOT NULL,
    fecha_emision    date NULL,
    importe          DECIMAL NULL,
    nombre_taller    VARCHAR(255) NULL,
    ruta_imagen      VARCHAR(255) NULL,
    ocr_texto        VARCHAR(255) NULL,
    CONSTRAINT pk_factura PRIMARY KEY (id_factura)
);

CREATE TABLE mantenimiento
(
    id_mantenimiento BIGINT AUTO_INCREMENT NOT NULL,
    id_vehiculo      BIGINT       NOT NULL,
    tipo             VARCHAR(255) NOT NULL,
    fecha            date         NOT NULL,
    kilometraje      INT          NOT NULL,
    taller           VARCHAR(255) NULL,
    notas            VARCHAR(255) NULL,
    CONSTRAINT pk_mantenimiento PRIMARY KEY (id_mantenimiento)
);

CREATE TABLE repostaje
(
    id_repostaje BIGINT AUTO_INCREMENT NOT NULL,
    id_vehiculo  BIGINT  NOT NULL,
    fecha        date    NOT NULL,
    kilometraje  INT     NOT NULL,
    litros       DECIMAL NOT NULL,
    precio_litro DECIMAL NOT NULL,
    gasolinera   VARCHAR(255) NULL,
    CONSTRAINT pk_repostaje PRIMARY KEY (id_repostaje)
);

CREATE TABLE usuario
(
    id_usuario    BIGINT AUTO_INCREMENT NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre        VARCHAR(255) NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario)
);

CREATE TABLE vehiculo
(
    id_vehiculo BIGINT AUTO_INCREMENT NOT NULL,
    id_usuario  BIGINT       NOT NULL,
    matricula   VARCHAR(255) NOT NULL,
    marca       VARCHAR(255) NOT NULL,
    modelo      VARCHAR(255) NOT NULL,
    anio        INT NULL,
    combustible VARCHAR(255) NOT NULL,
    kilometraje INT          NOT NULL,
    color       VARCHAR(255) NULL,
    CONSTRAINT pk_vehiculo PRIMARY KEY (id_vehiculo)
);

ALTER TABLE factura
    ADD CONSTRAINT uc_factura_id_mantenimiento UNIQUE (id_mantenimiento);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE factura
    ADD CONSTRAINT FK_FACTURA_ON_ID_MANTENIMIENTO FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento (id_mantenimiento);

ALTER TABLE mantenimiento
    ADD CONSTRAINT FK_MANTENIMIENTO_ON_ID_VEHICULO FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo);

ALTER TABLE repostaje
    ADD CONSTRAINT FK_REPOSTAJE_ON_ID_VEHICULO FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo);

ALTER TABLE vehiculo
    ADD CONSTRAINT FK_VEHICULO_ON_ID_USUARIO FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);