# API Proveedores

Usuario inicial de prueba:

```json
{
  "email": "admin@proveedores.local",
  "password": "admin123"
}
```

## Login

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "admin@proveedores.local",
  "password": "admin123"
}
```

## Proveedores

```http
GET /api/proveedores
Authorization: Bearer <token>
```

```http
POST /api/proveedores
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "razonSocial": "Servicios Norte S.A.",
  "cuit": "30-33333333-3",
  "email": "contacto@serviciosnorte.com",
  "telefono": "011-4000-3000",
  "direccion": "Av. San Martin 500"
}
```

## Productos

```http
POST /api/productos
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "nombre": "Notebook corporativa",
  "descripcion": "Equipo para puestos administrativos",
  "precio": 950000.00,
  "stock": 8,
  "proveedorId": 1,
  "categoriaId": 1
}
```

## Ordenes

```http
POST /api/ordenes
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "proveedorId": 1,
  "items": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```
