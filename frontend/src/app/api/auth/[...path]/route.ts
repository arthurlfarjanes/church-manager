import { auth } from "@/lib/auth/server";

/**
 * Route Handler que encaminha requisições de autenticação ao Managed Better Auth do Neon.
 */
export const { GET, POST } = auth.handler();
