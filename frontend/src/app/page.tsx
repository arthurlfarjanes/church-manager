import Link from "next/link";
import { ArrowUpRight } from "lucide-react";
import { modules } from "@/config/modules";
import { ApiStatus } from "@/components/ui/ApiStatus";

export default function DashboardPage() {
  return (
    <div className="mx-auto max-w-6xl">
      <section className="relative mb-10 overflow-hidden rounded-3xl border border-border bg-gradient-to-br from-surface-2 via-surface to-background p-8 md:p-12 animate-fade-up">
        <div className="pointer-events-none absolute -right-20 -top-20 h-72 w-72 rounded-full bg-primary/30 blur-3xl" />
        <div className="pointer-events-none absolute -bottom-24 right-40 h-56 w-56 rounded-full bg-accent/20 blur-3xl" />

        <ApiStatus />
        <h1 className="mt-5 font-display text-5xl font-semibold tracking-tight md:text-6xl">
          Paz e bem! <span className="text-accent">✦</span>
        </h1>
        <p className="mt-3 max-w-xl text-lg text-muted">
          Bem-vindo ao Church Manager. Organize o inventário, a agenda de
          eventos, os horários de missas e as pastorais da sua paróquia.
        </p>
      </section>

      <h2 className="mb-4 text-sm font-medium uppercase tracking-widest text-muted">
        Módulos
      </h2>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {modules.map(({ slug, href, title, description, icon: Icon }, i) => (
          <Link
            key={slug}
            id={`card-${slug}`}
            href={href}
            style={{ animationDelay: `${i * 60}ms` }}
            className="group relative rounded-2xl border border-border bg-surface/80 p-6 transition-all duration-300 hover:-translate-y-1 hover:border-primary/50 hover:shadow-xl hover:shadow-primary/10 animate-fade-up"
          >
            <div className="mb-4 flex items-center justify-between">
              <span className="grid h-11 w-11 place-items-center rounded-xl bg-primary/15 ring-1 ring-primary/30">
                <Icon className="h-5 w-5 text-accent" />
              </span>
              <ArrowUpRight className="h-4 w-4 text-muted opacity-0 transition-all group-hover:translate-x-0.5 group-hover:opacity-100" />
            </div>
            <h3 className="text-lg font-semibold">{title}</h3>
            <p className="mt-1 text-sm text-muted">{description}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
