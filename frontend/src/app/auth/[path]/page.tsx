import React, { Suspense } from "react";
import { AuthView } from "@neondatabase/auth-ui";

type PageProps = {
  params: Promise<{ path: string }>;
};

async function AuthContent({ params }: PageProps) {
  const { path } = await params;
  return <AuthView pathname={path} />;
}

export default function AuthDynamicPage(props: PageProps) {
  return (
    <div className="flex min-h-[70vh] items-center justify-center p-4">
      <div className="w-full max-w-md rounded-2xl border border-border bg-surface/90 p-6 shadow-2xl backdrop-blur-xl">
        <Suspense
          fallback={
            <div className="p-8 text-center text-muted">
              Carregando formulário...
            </div>
          }
        >
          <AuthContent {...props} />
        </Suspense>
      </div>
    </div>
  );
}
