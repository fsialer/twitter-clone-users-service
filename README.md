# MICROSERVICIO DE USUARIOS
> Este microservicio se encarga de gestionar los usuarios tanto sus datos personales y podra interactuar siguiendo a otros usuarios registrados.

## Variables de entorno
```
DB_HOST=mongodb://localhost:27017/posts_db?authSource=admin
```
```
DB_USERNAME=admin
```
```
DB_PASSWORD=*****
```
```
AZURE_SERVICE_BUS_CONNECTION_STRING=*****
```
```
AZURE_SERVICE_BUS_QUEUE_NAME=*****
```
>Nota: Se necesitará crear el servicio de [AZURE SERVICE BUS](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-messaging-overview)  para obtener los valores de las variables de entorno [AZURE_SERVICE_BUS_CONNECTION_STRING, AZURE_SERVICE_BUS_QUEUE_NAME].
## Tabla de recursos
| NOMBRE                    | RUTA                    | PETICION | PARAMETROS                                          | CUERPO                                                                           | 
|---------------------------|-------------------------|----------|-----------------------------------------------------|----------------------------------------------------------------------------------|
| Actuator                  | /actuator       | GET      | NINGUNO     | NINGUNO                                                                      |
| Listar usuarios           | /v1/users               | GET      | NINGUNO                                             | NINGUNO                                                                          |
| Obtener usuario por id    | /v1/users/{id}          | GET      | NINGUNO                                             | NINGUNO                                                                          |
| Crear usuario             | /v1/users               | POST     | NINGUNO                                             | {<br/>"names":"Jhon"<br/>"lastNames":"Doe"<br/>"email":"jhondoe@example.com"<br/>"userId":"cde8c071a420424abf2"<br/>} |
| Actualizar usuario        | /v1/users/{id}          | PUT      | NINGUNO                                             |{<br/>"names":"Jhon"<br/>"lastNames":"Doe"<br/>"email":"jhondoe@example.com"<br/>}|
| Eliminar usuario          | /v1/users/{id}          | DELETE   | NINGUNO                                             | NINGUNO                                                                          |
| Verificar usuario         | /v1/users/{id}/verify   | GET      | NINGUNO                                             | NINGUNO                                                                          |
| Listar usuarios por ids   | /v1/users/find-by-ids   | GET      | ?ids=6804498d871f48237c0f5e40,6804498d871f48237c0f5e47 | {<br/>"postId":"6804498d871f48237c0f5e40",<br/> "typeTarget":"LIKE"<br/>}        |
| Seguir usuario            | /v1/users/follow        | POST     |                                                     | {<br/>"followedId":"cde8c071a420424abf28b189ae2cd6982",<br/>}        |
| Dejar de seguir a usuario | /v1/users/unfollow/{id} | DELETE   |                                                     |  |

## Stack
* SPRING BOOT
* SPRING WEBFLUX
* SPRING DATA
* MONGODB
* LOMBOK
* MAPSTRUCT
* MOCKITO
* JUNIT
* AZURE SERVICE BUS

## Referencias
* [Azure Services Bus](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-messaging-overview)