# ⛪ Church Manager — Gerenciador de Paróquias

Sistema web para gestão paroquial: **inventário**, **eventos**, **horários de missas**, **pastorais e suas atividades**, **pessoas** e mais.

| Camada         | Tecnologia                                                                 |
| -------------- | -------------------------------------------------------------------------- |
| Frontend       | [Next.js 16](https://nextjs.org/) (App Router) + TypeScript + Tailwind CSS v4 |
| Backend        | Java 21 + [Spring Boot 4](https://spring.io/projects/spring-boot) (Web MVC, Data JPA, Validation) |
| Banco de dados | PostgreSQL 17 (via Docker)                                                  |
| Migrações      | Flyway                                                                     |
| Documentação   | SpringDoc OpenAPI (Swagger UI)                                             |

> **Por que Spring Boot?** É o framework Java mais maduro e popular para APIs REST, com integração nativa a PostgreSQL (JPA/Hibernate), migrações (Flyway), validação, documentação OpenAPI e segurança (Spring Security) — tudo o que este projeto vai precisar.

---

## 📁 Estrutura do projeto

```
church-manager/
├── docker-compose.yml          # PostgreSQL (+ pgAdmin opcional)
├── .env.example                # Variáveis do docker-compose
├── README.md
│
├── backend/                    # API REST - Spring Boot
│   ├── mvnw / mvnw.cmd         # Maven Wrapper (não precisa instalar Maven)
│   ├── pom.xml
│   ├── .env.example            # Variáveis aceitas pelo backend
│   └── src/main/
│       ├── java/br/com/churchmanager/
│       │   ├── ChurchManagerApiApplication.java
│       │   ├── config/         # CORS, OpenAPI...
│       │   ├── common/         # BaseEntity, exceções, endpoints utilitários
│       │   └── modules/        # Um pacote por domínio (package-by-feature)
│       │       ├── paroquia/   # Dados da paróquia e comunidades
│       │       ├── inventario/ # Itens, categorias, movimentações
│       │       ├── evento/     # Eventos e reservas de espaço
│       │       ├── missa/      # Horários, intenções, escalas
│       │       ├── pastoral/   # Pastorais, membros, atividades
│       │       ├── pessoa/     # Paroquianos e voluntários
│       │       └── usuario/    # Usuários e autenticação
│       └── resources/
│           ├── application.yml       # Configuração principal
│           ├── application-dev.yml   # Perfil de desenvolvimento
│           └── db/migration/         # Scripts Flyway (V1__init.sql, ...)
│
└── frontend/                   # Web app - Next.js
    ├── .env.example            # NEXT_PUBLIC_API_URL
    └── src/
        ├── app/                # Rotas (App Router)
        │   ├── page.tsx        # Painel inicial
        │   ├── inventario/ eventos/ missas/ pastorais/ pessoas/
        │   ├── layout.tsx
        │   └── globals.css     # Design tokens (Tailwind v4 @theme)
        ├── components/
        │   ├── layout/         # Sidebar, etc.
        │   └── ui/             # Componentes reutilizáveis
        ├── config/modules.ts   # Registro dos módulos (menu + painel)
        ├── lib/api.ts          # Cliente HTTP para a API Java
        ├── hooks/              # React hooks customizados
        └── types/              # Tipos TypeScript compartilhados
```

Cada módulo do backend segue a organização:

```
modules/<modulo>/
├── entity/       # Entidades JPA (estendem BaseEntity: id UUID + created_at/updated_at)
├── repository/   # Interfaces Spring Data JPA
├── dto/          # Records de request/response
├── service/      # Regras de negócio
└── controller/   # Endpoints REST
```

---

## ✅ Pré-requisitos

Instale as ferramentas abaixo (versões mínimas):

| Ferramenta                                                          | Versão | Verificar com        |
| ------------------------------------------------------------------- | ------ | -------------------- |
| [Node.js](https://nodejs.org/) (inclui npm)                         | 20.9+  | `node -v`            |
| [Java JDK](https://adoptium.net/) (Temurin recomendado)             | 21     | `java -version`      |
| [Docker Desktop](https://www.docker.com/products/docker-desktop/)   | 24+    | `docker --version`   |
| [Git](https://git-scm.com/)                                         | —      | `git --version`      |

> Não é necessário instalar **Maven** (o projeto usa o Maven Wrapper `mvnw`) nem **PostgreSQL** localmente (ele roda no Docker).

---

## 🚀 Passo a passo para rodar o projeto

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio> church-manager
cd church-manager
```

### 2. Subir o banco de dados (PostgreSQL)

Abra o **Docker Desktop** e aguarde ele ficar pronto. Depois, na raiz do projeto:

```bash
# (opcional) copie as variáveis para personalizar usuário/senha/porta
cp .env.example .env          # Windows PowerShell: Copy-Item .env.example .env

docker compose up -d postgres
```

Verifique se o container está saudável (`healthy`):

```bash
docker compose ps
```

Dados de conexão padrão:

| Campo    | Valor            |
| -------- | ---------------- |
| Host     | `localhost`      |
| Porta    | `5432`           |
| Banco    | `church_manager` |
| Usuário  | `church`         |
| Senha    | `church`         |

> 💡 **pgAdmin (opcional):** para uma interface gráfica do banco, rode `docker compose --profile tools up -d` e acesse <http://localhost:5050> (login `admin@churchmanager.local` / `admin`). Ao registrar o servidor no pgAdmin use o host **`postgres`** (nome do serviço na rede do Docker).

### 3. Rodar o backend (Spring Boot)

Em um novo terminal:

```bash
cd backend

# Linux / macOS
./mvnw spring-boot:run

# Windows (PowerShell ou CMD)
.\mvnw.cmd spring-boot:run
```

Na primeira execução o Maven baixa as dependências (pode levar alguns minutos). Ao iniciar, o **Flyway** aplica automaticamente as migrações em `src/main/resources/db/migration`.

Quando aparecer `Started ChurchManagerApiApplication`, teste:

| Recurso        | URL                                           |
| -------------- | --------------------------------------------- |
| Ping           | <http://localhost:8080/api/ping>              |
| Health check   | <http://localhost:8080/api/actuator/health>   |
| Swagger UI     | <http://localhost:8080/api/swagger-ui.html>   |
| OpenAPI (JSON) | <http://localhost:8080/api/v3/api-docs>       |

#### Variáveis de ambiente do backend (opcionais)

Os valores padrão já funcionam com o `docker-compose`. Para sobrescrever, defina antes de rodar:

| Variável                 | Padrão                                              |
| ------------------------ | --------------------------------------------------- |
| `SPRING_PROFILES_ACTIVE` | `dev`                                               |
| `SERVER_PORT`            | `8080`                                              |
| `DB_URL`                 | `jdbc:postgresql://localhost:5432/church_manager`   |
| `DB_USERNAME`            | `church`                                            |
| `DB_PASSWORD`            | `church`                                            |
| `CORS_ALLOWED_ORIGINS`   | `http://localhost:3000`                             |

Exemplo (PowerShell): `$env:DB_PASSWORD="outrasenha"; .\mvnw.cmd spring-boot:run`

### 4. Rodar o frontend (Next.js)

Em outro terminal:

```bash
cd frontend

npm install

# crie o arquivo de ambiente local (se ainda não existir)
cp .env.example .env.local    # Windows PowerShell: Copy-Item .env.example .env.local

npm run dev
```

Acesse <http://localhost:3000>. No painel inicial, o indicador **"API conectada"** (verde) confirma que o frontend está se comunicando com o backend.

| Variável              | Padrão                       | Descrição                 |
| --------------------- | ---------------------------- | ------------------------- |
| `NEXT_PUBLIC_API_URL` | `http://localhost:8080/api`  | URL base da API Java      |

---

## 🧰 Comandos úteis

### Banco de dados

```bash
docker compose up -d postgres     # sobe o banco
docker compose stop               # para os containers
docker compose logs -f postgres   # logs do banco
docker compose down               # remove containers (mantém os dados)
docker compose down -v            # ⚠️ remove containers E apaga todos os dados
docker exec -it church-manager-db psql -U church -d church_manager   # console SQL
```

### Backend (dentro de `backend/`)

```bash
./mvnw spring-boot:run            # roda em modo dev (com DevTools / hot reload)
./mvnw clean package              # gera o .jar em target/ (roda os testes; precisa do banco no ar)
./mvnw clean package -DskipTests  # gera o .jar sem rodar testes
./mvnw test                       # executa os testes
java -jar target/backend-0.0.1-SNAPSHOT.jar   # executa o .jar gerado
```

> No Windows, troque `./mvnw` por `.\mvnw.cmd`.

### Frontend (dentro de `frontend/`)

```bash
npm run dev      # servidor de desenvolvimento (http://localhost:3000)
npm run build    # build de produção
npm run start    # serve o build de produção
npm run lint     # ESLint
```

---

## 🗄️ Migrações de banco (Flyway)

- O schema é controlado **somente** pelo Flyway (`spring.jpa.hibernate.ddl-auto=validate`).
- Crie novos arquivos em `backend/src/main/resources/db/migration` seguindo o padrão:
  `V<numero>__<descricao>.sql` — ex.: `V2__create_inventario.sql`.
- **Nunca edite** uma migração que já foi aplicada; crie uma nova.
- As migrações rodam automaticamente ao iniciar o backend.

---

## 🛠️ Solução de problemas

| Problema | Solução |
| -------- | ------- |
| `failed to connect to the docker API` | O Docker Desktop não está aberto. Inicie-o e aguarde ficar "running". |
| `Connection refused` ao iniciar o backend | O PostgreSQL não está no ar. Rode `docker compose up -d postgres` e confira `docker compose ps`. |
| Porta `5432` já em uso | Você tem outro PostgreSQL rodando. Altere `POSTGRES_PORT` no `.env` (ex.: `5433`) e use `DB_URL=jdbc:postgresql://localhost:5433/church_manager`. |
| Porta `8080` ou `3000` em uso | Use `SERVER_PORT=8081` no backend e/ou `npm run dev -- -p 3001` no frontend (lembre de ajustar `NEXT_PUBLIC_API_URL` e `CORS_ALLOWED_ORIGINS`). |
| "API offline" no painel | Confira se o backend está rodando e se `NEXT_PUBLIC_API_URL` está correto. Reinicie o `npm run dev` após alterar o `.env.local`. |
| `JAVA_HOME not found` ao rodar `mvnw` | Instale o JDK 21 e configure a variável de ambiente `JAVA_HOME`. |

---

## 🗺️ Roadmap

- [x] Estrutura do monorepo (frontend, backend, banco)
- [x] Configuração base: CORS, OpenAPI, Flyway, tratamento de erros
- [x] Layout do frontend com navegação por módulos
- [ ] Modelagem do banco e CRUD de **Inventário**
- [ ] **Eventos** e calendário
- [ ] **Horários de missas** e intenções
- [ ] **Pastorais** e atividades
- [ ] Cadastro de **Pessoas**
- [ ] Autenticação e perfis de acesso (Spring Security + JWT)
- [ ] Dockerfile do backend/frontend e deploy
