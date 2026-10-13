# Microservice template (Spring Boot 3 · Java 21 · Hexagonal)

Starting point for new microservices: REST + JPA + Flyway, an **optional** JWT authentication
module and an initial business module (`sample`) you can rename with one command.

## Quick start

```bash
cp .env.example .env        # then export the variables, or set them in your IDE
docker compose up -d        # local PostgreSQL (optional)
./gradlew bootRun
```

By default authentication is **off** and every endpoint is public.

## Customize

| What | How |
|---|---|
| Module name | `./gradlew renameModule -PfromName=product -PmoduleName=item` (packages, classes, table, endpoints, migration file) |
| Service name | `rootProject.name` in `settings.gradle` and `APP_NAME` |
| Base package / group | rename `com.santiagomac.template` and `group` in `build.gradle` with your IDE |
| Database | `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, or a full `DB_URL`. Another engine: swap the driver and `flyway-database-*` in `build.gradle` |
| Schema | Flyway migrations in `src/main/resources/db/migration/common` (`V3__...`, `V4__...`) |
| Authentication | `APP_AUTH_ENABLED=true` + `APP_AUTH_JWT_SECRET` (≥ 32 chars) |
| CORS | `APP_CORS_ALLOWED_ORIGINS=https://app.example.com` |

All variables are listed in [.env.example](.env.example).

## Optional authentication

`APP_AUTH_ENABLED=true` activates (and `false` fully removes from the context) the `auth` package:

- `POST /api/v1/auth/signup` · `login` · `refreshToken` (rotates the refresh token) · `logout`
- Tokens travel in `HttpOnly` cookies **or** an `Authorization: Bearer` header (access tokens only).
- Everything else requires authentication; use `@PreAuthorize("hasRole('ADMIN')")` for roles.
- Self-registration always creates a `USER`. Promote an admin directly in the DB:
  `UPDATE users SET role_id = '00000000-0000-0000-0000-000000000001' WHERE email = '...'`.
- Set `APP_AUTH_COOKIE_SECURE=false` only for local plain-http development.

## Structure

```plaintext
com.santiagomac.template
├── TemplateApplication
├── common/                 # shared: error handling (RFC 7807), CORS, open security chain
├── auth/                   # OPTIONAL module (domain · application · infrastructure)
└── modules/
    └── product/            # your business module (domain · application · infrastructure)
```

Dependency rule: `infrastructure → application → domain`. The domain/application layers
do not import Spring Security; adapters implement the ports.

## Adding another module

`./gradlew createModule -PmoduleName=order` generates `modules/order` (domain, application, infrastructure, CRUD at `/api/v1/orders`) from `templates/module` plus the next Flyway migration. Add `-PmodulePlural=...` for irregular plurals. Edit the templates to change what new modules look like.

## Tests

`./gradlew test` runs against in-memory H2 (profile `test`), once with auth disabled and once enabled.
