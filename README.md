# ⛪ Church Manager — Gerenciador de Paróquias

Sistema web para gestão paroquial: **inventário**, **eventos**, **horários de missas**, **pastorais e suas atividades**, **pessoas** e mais.

| Camada         | Tecnologia                                                                 |
| -------------- | -------------------------------------------------------------------------- |
| Frontend       | [Next.js 16](https://nextjs.org/) (App Router) + TypeScript + Tailwind CSS v4 |
| Backend        | Java 21 + [Spring Boot 4](https://spring.io/projects/spring-boot) (Web MVC, Data JPA, Validation) |
| Banco de dados | PostgreSQL hospedado no [Neon](https://neon.tech) (serverless, na nuvem)    |
| Migrações      | Flyway                                                                     |
| Documentação   | SpringDoc OpenAPI (Swagger UI)                                             |

> **Por que Spring Boot?** É o framework Java mais maduro e popular para APIs REST, com integração nativa a PostgreSQL (JPA/Hibernate), migrações (Flyway), validação, documentação OpenAPI e segurança (Spring Security) — tudo o que este projeto vai precisar.

---

## 📁 Estrutura do projeto

```
church-manager/
├── README.md
│
├── backend/                    # API REST - Spring Boot
│   ├── mvnw / mvnw.cmd         # Maven Wrapper (não precisa instalar Maven)
│   ├── pom.xml
│   ├── .env                    # ⚠️ credenciais do Neon (você cria; não versionado)
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
    ├── .env.local              # URL da API (você cria; não versionado)
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

> 🔒 **Nenhum arquivo `.env*` é versionado** (nem `.env.example`). Os modelos estão neste README, nos passos 2 e 4.

---

## ✅ Pré-requisitos

| Ferramenta                                              | Versão | Verificar com     |
| ------------------------------------------------------- | ------ | ----------------- |
| [Node.js](https://nodejs.org/) (inclui npm)             | 20.9+  | `node -v`         |
| [Java JDK](https://adoptium.net/) (Temurin recomendado) | 21     | `java -version`   |
| [Git](https://git-scm.com/)                             | —      | `git --version`   |
| Conta no [Neon](https://neon.tech) (plano Free serve)   | —      | —                 |

> Não é necessário instalar **Maven** (o projeto usa o Maven Wrapper `mvnw`) nem **PostgreSQL** localmente (o banco fica no Neon).

---

## 🚀 Passo a passo para rodar o projeto

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio> church-manager
cd church-manager
```

### 2. Criar o banco de dados no Neon

1. Acesse <https://console.neon.tech> e crie uma conta (pode entrar com GitHub ou Google).
2. Clique em **New Project** e preencha:
   - **Project name:** `church-manager`
   - **Postgres version:** a mais recente (17 ou superior)
   - **Region:** `AWS São Paulo (sa-east-1)`, ou a mais próxima de você — menor latência
   - **Database name:** `church_manager`

   Clique em **Create project**.
3. No painel do projeto, clique em **Connect** (canto superior direito). Na janela que abrir:
   - **Branch:** `production` (ou `main`) · **Database:** `church_manager` · **Role:** `neondb_owner`
   - Em **Connection string**, escolha o formato **Java** (ou JDBC) se disponível.
4. Copie **duas** conexões:
   - Com **Connection pooling ativado** → host contém `-pooler` → vai para `DB_URL`
   - Com **Connection pooling desativado** → host **sem** `-pooler` → vai para `DB_URL_UNPOOLED`
5. Anote também o **usuário** (ex.: `neondb_owner`) e a **senha** (clique em *Show password*).

> 💡 **Por que duas URLs?** A aplicação usa a conexão *pooled* (PgBouncer do Neon, aguenta mais conexões simultâneas). Já as migrações do Flyway precisam da conexão *direta*, pois o pooler não mantém estado de sessão.

#### Convertendo a URL do Neon para JDBC

Se o Neon mostrar a URL no formato `postgresql://`, converta assim:

```
# Formato do Neon:
postgresql://neondb_owner:SENHA@ep-cool-name-123456-pooler.sa-east-1.aws.neon.tech/church_manager?sslmode=require&channel_binding=require

# Formato JDBC (usuário e senha vão em variáveis separadas):
jdbc:postgresql://ep-cool-name-123456-pooler.sa-east-1.aws.neon.tech/church_manager?sslmode=require
```

Ou seja: troque `postgresql://usuario:senha@` por `jdbc:postgresql://`, e mantenha `?sslmode=require` (SSL é obrigatório no Neon).

### 3. Configurar e rodar o backend (Spring Boot)

Crie o arquivo **`backend/.env`** com o conteúdo abaixo, substituindo pelos seus dados do Neon:

```properties
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# Conexão POOLED (host contém "-pooler") - usada pela aplicação
DB_URL=jdbc:postgresql://ep-xxxxxx-pooler.sa-east-1.aws.neon.tech/church_manager?sslmode=require
# Conexão DIRETA (host SEM "-pooler") - usada pelo Flyway nas migrações
DB_URL_UNPOOLED=jdbc:postgresql://ep-xxxxxx.sa-east-1.aws.neon.tech/church_manager?sslmode=require

DB_USERNAME=neondb_owner
DB_PASSWORD=sua_senha_aqui

CORS_ALLOWED_ORIGINS=http://localhost:3000
```

> O Spring Boot carrega o `backend/.env` automaticamente (`spring.config.import` no `application.yml`). Variáveis de ambiente do sistema, se definidas, têm prioridade sobre o arquivo. **Rode o backend a partir da pasta `backend/`** para que o arquivo seja encontrado.

Depois, rode:

```bash
cd backend

# Linux / macOS
./mvnw spring-boot:run

# Windows (PowerShell ou CMD)
.\mvnw.cmd spring-boot:run
```

Na primeira execução o Maven baixa as dependências (pode levar alguns minutos). Ao iniciar, o **Flyway** aplica automaticamente as migrações de `src/main/resources/db/migration` no banco do Neon — você pode conferir a tabela `flyway_schema_history` em **Tables** no console do Neon.

Quando aparecer `Started ChurchManagerApiApplication`, teste:

| Recurso        | URL                                           |
| -------------- | --------------------------------------------- |
| Ping           | <http://localhost:8080/api/ping>              |
| Health check   | <http://localhost:8080/api/actuator/health>   |
| Swagger UI     | <http://localhost:8080/api/swagger-ui.html>   |
| OpenAPI (JSON) | <http://localhost:8080/api/v3/api-docs>       |

#### Referência das variáveis do backend

| Variável                 | Obrigatória | Padrão                  | Descrição                                   |
| ------------------------ | ----------- | ----------------------- | ------------------------------------------- |
| `DB_URL`                 | ✅          | —                       | JDBC da conexão **pooled** do Neon          |
| `DB_URL_UNPOOLED`        | recomendada | valor de `DB_URL`       | JDBC da conexão **direta** (Flyway)         |
| `DB_USERNAME`            | ✅          | —                       | Usuário (role) do Neon                      |
| `DB_PASSWORD`            | ✅          | —                       | Senha do Neon                               |
| `SPRING_PROFILES_ACTIVE` |             | `dev`                   | Perfil do Spring                            |
| `SERVER_PORT`            |             | `8080`                  | Porta da API                                |
| `CORS_ALLOWED_ORIGINS`   |             | `http://localhost:3000` | Origens liberadas (separadas por vírgula)   |

### 4. Configurar e rodar o frontend (Next.js)

Crie o arquivo **`frontend/.env.local`**:

```properties
# URL base da API Java (Spring Boot)
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

Depois, em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

Acesse <http://localhost:3000>. No painel inicial, o indicador **"API conectada"** (verde) confirma que o frontend está se comunicando com o backend.

---

## 🧰 Comandos úteis

### Backend (dentro de `backend/`)

```bash
./mvnw spring-boot:run            # roda em modo dev (com DevTools / hot reload)
./mvnw clean package              # gera o .jar em target/ (roda os testes; precisa do .env)
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

### Banco de dados (Neon)

- **SQL Editor:** no console do Neon, menu **SQL Editor**, para rodar consultas direto no navegador.
- **Tables:** visualize e edite os dados das tabelas.
- **Branches:** crie um branch (cópia instantânea do banco) para testar migrações sem afetar os dados principais — basta usar a URL do branch no `.env`.
- Cliente desktop opcional: [DBeaver](https://dbeaver.io/) ou [pgAdmin](https://www.pgadmin.org/) com os mesmos dados de conexão (SSL obrigatório).

---

## 🗄️ Migrações de banco (Flyway)

- O schema é controlado **somente** pelo Flyway (`spring.jpa.hibernate.ddl-auto=validate`).
- Crie novos arquivos em `backend/src/main/resources/db/migration` seguindo o padrão:
  `V<numero>__<descricao>.sql`, por exemplo `V2__create_inventario.sql`.
- **Nunca edite** uma migração que já foi aplicada; crie uma nova.
- As migrações rodam automaticamente ao iniciar o backend, usando a conexão **direta** (`DB_URL_UNPOOLED`).

---

## 🛠️ Solução de problemas

| Problema | Solução |
| -------- | ------- |
| `Could not resolve placeholder 'DB_URL'` | O `backend/.env` não foi encontrado. Confirme que ele existe e que você rodou o comando **dentro da pasta `backend/`**. |
| `password authentication failed` | Usuário/senha incorretos. Confira no Neon (**Connect** › *Show password*) ou redefina a senha em **Roles**. |
| `The server does not support SSL` / erro de SSL | Garanta que a URL termina com `?sslmode=require`. |
| Primeira requisição demora alguns segundos | Normal: o Neon suspende o banco após ~5 min sem uso (*scale to zero*) e "acorda" no primeiro acesso. |
| Erro do Flyway com `prepared statement` ou `search_path` | O Flyway está usando a conexão pooled. Defina `DB_URL_UNPOOLED` com o host **sem** `-pooler`. |
| `too many connections` | Reduza `spring.datasource.hikari.maximum-pool-size` ou verifique se há outras instâncias do backend rodando. |
| Porta `8080` ou `3000` em uso | Use `SERVER_PORT=8081` no `.env` e/ou `npm run dev -- -p 3001` (ajuste `NEXT_PUBLIC_API_URL` e `CORS_ALLOWED_ORIGINS`). |
| "API offline" no painel | Confira se o backend está rodando e se `NEXT_PUBLIC_API_URL` está correto. Reinicie o `npm run dev` após alterar o `.env.local`. |
| `JAVA_HOME not found` ao rodar `mvnw` | Instale o JDK 21 e configure a variável de ambiente `JAVA_HOME`. |

---

## 🗺️ Roadmap

- [x] Estrutura do monorepo (frontend e backend)
- [x] Configuração base: CORS, OpenAPI, Flyway, tratamento de erros
- [x] Banco PostgreSQL no Neon
- [x] Layout do frontend com navegação por módulos
- [ ] Modelagem do banco e CRUD de **Inventário**
- [ ] **Eventos** e calendário
- [ ] **Horários de missas** e intenções
- [ ] **Pastorais** e atividades
- [ ] Cadastro de **Pessoas**
- [ ] Autenticação e perfis de acesso (Spring Security + JWT)
- [ ] Deploy (frontend e backend)
