# Ejercicio Tecnico

Aplicacion Springboot para creacion,login y obtencion de informacion de usuario.

## Table of Contents

- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)

## Project Overview

Aplicacion hecha con Springboot 2.5.14 para gestion de usuarios 

### Endpoints: 
> La aplicacion cuenta con distintos endpoints que representan un metodo de la clase UserController.
- /api/sign-up : Crea un usuario con un mail no existente en la base de datos h2
ejemplo de request:
```
    (async function createUser() {
        let user = { "name": "camilo", "email": "usuario@mail.com","password":"Hola12345","phones":[{"number":78799123,"citycode":9,"contrycode":"+56"}] };
        try {
            const response = await fetch("http://localhost:8080/api/sign-up", {
                method: "POST",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            });
            if (response.ok) {
                const createdUser = await response.json();
                console.log("User created successfully. Response:", createdUser);
            } else {
                console.log("Failed to create user. Response:", response);
            }
        } catch (err) {
            console.log("An error occurred:", err);
        }
    })(); 
```
> _Puedes correr este fragmento de codigo en javascript desde tu navegador, a traves de la consola para interactuar con la api_

- /api/login : inicia sesion con un usuario utilizando si mail y constraseña. obtiene un token JWT temporal por 2 minutos 
ejemplo de request:

```
(async function loginUser() {
    let user = { "name": "camilo", "email": "usuario@mail.com","password":"Hola12345","phones":[{"number":78799123,"citycode":9,"contrycode":"+56"}] };

    try {
        const response = await fetch("http://localhost:8080/api/login", {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {
            const createdUser = await response.json();
            console.log("User created successfully:", createdUser);
        } else {
            console.log("Failed to create user. Status:", response);
        }
    } catch (err) {
        console.log("An error occurred:", err);
    }
})();
```
> _Puedes correr este fragmento de codigo en javascript desde tu navegador, a traves de la consola para interactuar con la api_

## Prerequisitos
Java 11
## Iniciando el proyecto

# Clonar Repositorio
git clone [https://github.com/yourusername/your-repository.git](https://github.com/CamiloPoblet/ejercicioTecnico.git)

# Iniciar Proyecto
Ejecutar RunProject.bat , este batch realiza una build con gradle y ejecuta el jar resultante, mientras el batch este abierto la aplicacion estara corriendo y se podra interactuar con ella
