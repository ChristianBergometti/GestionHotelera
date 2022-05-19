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

INSERT INTO habitacion VALUES ('A1', 2, 4000);
INSERT INTO habitacion VALUES ('A2', 2, 4000);
INSERT INTO habitacion VALUES ('B1', 4, 7000);
INSERT INTO habitacion VALUES ('B2', 4, 7000);
INSERT INTO habitacion VALUES ('C1', 6, 10000);
INSERT INTO habitacion VALUES ('C2', 6, 10000);