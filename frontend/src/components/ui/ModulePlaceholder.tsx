import { Hammer } from "lucide-react";
import { PageHeader } from "@/components/ui/PageHeader";
import { modules } from "@/config/modules";

/** Página padrão de um módulo ainda não implementado. */
export function ModulePlaceholder({ slug }: { slug: string }) {
  const mod = modules.find((m) => m.slug === slug);
  if (!mod) return null;

  return (
    <div className="mx-auto max-w-6xl">
      <PageHeader title={mod.title} description={mod.description} icon={mod.icon} />
      <div className="grid place-items-center rounded-2xl border border-dashed border-border bg-surface/50 px-6 py-20 text-center animate-fade-up">
        <Hammer className="mb-4 h-8 w-8 text-accent" />
        <p className="text-lg font-medium">Módulo em construção</p>
        <p className="mt-1 max-w-md text-sm text-muted">
          A estrutura está pronta. Os cadastros e listagens deste módulo serão
          implementados nas próximas etapas.
        </p>
      </div>
    </div>
  );
}
