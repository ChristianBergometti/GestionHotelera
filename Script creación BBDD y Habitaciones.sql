/* Con los dos comandos siguientes, crear la base de datos y añadirla en la primera línea del
application.properties del proyecto.
Debiera quedar:
spring.datasource.url:jdbc:mysql://localhost:3306/eggperience?allowPublicKeyRetrieval=true&useSSL=false&useTimezone=true&serverTimezone=GMT&characterEncoding=UTF-8 
*/
CREATE DATABASE eggperience;
use eggperience;

/* Solo usar las siguientes órdenes cuando, tras haber levantado el proyecto, comprobemos que 
se creó la tabla 'habitacion' en la base de datos.
También, comprobar con un "SELECT * FROM habitacion" en qué órden aparecen las columnas
ya que, conforme a ese órden creo que habrá que reordenar los datos que ingresamos con el "INSERT INTO" */

INSERT INTO habitacion VALUES (1, true, 2, 'A1', 4000);
INSERT INTO habitacion VALUES (2, true, 2, 'A2', 4000);
INSERT INTO habitacion VALUES (3, true, 4, 'B1', 7000);
INSERT INTO habitacion VALUES (4, true, 4, 'B2', 7000);
INSERT INTO habitacion VALUES (5, true, 6, 'C1', 10000);
INSERT INTO habitacion VALUES (6, true, 6, 'C2', 10000);

/* Comprobamos si las habitaciones fueron persistidas */
SELECT * FROM habitacion;

/* Y ualá :D */
