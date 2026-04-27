#  Olité — Tienda de Productos Naturales y Ecológicos

<p align="center">
  <strong>Aplicación Android de comercio electrónico para productos naturales y ecológicos</strong><br>
  <em>Proyecto Final — CFGS Desarrollo de Aplicaciones Multiplataforma</em>
</p>

---

##  Descripción

Olité es una aplicación móvil Android completa de e-commerce desarrollada para un negocio real de productos naturales, ecológicos y artesanales de higiene y cuidado personal. El proyecto incluye un backend API REST, una base de datos relacional y una app Android nativa, todo ello desplegado en producción en AWS.

**Autor:** Daniel Palacios Moreno 
**Contacto:** daniel.palaciosmm@gmail.com
 
---

##  Stack Tecnológico

| Componente | Tecnología |
|---|---|
| **Frontend** | Kotlin · Android Views (XML) · MVVM · ViewModel · LiveData · Retrofit · Glide · Navigation Component · Material Design |
| **Backend** | Java · Spring Boot 4.0.3 · Spring Security · JWT (jjwt 0.12.6) · JPA/Hibernate · Lombok · Maven |
| **Base de datos** | MySQL 8.0 · 6 tablas relacionadas · Diseño E-R |
| **Cloud** | AWS EC2 (servidor) · AWS RDS (base de datos) |
| **IDEs** | Android Studio · IntelliJ IDEA |
| **Testing** | JUnit 5 · Mockito · Postman |

---

##  Estructura del Repositorio

```
olite/
├── OliteBackend/              → Proyecto backend (Java + Spring Boot)
│   ├── src/                   → Código fuente
│   ├── uploads/               → Imágenes de los productos
│   ├── pom.xml                → Dependencias Maven
│   └── application.properties → Configuración (BD, JWT, puerto)
├── OliteAndroid/              → Proyecto frontend (Kotlin + Android)
│   ├── app/src/               → Código fuente
│   └── build.gradle           → Dependencias Gradle
├── script_olite_db.sql        → Script SQL (creación BD + datos de prueba)
└── README.md                  → Este archivo
```

---

##  Arquitectura

### Visión general

```
App Android (Kotlin + MVVM)
        ↓ HTTP / JSON + JWT
API REST (Spring Boot)
        ↓ JDBC / JPA
MySQL (olite_db - 6 tablas)
```

### Backend — Arquitectura en capas

```
Controller → Service → Repository → Entity
     ↑           ↑          ↑           ↑
  Recibe HTTP   Lógica    CRUD auto   Mapeo BD
  y delega     negocio    Spring Data  con JPA
```

- **Entity:** Clases @Entity mapeadas a tablas MySQL con JPA
- **Repository:** Interfaces JpaRepository con CRUD automático
- **DTO:** Objetos de transferencia sin exponer entidades
- **Service:** Lógica de negocio con @Service e inyección Lombok
- **Controller:** @RestController que recibe HTTP y delega a Service
- **Security:** JwtUtil + JwtFilter + SecurityConfig (BCrypt, roles)

### Frontend — MVVM

```
Fragment (View) → ViewModel → Repository → Retrofit → API REST
       ↑                                                  ↓
       └──────── LiveData (actualización automática) ──────┘
```

- **Model:** DTOs (data class) + ApiService (Retrofit) + Repositories
- **ViewModel:** MutableLiveData + viewModelScope + Corrutinas
- **View:** Fragments + XML Layouts + Adapters (RecyclerView)
- **Single Activity Architecture:** 1 MainActivity + 11 Fragments

---

##  Funcionalidades

### Usuario visitante (sin registro)
- Navegar por el Home con banner, productos destacados y categorías
- Explorar el catálogo completo con buscador en tiempo real
- Ver el detalle de cualquier producto

### Usuario registrado
- Registro y login con autenticación JWT
- Añadir productos al carrito con selector de cantidad
- Realizar pedidos y consultar historial con estados (Pendiente/Enviado/Finalizado)
- Editar perfil personal
- Cerrar sesión

### Administrador (ROLE_ADMIN)
- CRUD completo de productos (crear, editar, eliminar)
- Gestión de usuarios (listar, eliminar con protección auto-borrado)
- Gestión de pedidos (cambiar estado de cualquier pedido)

---

##  Base de Datos

6 tablas relacionadas:

| Tabla | Descripción |
|---|---|
| `usuario` | Usuarios del sistema con roles (USER/ADMIN) y contraseñas BCrypt |
| `producto` | Catálogo de productos con nombre, descripción, precio, stock e imagen |
| `carrito` | Carrito único por usuario (relación 1:1) |
| `carrito_producto` | Productos dentro del carrito con cantidad y precio unitario |
| `pedido` | Pedidos realizados con estado, fecha, total y método de pago |
| `pedido_detalle` | Detalle de cada pedido con precio histórico de venta |

### Usuarios de prueba

| Email | Contraseña | Rol |
|---|---|---|
| `dani@ejemplo.com` | `123456` | ROLE_USER |
| `admin@olite.com` | `admin1234` | ROLE_ADMIN |

---

##  Seguridad

- **Autenticación:** JWT (JSON Web Token) con validez de 24 horas
- **Cifrado:** BCrypt para contraseñas almacenadas
- **Autorización:** Roles ROLE_USER y ROLE_ADMIN con control por endpoint
- **Filtro:** JwtFilter intercepta cada petición y valida el token
- **Stateless:** Sin sesiones en el servidor, cada petición incluye el token

### Control de acceso

| Endpoint | Público | USER | ADMIN |
|---|---|---|---|
| `/api/auth/**` | ✅ | ✅ | ✅ |
| `/api/productos` (GET) | ✅ | ✅ | ✅ |
| `/api/carrito/**` | ❌ | ✅ | ✅ |
| `/api/pedidos/**` | ❌ | ✅ | ✅ |
| `/api/productos` (POST/PUT/DELETE) | ❌ | ❌ | ✅ |
| `/api/usuarios` (GET all, DELETE) | ❌ | ❌ | ✅ |

---

##  Instalación y Ejecución

### Requisitos previos

- Java JDK 21 o superior
- MySQL 8.0
- Android Studio (Hedgehog o superior)
- IntelliJ IDEA
- Git

### 1. Clonar el repositorio

```bash
git clone https://github.com/dawerto/olite.git
cd olite
```

### 2. Configurar la base de datos

```sql
-- Abrir MySQL Workbench → File → Open SQL Script → script_olite_db.sql
-- Ejecutar el script completo
-- Verificar:
USE olite_db;
SELECT * FROM producto;  -- Deben aparecer 6 productos
```

### 3. Configurar el backend

Editar `OliteBackend/src/main/resources/application.properties`:

```properties
# Configurar con tus credenciales de MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/olite_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

Abrir la carpeta `OliteBackend/` en IntelliJ IDEA → Ejecutar `OlitebackendApplication.java`

Verificar: `http://localhost:8080/api/productos`

### 4. Ejecutar la app Android

Abrir la carpeta `OliteAndroid/` en Android Studio → Seleccionar emulador → Run

### Configuración de URLs de imágenes según entorno

El script SQL incluye las URLs de imágenes apuntando al emulador (`10.0.2.2`). Según tu entorno, ejecuta el UPDATE correspondiente:

```sql
-- Para dispositivo físico en red local:
UPDATE producto SET imagen = REPLACE(imagen, 'http://10.0.2.2:8080', 'http://TU_IP_LOCAL:8080') WHERE id_producto > 0;

-- Para AWS (producción):
UPDATE producto SET imagen = REPLACE(imagen, 'http://10.0.2.2:8080', 'http://TU_IP_EC2:8080') WHERE id_producto > 0;

-- Para revertir al emulador(DEFAULT):
UPDATE producto SET imagen = REPLACE(imagen, 'http://TU_IP:8080', 'http://10.0.2.2:8080') WHERE id_producto > 0;
```

También actualiza la `BASE_URL` en `OliteAndroid/.../network/RetrofitClient.kt` para que coincida.

### Orden de arranque

1. **MySQL** → verificar que el servicio está activo
2. **Backend** → ejecutar desde IntelliJ IDEA
3. **Android** → ejecutar desde Android Studio

---

##  API REST — Endpoints (19)

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/auth/register` | Registro de usuario | Público |
| POST | `/api/auth/login` | Login (devuelve JWT) | Público |
| GET | `/api/productos` | Listar todos los productos | Público |
| GET | `/api/productos/{id}` | Obtener producto por ID | Público |
| POST | `/api/productos` | Crear producto | ADMIN |
| PUT | `/api/productos/{id}` | Actualizar producto | ADMIN |
| DELETE | `/api/productos/{id}` | Eliminar producto | ADMIN |
| GET | `/api/carrito/{idUsuario}` | Obtener carrito del usuario | USER |
| POST | `/api/carrito/{idUsuario}/add/{idProducto}` | Añadir al carrito | USER |
| DELETE | `/api/carrito/item/{idCarritoProducto}` | Eliminar producto del carrito | USER |
| DELETE | `/api/carrito/{idUsuario}/vaciar` | Vaciar carrito | USER |
| POST | `/api/pedidos/{idUsuario}` | Realizar pedido | USER |
| GET | `/api/pedidos/usuario/{idUsuario}` | Pedidos del usuario | USER |
| GET | `/api/pedidos/todos` | Todos los pedidos | ADMIN |
| PUT | `/api/pedidos/estado/{idPedido}` | Cambiar estado pedido | ADMIN |
| GET | `/api/usuarios` | Listar todos los usuarios | ADMIN |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID | USER |
| PUT | `/api/usuarios/{id}` | Actualizar usuario | USER |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario | ADMIN |

---

##  Vías Futuras

- Despliegue con Docker para portabilidad
- Pasarela de pago real (Stripe / PayPal)
- Notificaciones push con Firebase Cloud Messaging
- Sistema de favoritos y valoraciones de productos
- Soporte multi-idioma y modo oscuro
- Versión iOS con Kotlin Multiplatform
- OAuth2 (login con Google / Facebook)

---

##  Licencia

Este proyecto ha sido desarrollado como Trabajo Final del CFGS en Desarrollo de Aplicaciones Multiplataforma (2S 2025/2026).

---

<p align="center">
  Desarrollado por <strong>Daniel Palacios Moreno</strong>
</p>
