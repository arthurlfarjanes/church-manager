import { sql } from "@/lib/db";
import { Database, ShieldCheck } from "lucide-react";

export function NeonStatusSkeleton() {
  return (
    <div className="flex flex-wrap items-center gap-2">
      <span className="inline-flex items-center gap-2 rounded-full border border-border bg-surface px-3 py-1 text-xs text-muted">
        <Database className="h-3.5 w-3.5 text-accent" />
        <span className="h-2 w-2 rounded-full bg-amber-400 animate-pulse" />
        Conectando Neon...
      </span>
      <span className="inline-flex items-center gap-2 rounded-full border border-border bg-surface px-3 py-1 text-xs text-muted">
        <ShieldCheck className="h-3.5 w-3.5 text-accent" />
        <span className="h-2 w-2 rounded-full bg-emerald-400 shadow-[0_0_8px] shadow-emerald-400" />
        Neon Auth
      </span>
    </div>
  );
}

export async function NeonStatus() {
  let dbStatus: "online" | "offline" = "offline";
  let pgVersion = "17";

  try {
    const result = await sql`SELECT version() as v`;
    if (result && result.length > 0) {
      dbStatus = "online";
      const vStr = String(result[0].v);
      const match = vStr.match(/PostgreSQL (\d+\.\d+)/);
      if (match) {
        pgVersion = match[1];
      }
    }
  } catch {
    dbStatus = "offline";
  }

  return (
    <div className="flex flex-wrap items-center gap-2">
      <span
        id="neon-db-status"
        className="inline-flex items-center gap-2 rounded-full border border-border bg-surface px-3 py-1 text-xs text-muted"
      >
        <Database className="h-3.5 w-3.5 text-accent" />
        <span
          className={`h-2 w-2 rounded-full ${
            dbStatus === "online"
              ? "bg-emerald-400 shadow-[0_0_8px] shadow-emerald-400"
              : "bg-rose-500"
          }`}
        />
        {dbStatus === "online"
          ? `Neon Postgres (v${pgVersion})`
          : "Neon Postgres offline"}
      </span>

      <span
        id="neon-auth-status"
        className="inline-flex items-center gap-2 rounded-full border border-border bg-surface px-3 py-1 text-xs text-muted"
      >
        <ShieldCheck className="h-3.5 w-3.5 text-accent" />
        <span className="h-2 w-2 rounded-full bg-emerald-400 shadow-[0_0_8px] shadow-emerald-400" />
        Neon Auth (Better Auth)
      </span>
    </div>
  );
}
