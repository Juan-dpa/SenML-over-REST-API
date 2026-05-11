DROP TABLE IF EXISTS mediciones;
DROP TABLE IF EXISTS dispositivos;
DROP TABLE IF EXISTS parcelas;

CREATE TABLE parcelas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150)
);

CREATE TABLE dispositivos (
    id SERIAL PRIMARY KEY,
    parcela_id INTEGER REFERENCES parcelas(id),
    urn VARCHAR(150) NOT NULL,
    tipo VARCHAR(50),
    descripcion VARCHAR(200)
);

CREATE TABLE mediciones (
    id SERIAL PRIMARY KEY,
    parcela_id INTEGER REFERENCES parcelas(id),
    dispositivo_urn VARCHAR(150),
    magnitud VARCHAR(80),
    unidad VARCHAR(30),
    valor_numerico DOUBLE PRECISION,
    valor_booleano BOOLEAN,
    valor_texto VARCHAR(250),
    timestamp_epoch DOUBLE PRECISION
);
