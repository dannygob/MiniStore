# MiniStore

📱 Proyecto: Aplicación para Tienda de Abarrotes
Plataforma: Android Studio Lenguaje: Kotlin UI Framework: Jetpack Compose Base de Datos Local:
SQLite Base de Datos Remota: Firebase (Firestore + Storage) API externa: Open Food Facts (gratuita)
UI dinámica: LazyColumn / LazyRow

🧩 1. Funcionalidades Principales
📦 Inventario
Escaneo de productos por código de barras o ingreso manual.
Registro de entradas (compras) y salidas (ventas).
Control de stock con alertas por:
Cantidad mínima
Vencimiento
Clasificación por categorías.
Sistema de ubicación dentro del negocio (ej. $pasillo, $estante, $nivel) para optimizar la búsqueda
de productos.
🧾 Facturación y Gestión Contable
Emisión de recibos (en pantalla o PDF).
Registro de gastos fijos/operativos.
Consulta histórica de compras y ventas.
👤 Gestión de Personas
Clientes:
Nombre, teléfono, dirección, correo electrónico
Historial de compras y pedidos
Proveedores:
Nombre, teléfono, dirección, correo electrónico
Productos suministrados, historial de compras
🛒 Pedidos
Crear órdenes personalizadas para clientes.
Estados: Pendiente, Entregado, Cancelado.
Asociación al historial del cliente.
Notificaciones automáticas para clientes cuando sus pedidos están listos o en camino.

📊 Reportes y Análisis
Gráficas de ventas por:
Producto
Cliente
Categoría
Tiempo (semanal, mensual, trimestral, anual)
Productos más vendidos
Sugerencias de reposición según ventas y tendencias

🧱 2. Arquitectura de la Aplicación
💾 Bases de Datos
📍 SQLite (Offline)
Inventario
Clientes
Proveedores
Ventas recientes
Pedidos locales
☁️ Firebase Firestore / Storage
Historial de ventas completo
Datos sincronizados entre dispositivos
Recibos en PDF
Autenticación (multiusuario, opcional)
🔍 API Pública
Open Food Facts https://world.openfoodfacts.org/api/v0/product/[barcode].json

📱 3. Vistas Iniciales y Navegación
🧭 Menú o Bottom Navigation:
Inicio / Dashboard
Inventario
Ventas
Compras
Pedidos
Reportes
Clientes
Proveedores
Configuración
🔁 Listas con LazyColumn / LazyRow
Para mostrar:
Productos
Clientes
Proveedores
Pedidos

📚 4. Modelo de Datos Relacional (Tablas)
🗃️ Productos
ID, Nombre, Código de barras, Precio compra, Precio venta
Categoría, Stock, ProveedorID
🧾 Ventas y DetalleVenta
Ventas: ID, Fecha, ClienteID, Total
Detalle: ID, VentaID, ProductoID, Cantidad, Precio unitario
📥 Compras y DetalleCompra
Compras: ID, Fecha, ProveedorID, Total
Detalle: ID, CompraID, ProductoID, Cantidad, Precio unitario
👤 Clientes
ID, Nombre, Teléfono, Dirección, Email
Preferencias de pago (efectivo, tarjeta, transferencia )
🚚 Proveedores
ID, Nombre, Teléfono, Dirección, Email
🧾 Pedidos
ID, ClienteID, Fecha, Estado, Total
💸 Servicios y Gastos
ID, Tipo, Monto, Fecha, Descripción

🧠 5. Inteligencia y Estadísticas
Ranking de productos más vendidos.
Recomendaciones de reposición:
Basadas en volumen, frecuencia, y temporada
Comparativas por períodos:
Línea de tiempo, barras
Semana vs semana, mes vs mes, año vs año

📂 Estructura de Carpetas y Módulos — Proyecto de Tienda de Abarrotes (con Hilt)
kotlin
CopiarEditar
📦 app
├── 📁 data // Capa de acceso a datos
│ ├── 📁 local // SQLite
│ │ ├── dao/
│ │ ├── database/
│ │ └── entities/
│ ├── 📁 remote // Firebase + APIs
│ │ ├── firebase/
│ │ └── api/
│ └── 📁 repository // Implementaciones de repositorio
│
├── 📁 domain // Capa de negocio
│ ├── models/ // Modelos limpios (sin anotaciones)
│ └── usecases/ // Lógica de negocio
│
├── 📁 presentation // Interfaz de usuario (Jetpack Compose)
│ ├── 📁 navigation/ // Grafo de navegación
│ ├── 📁 screens/ // Una carpeta por módulo visual
│ │ ├── dashboard/
│ │ ├── inventory/
│ │ ├── sales/
│ │ ├── purchases/
│ │ ├── orders/
│ │ ├── reports/
│ │ ├── clients/
│ │ ├── providers/
│ │ └── settings/
│ ├── 📁 components/ // Componentes reutilizables (cards, dialogs)
│ └── 📁 theme/ // Colores, tipografías, estilos Compose
│
├── 📁 di // Inyección de dependencias con Hilt
│ ├── AppModule.kt // Room, Repositories, API, Firebase
│ ├── FirebaseModule.kt // Proveedores de Firebase Services
│ ├── ViewModelModule.kt // (opcional) si registras los ViewModels manualmente
│
├── 📁 utils // Funciones auxiliares y constantes
│ ├── constants/ // Rutas, nombres de colecciones, códigos
│ ├── extensions/ // Extensiones para String, Date, etc.
│ └── helpers/ // Validaciones, formatos, etc.
│
└── MainActivity.kt

📝 Notas importantes sobre Hilt en esta estructura
📌 AppModule.kt será el núcleo: ahí proveerás cosas como:
Instancia de Room Database
DAOs
Repositorios
Retrofit (si usas alguna API además de Firebase)
Firebase Auth, Firestore, Storage
📌 FirebaseModule.kt separa las dependencias específicas de Firebase para tenerlo más organizado.
📌 ViewModelModule.kt es útil si decides usar @Binds o @Provides para ViewModels en lugar de usar
hiltViewModel() directamente (opcional).

✅ Secuencia de trabajo sugerida con Hilt en mente
Crear domain/models con las clases limpias.
Implementar data/local (entities + DAOs + DB).
Configurar data/remote/firebase y api.
Crear repository/ y definir interfaces.
Crear di/ y configurar Hilt (@Module, @InstallIn).
Crear usecases/ con lógica de negocio.
Comenzar la UI (presentation/screens/, navigation/, etc.)
Usar @HiltViewModel y hiltViewModel() para inyectar dependencias en pantallas.

