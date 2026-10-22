import { neon } from "@neondatabase/serverless";

/**
 * Cliente SQL para o Lakebase Postgres do Neon no Next.js.
 * Utiliza o driver serverless oficial com a conexão pooled (recomendado pelo skill neon-postgres).
 */
export const sql = neon(
  process.env.DATABASE_URL ||
    "postgresql://church_manager_owner:npg_da8x5KkzhZnT@ep-mute-truth-b65kgxri-pooler.c-2.sa-east-1.aws.neon.tech/church_manager?channel_binding=require&sslmode=require"
);
