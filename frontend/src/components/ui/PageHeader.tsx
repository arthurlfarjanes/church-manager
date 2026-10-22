import type { LucideIcon } from "lucide-react";

type PageHeaderProps = {
  title: string;
  description?: string;
  icon?: LucideIcon;
};

export function PageHeader({ title, description, icon: Icon }: PageHeaderProps) {
  return (
    <header className="mb-8 flex items-start gap-4 animate-fade-up">
      {Icon && (
        <span className="grid h-12 w-12 place-items-center rounded-2xl border border-border bg-surface-2">
          <Icon className="h-6 w-6 text-accent" />
        </span>
      )}
      <div>
        <h1 className="font-display text-4xl font-semibold tracking-tight">
          {title}
        </h1>
        {description && <p className="mt-1 text-muted">{description}</p>}
      </div>
    </header>
  );
}
