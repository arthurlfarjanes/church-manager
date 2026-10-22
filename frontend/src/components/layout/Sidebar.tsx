"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import clsx from "clsx";
import { Cross } from "lucide-react";
import { dashboardItem, modules } from "@/config/modules";

const navItems = [dashboardItem, ...modules];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="hidden md:flex w-64 shrink-0 flex-col border-r border-border bg-surface/70 backdrop-blur-xl">
      <div className="flex items-center gap-3 px-6 py-6">
        <span className="grid h-10 w-10 place-items-center rounded-xl bg-gradient-to-br from-primary to-accent/80 shadow-lg shadow-primary/30">
          <Cross className="h-5 w-5 text-white" />
        </span>
        <div>
          <p className="font-display text-xl font-semibold leading-none">
            Church Manager
          </p>
          <p className="text-xs text-muted">Gestão paroquial</p>
        </div>
      </div>

      <nav className="flex flex-1 flex-col gap-1 px-3">
        {navItems.map(({ href, title, icon: Icon }) => {
          const active =
            href === "/" ? pathname === "/" : pathname.startsWith(href);
          return (
            <Link
              key={href}
              id={`nav-${href === "/" ? "painel" : href.slice(1)}`}
              href={href}
              className={clsx(
                "group flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm transition-all",
                active
                  ? "bg-primary/20 text-foreground ring-1 ring-primary/40"
                  : "text-muted hover:bg-surface-2 hover:text-foreground",
              )}
            >
              <Icon
                className={clsx(
                  "h-4 w-4 transition-transform group-hover:scale-110",
                  active ? "text-accent" : "",
                )}
              />
              {title}
            </Link>
          );
        })}
      </nav>

      <p className="px-6 py-4 text-xs text-muted/70">v0.1.0</p>
    </aside>
  );
}
