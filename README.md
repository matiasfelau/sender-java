# sender-java

## Descripción
Este es un proyecto académico para la UADE, un paquete que permite la comunicación entre los distintos módulos a través del Core.

## Tabla de Contenidos
- [Changelog](#changelog)
- [Instalación](#instalación)
- [Uso](#uso)
- [Ejemplo](#ejemplo)

## Changelog

### [1.2.3] - 2024-10-28
- Modificación del JSON para incluir el header "target" que permita identificar el tipo de objetos que se transmiten en un mensaje del tipo Array.

### [1.2.2] - 2024-10-28
- Implementación de métodos para la conversión de cada elemento de un array entrante a una clase objetivo.
- Mensajes de aviso del flujo del programa.

### [1.2.1] - 2024-10-22
- Implementación de métodos para el manejo de Arrays en los mensajes.

### [1.2] - 2024-10-21
- Implementación de headers:
    1. token - para permitir el manejo de la autenticación con JWT.
    2. type - para permitir un mejor manejo de los mensajes al definir el tipo de dato que fue enviado.

## Instalación
Para instalar la libreria en Windows o en Linux seguí estos pasos:

### Windows
1. Ingresá a este enlace https://maven.apache.org/download.cgi, descargá el archivo apache-maven-3.9.9-bin.zip y descomprimilo en la carpeta que prefieras.
2. En Windows Search buscá "Editar las variables de entorno del sistema" y seguí estos pasos:
![Paso 1](images/1.png)
![Paso 2](images/2.png)
![Paso 3](images/3.png)

Nombre de la variable: MAVEN_HOME, Valor de la variable: ruta al directorio bin del archivo que descomprimiste en el paso anterior (ej: D:\Maven\apache-maven-3.9.9\bin).

![Paso 4](images/4.png)
![Paso 5](images/5.png)
![Paso 6](images/6.png)

Ruta al directorio bin del archivo que descomprimiste en el paso anterior (ej: D:\Maven\apache-maven-3.9.9\bin).

![Paso 7](images/7.png)

### Linux
1. Ejecutá estas instrucciones en bash:
```bash
sudo apt update
sudo apt upgrade
sudo apt install maven
mvn -version
```

### Finalizando en ambos casos (Maven)

- Agregá a tu archivo pom.xml las siguientes dependencias:
```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.22.0</version>
</dependency>

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>

<dependency>
    <groupId>io.github.matiasfelau</groupId>
    <artifactId>sender-java</artifactId>
    <version>1.2.1</version>
</dependency>
```

### Finalizando en ambos casos (Gradle)

Agrega las siguientes dependencias a tu archivo build.gradle:
```.gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.matiasfelau:sender-java:1.2.1'
}
```

## Uso
Usá el proyecto de la siguiente manera:
1. Iniciá dos conexiones con el servidor, una será para enviar mensajes y la otra para recibirlos. 
La conexión te va a pedir especificar un host, un puerto, un usuario y una contraseña. Todos los datos te van a ser dados por el Squad del Core.
2. Definí una función de callback, o sea lo que va a hacer tu aplicación cada vez que reciba un mensaje. Esto te va a permitir procesarlo.
Es importante aclarar que los mensajes viajan en formato de bytes[], así que es necesario usar el método convertDelivery.
3. Iniciá el servicio consumidor dándole una conexión e indicándole cuál es tu módulo, el cual estará restringido por el enum Modules.
Es importante aclarar que invocar a esta función bloqueará un hilo de procesamiento para estar permanentemente a la escucha.
4. Por último, podés usar el método publish para enviar mensajes a cualquier módulo válido. También se encuentra restringido por el enum Modules.
Necesitarás ingresar por parámetro una conexión (distinta a la del servicio consumidor), el mensaje en formato String, el nombre del módulo de origen, el de destino y el caso de uso que genera al mensaje.
Recomendamos cerrar la conexión usada después de enviar un mensaje, o un lote de ellos, y abrir una nueva cuando vuelva a ser necesaria.

> [!CAUTION]
> ACLARACIÓN
> 1. Tené en cuenta que la conexión es un objeto del tipo AutoCloseable, por lo que deberías manejar las excepciones e implementar una lógica de reconexión.
> 2. Los mensajes pueden ser clases convertidas a un String de formato JSON ó valores sueltos en forma de Plain String. Nuestra recomendación es usar clases, como la vista de ejemplo, para un mejor manejo de la información.
> 3. Asegurese de utilizar el Type correcto segun su mensaje a enviar. Evite errores para los consumidores.

# Ejemplo
```Java
package org.example;

import ar.edu.uade.*;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;

public class Main {
    public static void main(String[] args) throws Exception {
        Broker broker = new Broker(
                System.getenv("HOST"),
                Integer.parseInt(
                        System.getenv("PORT")
                ),
                System.getenv("USER"),
                System.getenv("PASSWORD")
        );

        Connection consumerConnection = broker.startConnection();

        //Redefino el callback para los mensajes recibidos.
        Consumer consumer = new Consumer(new CallbackInterface() {
            @Override
            public void callback(String consumerTag, Delivery delivery) {
                //Body es la clase que encapsula a los datos enviados desde el módulo de origen.
                Body body = Utilities.convertDelivery(delivery);

                //Los datos enviados desde el módulo de origen se encuentran en el atributo payload del body.
                String datos = body.getPayload();

                //Los datos enviados desde el módulo de origen pueden convertirse a cualquier clase del modelo, si corresponde.
                try {
                    Usuario usuario = Utilities.convertBody(body, Usuario.class);
                } catch (ConverterException e) {
                    
                }
            }
        });

        //Comienza a consumir utilizando un hilo secundario
        consumer.consume(consumerConnection, Modules.USUARIO);

        Connection publisherConnection = broker.startConnection();

        //Crea la instancia para poder publicar un mensaje
        Publisher publisher = new Publisher(Modules.USUARIO);

        Usuario usuario = new Usuario("Matias");

        //Convierto un objeto a un String de formato JSON.
        String mensaje = Utilities.convertClass(usuario);

        //El token es el JWT que entrega Gestion_Interna
        //Types presenta 3 variables, String, JSON o Array, utilizado para un mejor manejo del mensaje.
        publisher.publish(publisherConnection, mensaje, Modules.USUARIO, "Prueba", token, Types.JSON, target, status, user);

        broker.endConnection(publisherConnection);
    }
}
```

> [!IMPORTANT]
> En caso de querer trabajar con Arrays, puede seguir los siguientes elementos..
> (Para versiones mayores a la 1.2.1 se pueden usar los métodos convertArray y convertString de Utilities y aconsejamos su uso)

### Publicar varios mensajes    
```Java
...
// Inicializo el Publisher
Publisher publisher = new Publisher(Modules.USUARIO);

// Array de mensajes que voy a enviar
List<String> mensajes = new ArrayList<>();

// Cargo usuarios en el array como demostración
for (int i = 0; i < 10; i++) {
    Usuario usuario = new Usuario(String.valueOf(i));
    String mensaje = Utilities.convertClass(usuario);
    mensajes.add(mensaje);
}

// Convierto el array en un String
// Uso --!--##-->>DELIMITER<<--##--!-- como delimitador
String mensaje = String.join("--!--##-->>DELIMITER<<--##--!--", mensajes);

// Lo envío
publisher.publish(connection, mensaje, Modules.USUARIO, "Prueba");
...
```
### Callback refinado para varios mensajes a recibir...
```Java
...
Consumer consumer = new Consumer(new CallbackInterface() {
    @Override
    public void callback(String consumerTag, Delivery delivery) {
        try {
            // Decodifico el delivery
            Body body = Utilities.convertDelivery(delivery);

            // Consigo el String que contiene al array
            String mensaje = body.getPayload();

            // Convierto el String a un array real
            String[] mensajes = mensaje.split("--!--##-->>DELIMITER<<--##--!--");

            for (String s : mensajes) {
                System.out.println(s);
            }

        } catch (ConverterException e) {
            throw new RuntimeException(e);
        }
    }
});
...
```
> [!IMPORTANT]
> El metodo para la autencacion es:
```Java

public class Main {
    public static void main(String[] args) {
        Broker broker = new Broker(
                "3.141.117.124",
                5672,
                "admin",
                "59482*M97&!@3@%$2$r@");

        Connection connection;

        try {

            connection = broker.startConnection();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        Authenticator authenticator = new Authenticator(Modules.USUARIO);

        String response;
        
        String json = "ejemplo";

        try {

            response = authenticator.authenticate(connection, json);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        broker.endConnection(connection);

    }
}
```
