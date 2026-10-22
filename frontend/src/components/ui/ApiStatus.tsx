"use client";

import { useEffect, useState } from "react";
import clsx from "clsx";
import { api } from "@/lib/api";

type Status = "checking" | "online" | "offline";

/** Indica se a API Java está acessível (GET /api/ping). */
export function ApiStatus() {
  const [status, setStatus] = useState<Status>("checking");

  useEffect(() => {
    let cancelled = false;
    api
      .get<{ status: string }>("/ping")
      .then(() => !cancelled && setStatus("online"))
      .catch(() => !cancelled && setStatus("offline"));
    return () => {
      cancelled = true;
    };
  }, []);

  const label = {
    checking: "Verificando API…",
    online: "API conectada",
    offline: "API offline",
  }[status];

  return (
    <span
      id="api-status"
      className="inline-flex items-center gap-2 rounded-full border border-border bg-surface px-3 py-1 text-xs text-muted"
    >
      <span
        className={clsx(
          "h-2 w-2 rounded-full",
          status === "online" && "bg-emerald-400 shadow-[0_0_8px] shadow-emerald-400",
          status === "offline" && "bg-rose-500",
          status === "checking" && "bg-amber-400 animate-pulse",
        )}
      />
      {label}
    </span>
  );
}
