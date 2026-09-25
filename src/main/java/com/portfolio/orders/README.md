# Orders API

Microservicio REST para la creación y gestión de órdenes de compra. Diseñado para interactuar en un ecosistema de arquitectura distribuida.

Este servicio se comunica de manera sincrónica con el `inventory-api` para validar reglas de negocio (disponibilidad de stock) antes de persistir la orden en la base de datos local.

## Stack tecnológico
- Java 17
- Spring Boot 3.x
- Spring Web (REST)
- Spring Data JPA
- RestTemplate (Para peticiones HTTP entre microservicios)
- H2 Database (In-memory)

## Arquitectura y Lógica
- **Validación de Stock:** Al recibir un POST, el `OrderService` realiza una petición GET al Inventario. Si el stock es insuficiente o el producto no existe, se lanza una excepción controlada retornando un `400 Bad Request`.
- **Database per Service:** Este microservicio posee su propia base de datos (H2), garantizando el desacoplamiento de los datos según las mejores prácticas.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/orders` | Crea una orden si existe stock suficiente en el inventario. |

*Ejemplo de Payload:*
```json
{
    "productId": 1,
    "quantity": 2
}