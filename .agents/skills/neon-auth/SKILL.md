---
name: neon-auth
description: >-
  Add authentication to a new app. Use for "add auth", "add login", Neon Auth
  (Managed Better Auth), identity routing, sign-up, sign-in, password reset,
  email OTP, magic links, organizations, phone OTP, OAuth, passkeys, MFA,
  trusted domains, invalid domain, and @neondatabase/auth. No existing identity:
  default to Managed Better Auth. Keep working Better Auth, Clerk, Supabase
  Auth, or another IdP. User asked to migrate from Supabase Auth: Managed
  Better Auth. A required plugin outside Managed support: self-managed Better
  Auth on a Neon Function or the existing app host. Also use for auth APIs in
  @neondatabase/neon-js.
metadata:
  parent: neon
  source: https://github.com/neondatabase/agent-skills/tree/main/skills/neon-auth
---

**FIRST**: Use the parent `neon` skill for a Neon overview, getting started with Neon, Neon development best practices, and more.

# Neon Auth

Neon Auth is Managed Better Auth: users, sessions, and auth config live in the `neon_auth` schema on the branch's Lakebase Postgres, and auth state branches with the database. The client API is the Better Auth method set (`signIn.email`, `signIn.social`, `getSession`) through `@neondatabase/auth`. That wrapper is not a drop-in for bare `better-auth/client`: it pins the plugin list and adds Neon-specific OAuth verifier, iframe popup, and JWT handling. Stay on the wrapper while Auth is managed.

## When to Use

| Situation | What to do |
| --- | --- |
| No existing auth | Default to Managed Better Auth. |
| Needs a feature Managed does not offer | Self-managed Better Auth on the existing app host. |
| Already has Better Auth | Keep it. |

## Next.js Integration

### Packages:
```bash
npm install @neondatabase/auth@latest @neondatabase/auth-ui
```

### Environment Variables:
```env
NEON_AUTH_BASE_URL=https://...
NEON_AUTH_COOKIE_SECRET=...
```

### Route Handler (`src/app/api/auth/[...path]/route.ts`):
```ts
import { auth } from '@/lib/auth/server';

export const { GET, POST } = auth.handler();
```

### Client Setup (`src/lib/auth/client.ts`):
```ts
'use client';
import { createAuthClient } from '@neondatabase/auth/next';

export const authClient = createAuthClient();
export const { signIn, signUp, signOut, useSession } = authClient;
```

### Server Setup (`src/lib/auth/server.ts`):
```ts
import { createNeonAuth } from '@neondatabase/auth/next/server';

export const auth = createNeonAuth({
  baseUrl: process.env.NEON_AUTH_BASE_URL!,
  cookies: {
    secret: process.env.NEON_AUTH_COOKIE_SECRET!,
  },
});
```
