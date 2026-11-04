# Bella Imagen - Sistema de Gestión para Estética

## Descripción

Bella Imagen es un sistema de gestión para un establecimiento de estética y belleza. Permite administrar clientes, especialistas, servicios, citas y permutas de turnos, además de gestionar el acceso del personal mediante usuarios y roles.

## Tecnologías utilizadas

- Java JDK 25
- JavaFX 25
- MySQL 8.0
- Maven
- JDBC (MySQL Connector/J)
- BCrypt para el manejo seguro de contraseñas
- Dotenv para la configuración de variables de entorno
- Git y GitHub

## Requisitos previos

Antes de ejecutar el proyecto se necesita instalar:

1. JDK 25.
2. Apache Maven 3.10 o superior.
3. MySQL 8.0 o una base de datos MySQL compatible accesible desde la nube.
4. Visual Studio Code.
5. Git.

## Descargar el proyecto

Clonar el repositorio:

```bash
git clone https://github.com/FernandoH29/EsteticaBelleza.git
```

Entrar en la carpeta:

```bash
cd EsteticaBelleza
```

Abrir el proyecto en Visual Studio Code:

```bash
code .
```

## Configuración de la base de datos

El proyecto utiliza MySQL.

La estructura de la base de datos se encuentra en:

`database/bella_imagen.sql`

Para crear una base de datos nueva, primero ejecutar en MySQL:

```sql
CREATE DATABASE bella_imagen
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE bella_imagen;
```

Después ejecutar el archivo `database/bella_imagen.sql` desde MySQL Workbench.

Este archivo crea las siete tablas del sistema y sus relaciones.

## Configuración de conexión

El proyecto utiliza un archivo `.env` para almacenar los datos de conexión.

Copiar el archivo de ejemplo:

```bash
copy .env.example .env
```

Completar el archivo `.env` con los datos de conexión de MySQL:

```properties
DB_HOST=HOST_DE_MYSQL
DB_PORT=PUERTO
DB_NAME=NOMBRE_BASE_DATOS
DB_USER=USUARIO
DB_PASSWORD=CONTRASEÑA
```

**Importante:** El archivo `.env` contiene información privada y no debe subirse a GitHub.

Para utilizar la base de datos compartida de Railway, el administrador del proyecto debe proporcionar las credenciales de conexión de forma privada.

## Ejecutar el proyecto

Desde la carpeta principal ejecutar:

```bash
mvn clean javafx:run
```

Maven descargará las dependencias necesarias y ejecutará la aplicación.

## Estructura del proyecto

```text
EsteticaBelleza/
│
├── database/
│   └── bella_imagen.sql
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── bellaimagen/
│                   ├── conexion/
│                   ├── dao/
│                   ├── modelo/
│                   ├── vista/
│                   ├── Main.java
│                   ├── LoginView.java
│                   └── PanelAdministrativo.java
│
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

## Seguridad

- Las contraseñas de los usuarios se almacenan mediante BCrypt.
- Las credenciales de MySQL se configuran mediante variables de entorno.
- El archivo `.env` está excluido del repositorio mediante `.gitignore`.

## Funcionalidades principales

- Inicio de sesión para el personal.
- Administración de clientes.
- Administración de especialistas.
- Administración de servicios.
- Gestión de citas.
- Agendamiento público de citas.
- Gestión de permutas de turnos.
- Control de usuarios y roles.

## Repositorio

GitHub: https://github.com/FernandoH29/EsteticaBelleza