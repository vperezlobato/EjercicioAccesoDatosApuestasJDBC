GO
CREATE DATABASE CasaDeApuestas
GO
USE CasaDeApuestas
GO
--DROP DATABASE CasaDeApuestas

CREATE TABLE Partidos (
  ID UniqueIdentifier DEFAULT NEWID() NOT NULL,
  competicion varchar(30) NOT NULL,
  golesLocal TinyInt NULL,
  golesVisitante TinyInt NULL,
  fechaInicio SmallDateTime NULL,
  fechaFin SmallDateTime NULL,
  EstaAbierto Bit NOT NULL,
  limiteAlcanzadoTipo1 Money NOT NULL,
  limiteAlcanzadoTipo2 Money NOT NULL,
  limiteAlcanzadoTipo3 Money NOT NULL,


  CONSTRAINT PK_Partidos PRIMARY KEY (ID),
  CONSTRAINT CK_fechaInicio CHECK (fechaInicio < fechaFin)	-- El partido no puede empezar después de acabar.
)

GO

CREATE TABLE Usuarios (
  correoElectronico VarChar(45) NOT NULL,
  contraseña VarChar(20) NOT NULL,
  saldo Money NOT NULL,

  CONSTRAINT PK_Usuarios PRIMARY KEY (correoElectronico),
) 

GO

CREATE TABLE Apuestas (
  ID UniqueIdentifier DEFAULT NEWID() NOT NULL,
  cantidad SmallMoney NOT NULL,
  cuota Decimal(5,2) NOT NULL,
  IDPartido UniqueIdentifier NOT NULL,
  CorreoUsuario VarChar(45) NOT NULL,
  Tipo char(1) NOT NULL,

  CONSTRAINT PK_Apuestas PRIMARY KEY (ID),
  CONSTRAINT FK_IDPartido_Apuestas FOREIGN KEY(IDPartido) REFERENCES Partidos(ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_CorreoUsuario_Apuestas FOREIGN KEY(CorreoUsuario) REFERENCES Usuarios(correoElectronico) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT CK_Cuota_Apuestas CHECK (cuota > 1.00),
  CONSTRAINT CK_Tipo_Apuestas CHECK (Tipo LIKE '[123]')
)

GO

CREATE TABLE ApuestasTipo1 (
  IDApuesta UniqueIdentifier NOT NULL,
  GolesLocal TinyInt NOT NULL,
  GolesVisitante TinyInt NOT NULL,

  CONSTRAINT PK_ApuestasTipo1 PRIMARY KEY (IDApuesta),
  CONSTRAINT FK_IDApuesta_ApuestasTipo1 FOREIGN KEY (IDApuesta) REFERENCES Apuestas(ID) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT CK_GolesLocal_ApuestasTipo1 CHECK (GolesLocal >= 0),
  CONSTRAINT CK_GolesVisitante_ApuestasTipo1 CHECK (GolesVisitante >= 0)
)

GO

CREATE TABLE ApuestasTipo2 (
  IDApuesta UniqueIdentifier NOT NULL,
  LocalOVisitante Char(1) NOT NULL,
  Goles TinyInt NOT NULL,

  CONSTRAINT PK_ApuestasTipo2 PRIMARY KEY (IDApuesta),
  CONSTRAINT FK_IDApuesta_ApuestasTipo2 FOREIGN KEY (IDApuesta) REFERENCES Apuestas(ID) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT CK_LocalOVisitante_ApuestasTipo2 CHECK (LocalOVisitante LIKE '[LV]'),
  CONSTRAINT CK_Goles_ApuestasTipo2 CHECK (Goles >= 0)
)

GO

CREATE TABLE ApuestasTipo3 (
  IDApuesta UniqueIdentifier NOT NULL,
  [1X2] Char(1) NOT NULL,

  CONSTRAINT PK_ApuestasTipo3 PRIMARY KEY (IDApuesta),
  CONSTRAINT FK_IDApuesta_ApuestasTipo3 FOREIGN KEY (IDApuesta) REFERENCES Apuestas(ID) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT CK_1X2 CHECK ([1X2] LIKE '[1X2]')
)

GO

CREATE TABLE Equipos (
  nombre VarChar(50) NOT NULL,

  CONSTRAINT PK_Equipos PRIMARY KEY (nombre)
)

GO

CREATE TABLE Movimientos (
  ID UniqueIdentifier DEFAULT NEWID() NOT NULL,
  cantidad Money NOT NULL,
  tipo VarChar(8) NOT NULL, --Ingreso/Retirada
  correoUsuario VarChar(45) NOT NULL,

  CONSTRAINT PK_Movimientos PRIMARY KEY (ID),
  CONSTRAINT FK_correoUsuario_Movimientos FOREIGN KEY (correoUsuario) REFERENCES Usuarios(correoElectronico) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT CK_tipo_Movimientos CHECK (tipo LIKE '[IR]')
) 

GO

CREATE TABLE EquiposPartidos (
  Equipo VarChar(50) NOT NULL,
  Partido UniqueIdentifier NOT NULL,
  Tipo Char(1) NOT NULL,  --Local/Visitante

  CONSTRAINT PK_EquiposPartidos PRIMARY KEY (Equipo, Partido),
  CONSTRAINT FK_Equipo_EquiposPartidos FOREIGN KEY (Equipo) REFERENCES Equipos(nombre) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT FK_Partido_EquiposPartidos FOREIGN KEY (Partido) REFERENCES Partidos(ID) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT CK_Tipo_EquiposPartidos CHECK (Tipo LIKE '[LV]'),
  CONSTRAINT UQ_Tipo_Partido_EquiposPArtidos UNIQUE(Partido, Tipo)
)

-- TRIGGERS

--Una vez formalizada la apuesta no puede cambiarse ni borrarse.
GO
CREATE TRIGGER NoBorrarNiEditarApuestas ON Apuestas INSTEAD OF DELETE,UPDATE AS
BEGIN
	RAISERROR('No se puede borrar ni cambiar la apuesta una vez formalizada.', 16, 1)
END -- TRIGGER

--TEST
--BEGIN TRANSACTION

--DECLARE @IDPartido AS uniqueidentifier
--DECLARE @IDApuesta AS uniqueidentifier

--SET @IDPartido = NEWID()
--SET @IDApuesta = NEWID()


--INSERT INTO Partidos (ID, competicion, golesLocal, golesVisitante, fechaInicio, fechaFin, EstaAbierto, limiteAlcanzado) VALUES (@IDPartido, 'Liga', 0, 1, 'Sep 26 2019  7:32PM', CURRENT_TIMESTAMP, 1, 2500)

--INSERT INTO Apuestas (ID, cantidad, cuota, IDPartido, CorreoUsuario, tipo) VALUES (@IDApuesta, 23, 2.05, @IDPartido, 'test@test.test', '3')

--UPDATE Apuestas
--SET cuota = 5
--WHERE ID = @IDApuesta

--DELETE Apuestas
--WHERE ID = @IDApuesta

--ROLLBACK

GO

--Solo se puede apostar a partidos que estén abiertos para recibir apuestas. Normalmente se abre el partido unos días antes de su celebración y se cierra unos minutos antes de su finalización. 
CREATE TRIGGER PartidoDebeEstarAbierto ON Apuestas AFTER INSERT AS
BEGIN
	IF EXISTS(
		(SELECT P.EstaAbierto FROM inserted AS ins
		INNER JOIN Partidos AS P ON P.ID = ins.IDPartido
		WHERE P.EstaAbierto = 0))	-- Se traduciría a algo así como: si existe alguna apuesta de las insertadas cuyo partido tenga el campo EstaAbierto a 0, lanza el error y no insertes la apuesta.
	BEGIN
		RAISERROR('Solo se pueden realizar apuestas a partidos que estén abiertos a apuestas', 16, 1)
		ROLLBACK
	END --IF
END	--Trigger

--TEST
--BEGIN TRANSACTION

--DECLARE @IDPartido AS uniqueidentifier
--SET @IDPartido = NEWID()

--INSERT INTO Partidos (ID, competicion, golesLocal, golesVisitante, fechaInicio, fechaFin, EstaAbierto, limiteAlcanzado) VALUES (@IDPartido, 'Liga', 0, 1, 'Sep 26 2019  7:32PM', CURRENT_TIMESTAMP, 0, 2500)

--INSERT INTO Apuestas (cantidad, cuota, IDPartido, CorreoUsuario, tipo) VALUES ( 50, 1.83, @IDPartido, 'test@test.test', '2')

--ROLLBACK

--PROCEDURES

GO

--El saldo se disminuye cada vez que el usuario o usuaria formaliza una apuesta.

/* INTERFAZ
Comentario: Realiza una apuesta de tipo 1, insertándola en la base de datos y disminuyendo el saldo del usuario.
Prototipo: CREATE PROCEDURE RealizarApuestaTipo1
	@cantidad AS smallmoney,
	@cuota AS decimal(5,2),
	@IDPartido AS uniqueidentifier,
	@CorreoUsuario AS varchar(45),
	@GolesLocal AS tinyint,
	@GolesVisitante AS tinyint
Entrada: La cantidad de dinero apostado, la cuota que tendrá la apuesta, la ID del partido al que se apuesta, el correo del usuario que apuesta y los goles por los que se apuesta.
Precondiciones: No hay
Salida: No hay
Postcondiciones: La apuesta de tipo 1 queda formalizada, y el saldo del usuario se verá disminuido en la cantidad apostada.
*/
CREATE PROCEDURE RealizarApuestaTipo1
	@cantidad AS smallmoney,
	@cuota AS decimal(5,2),
	@IDPartido AS uniqueidentifier,
	@CorreoUsuario AS varchar(45),
	@GolesLocal AS tinyint,
	@GolesVisitante AS tinyint
AS
BEGIN
	DECLARE @IDApuesta AS uniqueidentifier
	SET @IDApuesta = NEWID()
	
	INSERT INTO Apuestas (ID, cantidad, cuota, IDPartido, CorreoUsuario, Tipo) VALUES (@IDApuesta, @cantidad, @cuota, @IDPartido, @CorreoUsuario, '1')

	INSERT INTO ApuestasTipo1 (IDApuesta, GolesLocal, GolesVisitante) VALUES (@IDApuesta, @GolesLocal, @GolesVisitante)

	UPDATE Usuarios
	SET saldo -= @cantidad
	WHERE correoElectronico = @CorreoUsuario

END --PROCEDURE

GO

--TEST
--BEGIN TRANSACTION

--EXECUTE RealizarApuestaTipo1 40, 2.03, 'CA103956-F8BE-460D-B802-0095987EE78D', 'test@test.test', 2, 1

--ROLLBACK

--GO
/* INTERFAZ
Comentario: Realiza una apuesta de tipo 2, insertándola en la base de datos y disminuyendo el saldo del usuario.
Prototipo: CREATE PROCEDURE RealizarApuestaTipo2
	@cantidad AS smallmoney,
	@cuota AS decimal(5,2),
	@IDPartido AS uniqueidentifier,
	@CorreoUsuario AS varchar(45),
	@LocalOVisitante AS char(1),
	@Goles AS tinyint
Entrada: La cantidad de dinero apostado, la cuota que tendrá la apuesta, la ID del partido al que se apuesta, el correo del usuario que apuesta, si apuesta al local o al visitante y el numero de goles de la apuesta.
Precondiciones: No hay
Salida: No hay
Postcondiciones: La apuesta de tipo 2 queda formalizada, y el saldo del usuario se verá disminuido en la cantidad apostada.
*/
CREATE PROCEDURE RealizarApuestaTipo2
	@cantidad AS smallmoney,
	@cuota AS decimal(5,2),
	@IDPartido AS uniqueidentifier,
	@CorreoUsuario AS varchar(45),
	@LocalOVisitante AS char(1),
	@Goles AS tinyint
AS
BEGIN

	DECLARE @IDApuesta AS uniqueidentifier
	SET @IDApuesta = NEWID()
	
	INSERT INTO Apuestas (ID, cantidad, cuota, IDPartido, CorreoUsuario, Tipo) VALUES (@IDApuesta, @cantidad, @cuota, @IDPartido, @CorreoUsuario, '2')

	INSERT INTO ApuestasTipo2 (IDApuesta, LocalOVisitante, Goles) VALUES (@IDApuesta, @LocalOVisitante, @Goles)

	UPDATE Usuarios
	SET saldo -= @cantidad
	WHERE correoElectronico = @CorreoUsuario

END --PROCEDURE

GO
--TEST
--BEGIN TRANSACTION

--SELECT * FROM ApuestasTipo2
--SELECT * FROM Usuarios

--EXECUTE RealizarApuestaTipo2 40, 2.03, 'CA103956-F8BE-460D-B802-0095987EE78D', 'test@test.test', 'L', 1

--SELECT * FROM ApuestasTipo2
--SELECT * FROM Usuarios

--ROLLBACK
--GO

/* INTERFAZ
Comentario: Realiza una apuesta de tipo 3, insertándola en la base de datos y disminuyendo el saldo del usuario.
Prototipo: CREATE PROCEDURE RealizarApuestaTipo3
	@cantidad AS smallmoney,
	@cuota AS decimal(5,2),
	@IDPartido AS uniqueidentifier,
	@CorreoUsuario AS varchar(45),
	@1X2 AS char(1)
Entrada: La cantidad de dinero apostado, la cuota que tendrá la apuesta, la ID del partido al que se apuesta, el correo del usuario que apuesta, y la apuesta (1 X 2)
Precondiciones: No hay
Salida: No hay
Postcondiciones: La apuesta de tipo 3 queda formalizada, y el saldo del usuario se verá disminuido en la cantidad apostada.
*/
CREATE PROCEDURE RealizarApuestaTipo3
	@cantidad AS smallmoney,
	@cuota AS decimal(5,2),
	@IDPartido AS uniqueidentifier,
	@CorreoUsuario AS varchar(45),
	@1X2 AS char(1)
AS
BEGIN

	DECLARE @IDApuesta AS uniqueidentifier
	SET @IDApuesta = NEWID()
	
	INSERT INTO Apuestas (ID, cantidad, cuota, IDPartido, CorreoUsuario, Tipo) VALUES (@IDApuesta, @cantidad, @cuota, @IDPartido, @CorreoUsuario, '3')

	INSERT INTO ApuestasTipo3 (IDApuesta, [1X2]) VALUES (@IDApuesta, @1X2)

	UPDATE Usuarios
	SET saldo -= @cantidad
	WHERE correoElectronico = @CorreoUsuario

END --PROCEDURE
GO

--TEST
--BEGIN TRANSACTION

--SELECT * FROM ApuestasTipo3
--SELECT * FROM Usuarios

--EXECUTE RealizarApuestaTipo3 40, 2.03, 'CA103956-F8BE-460D-B802-0095987EE78D', 'test@test.test', 'X'

--SELECT * FROM ApuestasTipo3
--SELECT * FROM Usuarios

--ROLLBACK
--GO

/*comprobar las apuestas ganadoras de un partido,y después le sumo al saldo la cantidad por cuota a los usuarios que hayan ganado las apuestas de ese partido*/

CREATE PROCEDURE AñadirGanancias
	@IDPARTIDO AS UNIQUEIDENTIFIER
AS
BEGIN

	DECLARE @GANADOR AS CHAR(1)

	DECLARE @APUESTASGANADORAS AS TABLE(
	ID UNIQUEIDENTIFIER NOT NULL,
	CANTIDAD SMALLMONEY NOT NULL,
	CUOTA DECIMAL(5,2),
	CORREOUSUARIO VARCHAR(45)
	)

	/* DECLARE @TIPO1 AS TABLE(
	ID UNIQUEIDENTIFIER NOT NULL,
	CANTIDAD SMALLMONEY NOT NULL,
	CUOTA DECIMAL(5,2),
	CORREOUSUARIO VARCHAR(45),
	GOLESLOCAL TINYINT,
	GOLESVISITANTE TINYINT)

	DECLARE @TIPO2 AS TABLE(
	ID UNIQUEIDENTIFIER NOT NULL,
	CANTIDAD SMALLMONEY NOT NULL,
	CUOTA DECIMAL(5,2),
	CORREOUSUARIO VARCHAR(45),
	LOCALOVISITANTE CHAR (1),
	GOLES TINYINT)
	
	DECLARE @TIPO3 AS TABLE(
	ID UNIQUEIDENTIFIER NOT NULL,
	CANTIDAD SMALLMONEY NOT NULL,
	CUOTA DECIMAL(5,2),
	CORREOUSUARIO VARCHAR(45),
	[1X2] CHAR (1)
	) */

	INSERT INTO @APUESTASGANADORAS
	SELECT A.ID,A.cantidad,A.cuota,A.CorreoUsuario
	FROM Apuestas AS A
	INNER JOIN Partidos AS P ON P.ID = A.IDPartido 
	INNER JOIN ApuestasTipo1 AS T1 ON T1.IDApuesta = A.ID
	WHERE T1.GolesLocal = P.golesLocal AND T1.GolesVisitante = P.golesVisitante AND P.ID = @IDPARTIDO

	INSERT INTO @APUESTASGANADORAS
	SELECT A.ID,A.cantidad,A.cuota,A.CorreoUsuario
	FROM Apuestas AS A
	INNER JOIN Partidos AS P ON P.ID = A.IDPartido 
	INNER JOIN ApuestasTipo2 AS T2 ON T2.IDApuesta = A.ID
	WHERE T2.LocalOVisitante = 'L' AND T2.Goles = P.golesLocal AND P.ID = @IDPARTIDO
	OR
	T2.LocalOVisitante ='V' AND T2.Goles = P.golesVisitante
	
	IF EXISTS ((SELECT * 
		FROM Partidos AS P
		WHERE P.ID = @IDPARTIDO AND P.golesLocal > P.golesVisitante)
		)
	BEGIN
		SET @GANADOR = '1'
	END --IF
	ELSE IF EXISTS ((SELECT * 
		FROM Partidos AS P
		WHERE P.ID = @IDPARTIDO AND P.golesLocal < P.golesVisitante)
		)
	BEGIN
		SET @GANADOR = '2'
	END --ELSE IF
	ELSE
	BEGIN
		SET @GANADOR = 'X'
	END --ELSE

	INSERT INTO @APUESTASGANADORAS
	SELECT A.ID,A.cantidad,A.cuota,A.CorreoUsuario
	FROM Apuestas AS A
	INNER JOIN Partidos AS P ON P.ID = A.IDPartido 
	INNER JOIN ApuestasTipo3 AS T3 ON T3.IDApuesta = A.ID
	WHERE T3.[1X2] = @GANADOR AND P.ID = @IDPARTIDO

	UPDATE Usuarios
	SET saldo += GT.GANANCIA
	FROM Usuarios AS U
	INNER JOIN (
		SELECT SUM(AG.CANTIDAD * AG.CUOTA) AS GANANCIA, AG.CORREOUSUARIO FROM @APUESTASGANADORAS AS AG	--Agrupa las ganancias de las apuestas ganadoras por usuario.
		GROUP BY AG.CORREOUSUARIO
	) AS GT ON GT.CORREOUSUARIO = U.correoElectronico

--Falta: Movimientos, meter los tres inserts a una sola tabla, los inserts deben ser al partido de entrada

END --Procedure


--los usuarios e usuarias pueden hacer ingresos en sus cuentas para aumentar el saldo y también pueden retirar dinero

GO
/* INTERFAZ
Comentario: Realiza un ingreso de dinero en la cuenta del usuario, registrando el movimiento y aumentando el saldo del usuario.
Prototipo: CREATE PROCEDURE IngresarDinero
	@cantidad AS smallmoney,
	@CorreoUsuario AS varchar(45)
Entrada: La cantidad de dinero a ingresar y el correo del usuario que realiza el ingreso.
Precondiciones: No hay
Salida: No hay
Postcondiciones: Se aumenta el saldo del usuario en la cantidad determinada y el movimiento queda registrado.
*/
CREATE PROCEDURE IngresarDinero
	@cantidad AS smallmoney,
	@CorreoUsuario AS varchar(45)
AS
BEGIN
	INSERT INTO Movimientos (cantidad, tipo, correoUsuario) VALUES (@cantidad, 'I', @CorreoUsuario)

	UPDATE Usuarios
	SET saldo += @cantidad
	WHERE correoElectronico = @CorreoUsuario
END

GO

----TEST
--BEGIN TRANSACTION

--SELECT * FROM Usuarios WHERE correoElectronico = 'test@test.test'

--EXECUTE IngresarDinero 35, 'test@test.test'

--SELECT * FROM Usuarios WHERE correoElectronico = 'test@test.test'

--ROLLBACK

/* INTERFAZ
Comentario: Realiza una retirada de dinero en la cuenta del usuario, registrando el movimiento y disminuyendo el saldo del usuario.
Prototipo: CREATE PROCEDURE RetirarDinero
	@cantidad AS smallmoney,
	@CorreoUsuario AS varchar(45)
Entrada: La cantidad de dinero a retirar y el correo del usuario que realiza la retirada.
Precondiciones: No hay
Salida: No hay
Postcondiciones: Se disminuye el saldo del usuario en la cantidad determinada y el movimiento queda registrado.
*/
CREATE PROCEDURE RetirarDinero
	@cantidad AS smallmoney,
	@CorreoUsuario AS varchar(45)
AS
BEGIN
	INSERT INTO Movimientos (cantidad, tipo, correoUsuario) VALUES (@cantidad, 'R', @CorreoUsuario)

	UPDATE Usuarios
	SET saldo -= @cantidad
	WHERE correoElectronico = @CorreoUsuario
END
 
GO

--TEST
--BEGIN TRANSACTION

--SELECT * FROM Usuarios WHERE correoElectronico = 'test@test.test'

--EXECUTE RetirarDinero 35, 'test@test.test'

--SELECT * FROM Usuarios WHERE correoElectronico = 'test@test.test'

--ROLLBACK

--GO

--Faltaría hacer el trigger para los otros tipos de apuesta y hacerlo con inner join a la tabla Partidos para ver el limite alcanzado

GO
CREATE TRIGGER LimiteApuestaTipo1 ON ApuestasTipo1 AFTER INSERT AS
BEGIN

	DECLARE @IDApuesta AS uniqueidentifier
	DECLARE @GolesLocal AS tinyint
	DECLARE @GolesVisitante AS tinyint

	DECLARE @IDPartido AS uniqueidentifier

	DECLARE insertado CURSOR FOR
	SELECT IDApuesta, GolesLocal, GolesVisitante FROM inserted

	OPEN insertado

	FETCH NEXT FROM insertado 
		INTO @IDApuesta, @GolesLocal ,@GolesVisitante

	WHILE @@FETCH_STATUS = 0
	BEGIN

		SET @IDPartido = (SELECT IDPartido FROM Apuestas WHERE ID = @IDApuesta) -- Se obtiene el partido al cual se ha realizado la apuesta

		IF ( (SELECT SUM(A.cantidad * A.cuota) FROM Apuestas AS A
		INNER JOIN ApuestasTipo1 AS AT1 ON AT1.IDApuesta = A.ID						-- Necesita la tabla de tipo1 para obtener los goles
		WHERE @IDPartido = A.IDPartido AND Tipo = '1'								-- Filtra para obtener solo las apuestas del mismo partido y tipo
		AND @GolesLocal = AT1.GolesLocal AND @GolesVisitante = AT1.GolesVisitante)	-- Filtra para obtener solo las apuestas con el mismo numero de goles
		> (SELECT limiteAlcanzadoTipo1 FROM Partidos AS P WHERE @IDPartido = P.ID) ) -- Si la suma de los beneficios totales de todas las apuestas de tipo 1 del partido superan el limite
		BEGIN
			RAISERROR('Limite alcanzado, las apuestas estan cerradas',16,1)
			ROLLBACK
		END--IF

		FETCH NEXT FROM insertado

	END --WHILE

	CLOSE insertado
	DEALLOCATE insertado

END --TRIGGER

GO
--TEST
--BEGIN TRAN

--DECLARE @IDPartido AS uniqueidentifier

--SET @IDPartido = NEWID()

--INSERT INTO Partidos (ID, competicion, golesLocal, golesVisitante, fechaInicio, fechaFin, EstaAbierto, limiteAlcanzadoTipo1, limiteAlcanzadoTipo2, limiteAlcanzadoTipo3)
--			VALUES (@IDPartido, 'Liga', 0, 2, CURRENT_TIMESTAMP, DATEADD(minute, 105,CURRENT_TIMESTAMP), 1, 100, 100, 100)

--INSERT INTO Usuarios (correoElectronico, contraseña, saldo) VALUES ('test@test.test', 'test', 300)

----EXECUTE RealizarApuestaTipo1 25, 3, @IDPartido, 'test@test.test', 0, 0	-- Aqui no debe saltar el rollback del trigger

----EXECUTE RealizarApuestaTipo1 25, 3, @IDPartido, 'test@test.test', 0, 0  -- Aqui debe saltar el rollback del trigger

--DECLARE @IDApuesta1 AS uniqueidentifier
--DECLARE @IDApuesta2 AS uniqueidentifier

--SET @IDApuesta1 = NEWID()
--SET @IDApuesta2 = NEWID()

--INSERT INTO Apuestas (ID, cantidad, cuota, IDPartido, CorreoUsuario, Tipo) VALUES
--		(@IDApuesta1, 25, 3, @IDPartido, 'test@test.test', 1),
--		(@IDApuesta2, 25, 3, @IDPartido, 'test@test.test', 1)

--INSERT INTO ApuestasTipo1 (IDApuesta, GolesLocal, GolesVisitante) VALUES
--		(@IDApuesta1, 0, 0),
--		(@IDApuesta2, 0, 0)

----SELECT * FROM Apuestas

--ROLLBACK

GO
CREATE TRIGGER LimiteApuestaTipo2 ON ApuestasTipo2 AFTER INSERT AS
BEGIN

	DECLARE @IDPartido AS uniqueidentifier
	DECLARE @IDApuesta AS uniqueidentifier
	DECLARE @Goles AS tinyint
	DECLARE @esLocaloVisitante AS char(1)
	
	DECLARE cursorApuestas2 CURSOR FOR
	SELECT IDApuesta, Goles, @esLocaloVisitante FROM inserted

	OPEN cursorApuestas2

	FETCH NEXT FROM cursorApuestas2
	INTO @IDApuesta, @Goles, @esLocaloVisitante

	WHILE @@FETCH_STATUS = 0
	BEGIN

	SET @IDPartido = (SELECT IDPartido FROM Apuestas WHERE ID = @IDApuesta)

		IF ( (SELECT SUM(A.cantidad * A.cuota) FROM Apuestas AS A
		INNER JOIN ApuestasTipo2 AS AT2 ON AT2.IDApuesta = A.ID
		WHERE @IDPartido = A.IDPartido AND Tipo = '2'
		AND @Goles = AT2.Goles AND @esLocaloVisitante = AT2.LocalOVisitante)
		> (SELECT limiteAlcanzadoTipo2 FROM Partidos AS P WHERE @IDPartido = P.ID) )
		BEGIN
			RAISERROR('Limite alcanzado, las apuestas estan cerradas',16,1)
			ROLLBACK
		END

		FETCH NEXT FROM cursorApuestas2

	END

	CLOSE cursorApuestas2
	DEALLOCATE cursorApuestas2

	END
--ROLLBACK

GO
CREATE TRIGGER LimiteApuestaTipo3 ON ApuestasTipo3 AFTER INSERT AS
BEGIN

	DECLARE @IDPartido AS uniqueidentifier
	DECLARE @IDApuesta AS uniqueidentifier
	DECLARE @quiniela AS char
	
	DECLARE cursorApuestas3 CURSOR FOR
	SELECT IDApuesta, 1X2 FROM inserted

	OPEN cursorApuestas3

	FETCH NEXT FROM cursorApuestas3
	INTO @IDApuesta, @quiniela

	WHILE @@FETCH_STATUS = 0
	BEGIN

	SET @IDPartido = (SELECT IDPartido FROM Apuestas WHERE ID = @IDApuesta)

		IF ( (SELECT SUM(A.cantidad * A.cuota) FROM Apuestas AS A
		INNER JOIN ApuestasTipo3 AS AT3 ON AT3.IDApuesta = A.ID
		WHERE @IDPartido = A.IDPartido AND Tipo = '3'
		AND @quiniela = AT3.[1X2])
		> (SELECT limiteAlcanzadoTipo3 FROM Partidos AS P WHERE @IDPartido = P.ID) )
		BEGIN
			RAISERROR('Limite alcanzado, las apuestas estan cerradas',16,1)
			ROLLBACK
		END

		FETCH NEXT FROM cursorApuestas3

	END

	CLOSE cursorApuestas3
	DEALLOCATE cursorApuestas3

END
--Trigger para comprobar que el usuario tiene saldo suficiente para realizar una apuesta
GO
CREATE TRIGGER comprobarSaldo ON Apuestas AFTER INSERT AS
BEGIN

	IF EXISTS (SELECT I.ID FROM inserted AS I
	INNER JOIN Usuarios AS U ON U.correoElectronico = I.CorreoUsuario
	WHERE I.cantidad > U.saldo )
	BEGIN

		RAISERROR('El usuario no tiene suficiente saldo para realizar la apuesta',16,1)
		ROLLBACK

	END --IF

END --TRIGGER

GO

--TEST
--BEGIN TRAN

--DECLARE @IDPartido AS uniqueidentifier

--SET @IDPartido = NEWID()

--INSERT INTO Usuarios VALUES ('test@test.test', 'test', 50)

--INSERT INTO Partidos VALUES (@IDPartido, 'Liga', 2, 0, CURRENT_TIMESTAMP, DATEADD(minute, 105, CURRENT_TIMESTAMP), 1, 5000, 5000, 5000)

--EXECUTE RealizarApuestaTipo1 
--	100, --Cantidad
--	1.87, --Cuota
--	@IDPartido, --Partido
--	'test@test.test', --Usuario
--	2, --GolesLocal
--	1 --GolesVisitante

--ROLLBACK

--GO

/*
TODO: La entidad movimientos debe guardar también los movimientos de saldo al realizar apuestas y al recibir los premios por las apuestas ganadas.
*/

--Trigger para que un partido no pueda tener más de dos equipos

--CREATE TRIGGER DosEquipos ON EquiposPartidos AFTER INSERT
--AS
--BEGIN

--	IF EXISTS (SELECT EP.Partido, COUNT(EP.Equipo) FROM EquiposPartidos AS EP
--	INNER JOIN inserted AS I ON I.Partido = EP.Partido
--	GROUP BY EP.Partido
--	HAVING COUNT(EP.Equipo) > 2)
--	BEGIN
	
--		RAISERROR('Un partido no puede tener mas de dos equipos',16,1)
--		ROLLBACK

--	END --IF

--END --Trigger

--GO



--TEST UQ_Tipo_Partido_EquiposPartidos
--BEGIN TRANSACTION

--INSERT INTO EquiposPartidos VALUES ('Real Betis Balompie', '62DBCD42-E094-4482-B5AD-73880CB7B1C5', 'L')
--INSERT INTO EquiposPartidos VALUES ('Valencia CF', '62DBCD42-E094-4482-B5AD-73880CB7B1C5', 'V')
--INSERT INTO EquiposPartidos VALUES ('Real Madrid', '62DBCD42-E094-4482-B5AD-73880CB7B1C5', 'V')

--SELECT * FROM EquiposPartidos

--ROLLBACK