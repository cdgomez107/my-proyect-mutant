# validate-mutant Project


Este proyecto permite validar en base a una secuencia de ADN (arreglo) si un humano es mutante o no y guarda en una base de datos la información para permitir traer unas estadísticas; siendo esto por medio de una API rest.



Para este desarrollo se utilizaron las siguientes tecnologías:

- Java

- Quarkus (https://quarkus.io/)

- Apache Camel (https://camel.apache.org/)

- MySQL (https://www.mysql.com/)

- Docker (https://www.docker.com/)



## Cómo consumir la API



La API esta hosteada en AWS y se conecta a una RDS (Base de datos - MySQL) para el manejo de información. Esta cuenta con dos operaciones las cuales son las siguientes:

- POST - /mutant/

  **Ejemplo de petición:** ``{


"dna":[


"ATGCGA",

"CAGTGC",

"TTCTCT",

"AGACTG",

"CCGTTA",

"TCACTG"

]

}``

**_NOTA:_**  Devuelve un 200 en caso de que el ADN sea un mutante y un 403 en caso de que no lo sea.



- GET - /stats/

  **Ejemplo de respuesta:** ``{


"ADN": {


"count_mutant_dna": 9,

"count_human_dna": 11,

"ratio": 0.45

}

}``

**_NOTE:_**  Devuelve un JSON con la información de las cantidades de mutantes y humanos.



**_La URL de la API es la siguiente:_** https://validate-mutant-meli.sojlam54kdv2c.us-east-1.cs.amazonlightsail.com



## Cómo ejecutar la API localmente



Si se quiere ejecutar la API localmente solo se necesita tener instalado docker para descargar y correr el contenedor de la aplicación.



Para descargar la imagen del docker hub con el siguiente comando:

`$ docker pull cdgomez107/validate-mutant-quarkus:latest`



Para correr el contenedor se debe ejecutar el siguiente comando:

`$ docker run -i --rm -p 8080:8080 cdgomez107/validate-mutant-quarkus`



Ya después de esto se puede consumir la API desde el dominio local al puerto 8080 (http://localhost:8080).