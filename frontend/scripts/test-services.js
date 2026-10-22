const { neon } = require("@neondatabase/serverless");

const dbUrl =
  process.env.DATABASE_URL ||
  "postgresql://church_manager_owner:npg_da8x5KkzhZnT@ep-mute-truth-b65kgxri-pooler.c-2.sa-east-1.aws.neon.tech/church_manager?channel_binding=require&sslmode=require";

async function main() {
  console.log("--- 1. Testing Neon Postgres Connection ---");
  const sql = neon(dbUrl);
  const version = await sql`SELECT version(), current_database(), current_user`;
  console.log("Postgres version & user:", version[0]);

  const tables = await sql`
    SELECT table_schema, table_name 
    FROM information_schema.tables 
    WHERE table_schema IN ('public', 'neon_auth') 
    ORDER BY table_schema, table_name
  `;
  console.log("Tables in database:", tables);

  console.log("\n--- 2. Testing Neon Auth JWKS Endpoint ---");
  const authUrl =
    process.env.NEON_AUTH_BASE_URL ||
    "https://ep-mute-truth-b65kgxri.neonauth.c-2.sa-east-1.aws.neon.tech/church_manager/auth";
  const jwksRes = await fetch(`${authUrl}/.well-known/jwks.json`);
  console.log("JWKS status:", jwksRes.status);
  const jwksData = await jwksRes.json();
  console.log("JWKS keys count:", jwksData.keys ? jwksData.keys.length : 0);

  console.log("\n--- ALL NEON SERVICES VERIFIED END-TO-END ---");
}

main().catch((err) => {
  console.error("Verification failed:", err);
  process.exit(1);
});
