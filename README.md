# Vuelta Ciclista II: Ejercicio 1

**Implementa un programa Java que permita registrar automáticamente una nueva etapa**
**completa, con participación incluida, respetando la integridad del esquema.**

El programa pide por el teclado los datos de etapa, para luego insertarla en tabla PARTICIPACION.
Luego con connection.setAutoCommit(false) deshabilitamos autocommit y con PreparedStatement preparamos una consulta de insercion de etapa en la tabla.

Despues, sacamos el numero de ciclistas con otra consulta. Esta sirve para calcular las posiciones aleatorias para todos ciclistas.
Y con estos posiciones aleatorias ya se inserta en tabla, calculando tambien los puntos, dependiendo de la posicion de ciclista.

En caso si falla algo, en catch se hace un rollback para revertir cambios.
