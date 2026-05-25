CREATE TABLE cita
(
    id_cita                BIGINT AUTO_INCREMENT NOT NULL,
    id_vehiculo            BIGINT       NOT NULL,
    id_tipo_mantenimiento  BIGINT NULL,
    titulo                 VARCHAR(255) NOT NULL,
    fecha_programada       date         NOT NULL,
    kilometraje_programado INT NULL,
    notas                  VARCHAR(255) NULL,
    completada             BIT(1) NULL,
    recordatorio_enviado   BIT(1) NULL,
    CONSTRAINT pk_cita PRIMARY KEY (id_cita)
);

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
    id_vehiculo      BIGINT NOT NULL,
    id_tipo          BIGINT NULL,
    id_taller        BIGINT NULL,
    tipo_enum        VARCHAR(255) NULL,
    fecha            date   NOT NULL,
    kilometraje      INT    NOT NULL,
    notas            VARCHAR(255) NULL,
    nombre_taller    VARCHAR(255) NULL,
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

CREATE TABLE seguro
(
    id_seguro           BIGINT AUTO_INCREMENT NOT NULL,
    id_vehiculo         BIGINT       NOT NULL,
    compania            VARCHAR(255) NOT NULL,
    numero_poliza       VARCHAR(255) NOT NULL,
    fecha_inicio        date NULL,
    fecha_fin           date NULL,
    telefono_asistencia VARCHAR(255) NULL,
    coberturas          VARCHAR(255) NULL,
    precio_anual DOUBLE NULL,
    activo              BIT(1) NULL,
    CONSTRAINT pk_seguro PRIMARY KEY (id_seguro)
);

CREATE TABLE taller
(
    id_taller BIGINT AUTO_INCREMENT NOT NULL,
    nombre    VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NULL,
    latitud   DECIMAL NULL,
    longitud  DECIMAL NULL,
    telefono  VARCHAR(255) NULL,
    email     VARCHAR(255) NULL,
    web       VARCHAR(255) NULL,
    CONSTRAINT pk_taller PRIMARY KEY (id_taller)
);

CREATE TABLE tipo_mantenimiento
(
    id_tipo                 BIGINT AUTO_INCREMENT NOT NULL,
    nombre_tipo             VARCHAR(255) NOT NULL,
    descripcion             VARCHAR(255) NULL,
    intervalo_km_default    INT NULL,
    intervalo_meses_default INT NULL,
    CONSTRAINT pk_tipo_mantenimiento PRIMARY KEY (id_tipo)
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

ALTER TABLE seguro
    ADD CONSTRAINT uc_seguro_id_vehiculo UNIQUE (id_vehiculo);

ALTER TABLE seguro
    ADD CONSTRAINT uc_seguro_numero_poliza UNIQUE (numero_poliza);

ALTER TABLE tipo_mantenimiento
    ADD CONSTRAINT uc_tipo_mantenimiento_nombre_tipo UNIQUE (nombre_tipo);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE cita
    ADD CONSTRAINT FK_CITA_ON_ID_TIPO_MANTENIMIENTO FOREIGN KEY (id_tipo_mantenimiento) REFERENCES tipo_mantenimiento (id_tipo);

ALTER TABLE cita
    ADD CONSTRAINT FK_CITA_ON_ID_VEHICULO FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo);

ALTER TABLE factura
    ADD CONSTRAINT FK_FACTURA_ON_ID_MANTENIMIENTO FOREIGN KEY (id_mantenimiento) REFERENCES mantenimiento (id_mantenimiento);

ALTER TABLE mantenimiento
    ADD CONSTRAINT FK_MANTENIMIENTO_ON_ID_TALLER FOREIGN KEY (id_taller) REFERENCES taller (id_taller);

ALTER TABLE mantenimiento
    ADD CONSTRAINT FK_MANTENIMIENTO_ON_ID_TIPO FOREIGN KEY (id_tipo) REFERENCES tipo_mantenimiento (id_tipo);

ALTER TABLE mantenimiento
    ADD CONSTRAINT FK_MANTENIMIENTO_ON_ID_VEHICULO FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo);

ALTER TABLE repostaje
    ADD CONSTRAINT FK_REPOSTAJE_ON_ID_VEHICULO FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo);

ALTER TABLE seguro
    ADD CONSTRAINT FK_SEGURO_ON_ID_VEHICULO FOREIGN KEY (id_vehiculo) REFERENCES vehiculo (id_vehiculo);

ALTER TABLE vehiculo
    ADD CONSTRAINT FK_VEHICULO_ON_ID_USUARIO FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);