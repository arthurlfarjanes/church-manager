import { createNeonAuth } from "@neondatabase/auth/next/server";

/**
 * Cliente Neon Auth para Server Components, Server Actions e Route Handlers.
 */
export const auth = createNeonAuth({
  baseUrl:
    process.env.NEON_AUTH_BASE_URL ||
    "https://ep-mute-truth-b65kgxri.neonauth.c-2.sa-east-1.aws.neon.tech/church_manager/auth",
  cookies: {
    secret:
      process.env.NEON_AUTH_COOKIE_SECRET ||
      "church_manager_neon_better_auth_secret_32_chars_ok",
  },
});
