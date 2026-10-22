"use client";

import Link from "next/link";
import { UserButton, SignedIn, SignedOut } from "@neondatabase/auth-ui";
import { LogIn } from "lucide-react";

export function UserNav() {
  return (
    <div className="flex items-center gap-3">
      <SignedIn>
        <UserButton />
      </SignedIn>
      <SignedOut>
        <Link
          href="/auth/sign-in"
          className="flex items-center gap-2 rounded-lg border border-border bg-surface-2 px-3 py-1.5 text-xs font-medium text-foreground transition-colors hover:border-primary/50 hover:bg-surface"
        >
          <LogIn className="h-3.5 w-3.5 text-accent" />
          <span>Entrar</span>
        </Link>
      </SignedOut>
    </div>
  );
}
