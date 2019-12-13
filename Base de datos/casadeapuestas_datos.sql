GO
USE CasaDeApuestas
GO

INSERT INTO Usuarios VALUES ('test@test.test', 'tst', 100000)
INSERT INTO Usuarios VALUES ('test2@test.test', 'tst2', 100000)

INSERT INTO Equipos VALUES 
('Real Betis Balompie'),
('Sevilla FC'),
('Real Madrid'),
('Barcelona FC'),
('Atletico de Madrid'),
('Valencia CF'),
('Real Sociedad'),
('Celta de Vigo')


DECLARE @IDPartidoAleatorio AS uniqueidentifier
--DECLARE @IDApuesta AS uniqueidentifier

SET @IDPartidoAleatorio = NEWID()
INSERT INTO Partidos VALUES 
(@IDPartidoAleatorio, 'Liga', 1, 0, CURRENT_TIMESTAMP, DATEADD(minute, 105, CURRENT_TIMESTAMP), 1, 250000, 125000, 89000)

INSERT INTO EquiposPartidos VALUES 
('Sevilla FC', @IDPartidoAleatorio, 'L'),
('Real Sociedad', @IDPartidoAleatorio, 'V')

--Apuestas ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 23,
		@cuota = 2.05,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 1,
		@GolesVisitante = 0

EXECUTE RealizarApuestaTipo2
		@cantidad = 5,
		@cuota = 2.15,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'L',
		@Goles = 1

EXECUTE RealizarApuestaTipo3
		@cantidad = 34.15,
		@cuota = 1.70,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = '1'


--Apuestas no ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 13,
		@cuota = 1.83,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 0,
		@GolesVisitante = 2

EXECUTE RealizarApuestaTipo2
		@cantidad = 7.20,
		@cuota = 1.45,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'V',
		@Goles = 3

EXECUTE RealizarApuestaTipo3
		@cantidad = 4.13,
		@cuota = 2.5,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = 'X'


SET @IDPartidoAleatorio = NEWID()
INSERT INTO Partidos VALUES 
(@IDPartidoAleatorio, 'Liga', 3, 1, CURRENT_TIMESTAMP, DATEADD(minute, 105, CURRENT_TIMESTAMP), 1, 99250, 105000, 88000)

INSERT INTO EquiposPartidos VALUES 
('Celta de Vigo', @IDPartidoAleatorio, 'L'),
('Valencia CF', @IDPartidoAleatorio, 'V')

--Apuestas ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 23,
		@cuota = 2.05,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 3,
		@GolesVisitante = 1

EXECUTE RealizarApuestaTipo2
		@cantidad = 5,
		@cuota = 2.15,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'L',
		@Goles = 3

EXECUTE RealizarApuestaTipo3
		@cantidad = 34.15,
		@cuota = 1.70,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = '1'

--Apuestas no ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 13,
		@cuota = 1.83,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 0,
		@GolesVisitante = 2

EXECUTE RealizarApuestaTipo2
		@cantidad = 7.20,
		@cuota = 1.45,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'V',
		@Goles = 3

EXECUTE RealizarApuestaTipo3
		@cantidad = 4.13,
		@cuota = 2.5,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = 'X'


SET @IDPartidoAleatorio = NEWID()
INSERT INTO Partidos VALUES 
(@IDPartidoAleatorio, 'Liga', 0, 0, CURRENT_TIMESTAMP, DATEADD(minute, 105, CURRENT_TIMESTAMP), 1, 300000, 335000, 59000)

INSERT INTO EquiposPartidos VALUES 
('Valencia CF', @IDPartidoAleatorio, 'L'),
('Real Madrid', @IDPartidoAleatorio, 'V')

--Apuestas ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 23,
		@cuota = 2.05,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 0,
		@GolesVisitante = 0

EXECUTE RealizarApuestaTipo2
		@cantidad = 5,
		@cuota = 2.15,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'L',
		@Goles = 0

EXECUTE RealizarApuestaTipo3
		@cantidad = 34.15,
		@cuota = 1.70,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = 'X'

--Apuestas no ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 13,
		@cuota = 1.83,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 0,
		@GolesVisitante = 2

EXECUTE RealizarApuestaTipo2
		@cantidad = 7.20,
		@cuota = 1.45,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'V',
		@Goles = 3

EXECUTE RealizarApuestaTipo3
		@cantidad = 4.13,
		@cuota = 2.5,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = '2'


SET @IDPartidoAleatorio = NEWID()
INSERT INTO Partidos VALUES 
(@IDPartidoAleatorio, 'Liga', 1, 4, CURRENT_TIMESTAMP, DATEADD(minute, 105, CURRENT_TIMESTAMP), 1, 400000, 130000, 120000)

INSERT INTO EquiposPartidos VALUES 
('Real Betis Balompie', @IDPartidoAleatorio, 'L'),
('Real Madrid', @IDPartidoAleatorio, 'V')

--Apuestas ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 23,
		@cuota = 2.05,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 1,
		@GolesVisitante = 4

EXECUTE RealizarApuestaTipo2
		@cantidad = 5,
		@cuota = 2.15,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'L',
		@Goles = 1

EXECUTE RealizarApuestaTipo3
		@cantidad = 34.15,
		@cuota = 1.70,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = '2'

--Apuestas no ganadoras
EXECUTE RealizarApuestaTipo1
		@cantidad = 13,
		@cuota = 1.83,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@GolesLocal = 0,
		@GolesVisitante = 2

EXECUTE RealizarApuestaTipo2
		@cantidad = 7.20,
		@cuota = 1.45,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@LocalOVisitante = 'L',
		@Goles = 5

EXECUTE RealizarApuestaTipo3
		@cantidad = 4.13,
		@cuota = 2.5,
		@IDPartido = @IDPartidoAleatorio,
		@CorreoUsuario = 'test@test.test',
		@1X2 = 'X'