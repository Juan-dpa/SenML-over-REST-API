INSERT INTO parcelas (nombre, ubicacion) VALUES ('Invernadero A', 'Murcia');
INSERT INTO parcelas (nombre, ubicacion) VALUES ('Campo Exterior', 'Almeria');

INSERT INTO dispositivos (parcela_id, urn, tipo, descripcion)
    VALUES (1, 'urn:dev:greenhouse-01', 'sensor', 'Sensor ambiental invernadero A');
INSERT INTO dispositivos (parcela_id, urn, tipo, descripcion)
    VALUES (1, 'urn:dev:greenhouse-02', 'actuador', 'Valvula de riego invernadero A');
INSERT INTO dispositivos (parcela_id, urn, tipo, descripcion)
    VALUES (2, 'urn:dev:field-01', 'sensor', 'Sensor de suelo campo exterior');

INSERT INTO mediciones (parcela_id, dispositivo_urn, magnitud, unidad, valor_numerico, timestamp_epoch)
    VALUES (1, 'urn:dev:greenhouse-01', 'temperature', 'Cel', 24.5, 1778495400.0);
INSERT INTO mediciones (parcela_id, dispositivo_urn, magnitud, unidad, valor_numerico, timestamp_epoch)
    VALUES (1, 'urn:dev:greenhouse-01', 'air_humidity', '%RH', 62.3, 1778495400.0);
INSERT INTO mediciones (parcela_id, dispositivo_urn, magnitud, unidad, valor_numerico, timestamp_epoch)
    VALUES (1, 'urn:dev:greenhouse-01', 'soil_moisture', '1', 0.45, 1778495460.0);
INSERT INTO mediciones (parcela_id, dispositivo_urn, magnitud, unidad, valor_booleano, timestamp_epoch)
    VALUES (1, 'urn:dev:greenhouse-02', 'irrigation_valve_open', NULL, false, 1778495400.0);
INSERT INTO mediciones (parcela_id, dispositivo_urn, magnitud, unidad, valor_numerico, timestamp_epoch)
    VALUES (2, 'urn:dev:field-01', 'soil_moisture', '1', 0.28, 1778495500.0);
INSERT INTO mediciones (parcela_id, dispositivo_urn, magnitud, unidad, valor_numerico, timestamp_epoch)
    VALUES (2, 'urn:dev:field-01', 'temperature', 'Cel', 31.2, 1778495500.0);
