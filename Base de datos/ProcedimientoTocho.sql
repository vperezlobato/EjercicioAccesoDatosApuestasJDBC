consSelect * from Partidos

GO
CREATE PROCEDURE consultarPartido 
	@IDPartido AS uniqueidentifier
AS
BEGIN
	Select A.Tipo,AT1.GolesLocal,AT1.GolesVisitante,AT2.Goles,AT2.LocalOVisitante,AT3.[1X2],SUM(A.cantidad) AS [Total Apostado] from Partidos AS P
	INNER JOIN Apuestas AS A ON A.IDPartido = P.ID
	LEFT JOIN ApuestasTipo1 AS AT1 ON AT1.IDApuesta = A.ID
	LEFT JOIN ApuestasTipo2 AS AT2 ON AT2.IDApuesta = A.ID
	LEFT JOIN ApuestasTipo3 AS AT3 ON AT3.IDApuesta = A.ID
	WHERE P.ID = @IDPartido
	group by A.Tipo,AT1.GolesLocal,AT1.GolesVisitante,AT2.Goles,AT2.LocalOVisitante,AT3.[1X2]	
END

GO
Execute consultarPartido '26AC576F-577A-4A0D-A354-625465D213A1'

Select M.correoUsuario,P.[Numero de apuestas],D.[Numero de apuestas ganadas] from Movimientos AS M
INNER JOIN
(Select COUNT(ID) as [Numero de apuestas],CorreoUsuario From Apuestas Where correoUsuario = 'test@test.test' group by CorreoUsuario) AS P
ON M.correoUsuario = P.CorreoUsuario
INNER JOIN 
(Select COUNT(A.ID) as [Numero de apuestas ganadas],A.CorreoUsuario From Apuestas AS A 
                    LEFT JOIN ApuestasTipo1 AS AT1 ON A.ID = AT1.IDApuesta 
                    INNER JOIN Partidos AS P ON P.ID = A.IDPartido 
                    LEFT JOIN ApuestasTipo2 AS AT2 ON AT2.IDApuesta = A.ID  
                    LEFT JOIN ApuestasTipo3 AS AT3 ON AT3.IDApuesta = A.ID 
                    WHERE ((AT1.GolesLocal = P.golesLocal AND AT1.GolesVisitante = P.golesVisitante) 
                    OR (AT2.LocalOVisitante = 'L' AND AT2.Goles = P.golesLocal )   
                    OR (AT3.[1X2] = '1' AND P.golesLocal > P.golesVisitante) OR (AT3.[1X2] = '2' AND P.golesLocal < P.golesVisitante) OR (AT3.[1X2] = 'X' AND P.golesLocal = P.golesVisitante)) 
                    AND A.CorreoUsuario = 'test@test.test'
					group by A.CorreoUsuario) AS D
ON D.CorreoUsuario = M.correoUsuario
group by M.correoUsuario,P.[Numero de apuestas],D.[Numero de apuestas ganadas]

select * from Apuestas