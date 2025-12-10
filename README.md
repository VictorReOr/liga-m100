# Liga M100 - Sistema de Gestión Deportiva
 
Solución Full Stack para la gestión de ligas de patinaje.
*   **Backend**: Java Spring Boot 21 (API REST, JWT, Postgres).
*   **Frontend**: React + TypeScript + Vite.
 
---
 
## 🚀 Guía de Despliegue Online (Paso a Paso)
 
Esta guía utiliza **Railway** (para Backend y Base de Datos) y **Vercel** (para Frontend) por ser la combinación más sencilla y gratuita/barata.
 
### Parte 1: Backend + Base de Datos (en Railway)
 
1.  **Crear cuenta en Railway**: Entra a [railway.app](https://railway.app) y logueate con GitHub.
2.  **Nuevo Proyecto**: Click en `+ New Project` > `Provision PostgreSQL`.
    *   Esto creará una base de datos. Click en ella > pestaña `Variables`. Copia la `DATABASE_URL`.
3.  **Desplegar Backend**:
    *   En el mismo proyecto, click `+ New` > `GitHub Repo`.
    *   Selecciona este repositorio (`liga-m100`).
    *   **IMPORTANTE**: Railway detectará el `Dockerfile` en `backend/Dockerfile`. Si te pregunta el directorio raíz, indica `backend`.
4.  **Configurar Variables de Entorno (Backend)**:
    *   Ve a la pestaña `Settings` o `Variables` del servicio backend recién creado.
    *   Añade las siguientes variables:
        *   `DB_URL`: Pega la URL de la base de datos de Postgres (empieza por `postgresql://...`).
        *   `DB_USER`: `postgres` (o lo que diga Railway en las vars de la DB).
        *   `DB_PASSWORD`: (La contraseña de la DB).
        *   `JWT_SECRET`: Genera una cadena random larga (ej: `mysecretkey1234567890securelongkey`).
        *   `CORS_ALLOWED_ORIGINS`: `*` (Para empezar) o la URL que te dará Vercel después.
        *   `PORT`: `8080`
5.  **Verificar**: Espera a que termine el despliegue. Railway te dará una URL pública (ej: `https://liga-m100-backend.up.railway.app`). Guardala.
 
### Parte 2: Frontend (en Vercel)
 
1.  **Crear cuenta en Vercel**: Entra a [vercel.com](https://vercel.com) y logueate con GitHub.
2.  **Nuevo Proyecto**: Click `Add New` > `Project`.
3.  **Importar Repo**: Selecciona el repositorio `liga-m100`.
4.  **Configurar Build**:
    *   **Framework Preset**: Vite.
    *   **Root Directory**: Edita y selecciona `frontend`.
5.  **Variables de Entorno**:
    *   Añade una variable llamada `VITE_API_URL`.
    *   Valor: La URL de tu backend en Railway + `/api/v1` (Ejemplo: `https://liga-m100-backend.up.railway.app/api/v1`).
6.  **Deploy**: Click en `Deploy`.
7.  **Finalizar**: Vercel te dará la URL de tu frontend (ej: `https://liga-m100-frontend.vercel.app`).
 
### Parte 3: Conexión Final
 
1.  Vuelve a **Railway** (Backend).
2.  Edita la variable `CORS_ALLOWED_ORIGINS` y pon la URL de tu frontend en Vercel (sin barra al final) para mayor seguridad, o déjala en `*` si prefieres simplicidad.
3.  Reinicia el servicio de backend (Redeploy) para aplicar el cambio.
 
---
 
## 💻 Despliegue Local (Desarrollo)
 
**Requisitos**: Java 21, Node 18, PostgreSQL, Maven.
 
1.  **Base de Datos**:
    ```bash
    # Crear DB local
    psql -U postgres -c "CREATE DATABASE ligam100;"
    ```
2.  **Backend**:
    ```bash
    cd backend
    mvn spring-boot:run
    ```
    *Arranca en http://localhost:8080*
 
3.  **Frontend**:
    ```bash
    cd frontend
    npm install
    npm run dev
    ```
    *Arranca en http://localhost:5173*
 
## 🔑 Credenciales por Defecto (Seed Data)
 
El sistema crea automáticamente estos usuarios al iniciar (contraseña para todos: `admin123`):
 
*   **Superadmin**: `admin`
*   **Juez**: `juez1`
*   **CPA**: `cpa_malaga`
 
---
 
## Estructura del Proyecto
*   `/backend`: API Spring Boot. Contiene `Dockerfile` para despliegue.
*   `/frontend`: SPA React. Utiliza `VITE_API_URL` para conectar al backend.
