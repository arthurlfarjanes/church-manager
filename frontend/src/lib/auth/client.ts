"use client";

import { createAuthClient } from "@neondatabase/auth/next";

/**
 * Cliente de autenticação para uso em componentes React (Client Components).
 * Baseado no Managed Better Auth do Neon.
 */
export const authClient = createAuthClient();

export const { signIn, signUp, signOut, useSession } = authClient;
