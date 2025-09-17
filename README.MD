# Template Spring Boot 3.3.2 con JWT

![JWT](https://img.shields.io/badge/JWT-Authentication-blue?logo=jsonwebtokens&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java%20JDK-21-007396?logo=openjdk&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-2.5.0-85EA2D?logo=swagger&logoColor=white)

## :arrow_down: Instalación y ejecución

1. Descarga de la plantilla:
    ```
    Ve al boton de <> Code y selecciona Descargar ZIP.
    ```

2. Navega al directorio del proyecto:
   ```bash
   cd template-spring-boot
   ```

3. Crear el archivo `.env` en la raiz del proyecto

   ```Diff
    template-spring-boot/
       ├─ .mvn/
       ├─ src/
       ├─ pom.xml
      +├─ .env        # variables de entorno (raíz del proyecto)
       └─ README.md
   ```

4. Correr el comando para compilar y ejecutar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

5. La API estará disponible en `http://localhost:${APP_PORT}`.


> [!WARNING]
> Sino funciona el comando es por falta de configuracion del java home, debe arreglar eso
> o correrlo en IDE JetBrains.

## :page_with_curl: Variables de entorno

Al momento de generar el archivo `.env` en la raiz del proyecto, debe contener las siguientes variables de entorno:

| Variable              | Valor (ejemplo)        | Descripción                                                |
|-----------------------|------------------------|------------------------------------------------------------|
| APP_NAME              | <tu_app_name>          | Nombre de la aplicación                                    |
| APP_PORT              | <tu_app_port>          | Puerto en el que corre la aplicación                       |
| HOST_CORS             | <tu_host_cors>         | Origen permitido para CORS                                 |
| DATABASE_NAME         | <tu_database_name>     | Nombre de la base de datos                                 |
| DATABASE_USER         | <tu_database_user>     | Usuario de la base de datos                                |
| DATABASE_PASSWORD     | <tu_database_password> | Contraseña de la base de datos                             |
| DATABASE_PORT         | <tu_database_port>     | Puerto de conexión a la base de datos                      |
| DATABASE_HOST         | <tu_database_host>     | Host de la base de datos                                   |
| JWT_SECRET            | <tu_jwt_secret>        | Clave secreta para JWT                                     |
| JWT_EXPIRES           | <tu_jwt_expires>       | Tiempo de expiración del JWT (ms)                          |
| SEC_PASSWORD_PEPPER   | <tu_password_pepper>   | Pepper adicional para hashing de contraseñas               |
| SWAGGER_ENABLED   | true o false           | Para habilitar las funciones de swagger, por defecto false |

> [!WARNING]
> En el template se usa la libreria para manejar base de datos SQL Server, si se desea usar otra base de datos
> se debe cambiar la dependencia en el archivo `pom.xml` y la configuración en el archivo `application.properties`.

## :closed_lock_with_key: Seguridad

La configuración de seguridad se encuentra en la clase `SecurityConfig.java`. Aquí se definen las reglas de acceso a los endpoints, la configuración de CORS
y la integración con JWT para la autenticación.

### Usuario de prueba
- **username:** test
- **password:** test
- **email:** test@ine.gob.gt
- **hash_password:** {argon2id}$argon2id$v=19$m=16384,t=3,p=1$quQz+izhePy4QeyeEelrjA$GLNHNQIrKjG2e3/HOCy3BzbKG4HuW0Eo9lrc/eQTFR4
- **llave_hash:** recursoshumanos

> Eso es una configuración por defecto en caso de querer realizar pruebas, en la columna de la tabla de user
> donde almacenen la contraseña, poner el hash_password y en la columna de email, colocar el proporcionado para realizar las pruebas.

En caso de cambiar la configuracion debe ir al `SecurityConfig.java` y cambiar las rutas protegidas.

```diff
src
└── main
    └── java
        └── com.ine.api.permisos._011
            ├── common
            ├── components
            └── config
                ├── EnvConfig.java
                ├── PepperingConfig.java
+               └── SecurityConfig.java
            ├── controllers
            ├── models
            ├── repositories
            ├── services
            ├── ApiPermisos011Application.java
            └── ServletInitializer.java
    └── resources
        ├── static
        ├── templates
        └── application.properties

```

## :hammer_and_wrench: Swagger

Este proyecto expone su documentación con **Swagger (springdoc-openapi)**.  
La activación se controla con la variable de entorno:

Para ejemplo de como se configura el controlador para los endpoints de swagger, puede ir a la clase `UserController.java`

```bash
SWAGGER_ENABLED=true   # Activa Swagger
SWAGGER_ENABLED=false  # Desactiva Swagger
```

### Para su configuración debe ir a la clase `OpenApiConfig.java`

```diff
 src/
 └─ main/
    └─ java/
       └─ com/
          └─ ine/
             └─ development/
                ├─ common/
                ├─ components/
                └─ config/
+                  ├── OpenApiConfig.java        # <- aquí
                   ├── EnvConfig.java
                   ├── OpenApiSecurityConfig.java
                   ├── PepperingConfig.java
                   └── SecurityConfig.java
                ├─ controllers/
                ├─ models/
                ├─ repositories/
                ├─ services/
                ├─ DevelopmentApplication.java
                └─ ServletInitializer.java
 resources/
 └─ application.properties
 test/
```

Por cada controlador nuevo que cree, debe ir a la clase `OpenApiConfig.java` y debe agregar la ruta puesta en el controlador.

> [!WARNING]
> Por defecto está desactivado siempre.
