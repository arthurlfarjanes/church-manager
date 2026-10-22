import React, { Suspense } from "react";
import type { Metadata } from "next";
import { Cormorant_Garamond, Inter } from "next/font/google";
import { Sidebar } from "@/components/layout/Sidebar";
import { AuthProvider } from "@/components/auth/AuthProvider";
import "./globals.css";

const inter = Inter({
  variable: "--font-inter",
  subsets: ["latin"],
});

const cormorant = Cormorant_Garamond({
  variable: "--font-cormorant",
  subsets: ["latin"],
  weight: ["500", "600", "700"],
});

export const metadata: Metadata = {
  title: {
    default: "Church Manager — Gestão Paroquial",
    template: "%s | Church Manager",
  },
  description:
    "Gerencie inventário, eventos, horários de missas e pastorais da sua paróquia em um só lugar.",
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html
      lang="pt-BR"
      className={`${inter.variable} ${cormorant.variable} h-full antialiased`}
    >
      <body className="min-h-full flex">
        <AuthProvider>
          <Suspense
            fallback={
              <aside className="hidden md:flex w-64 shrink-0 border-r border-border bg-surface/70" />
            }
          >
            <Sidebar />
          </Suspense>
          <main className="flex-1 min-w-0 px-6 py-8 md:px-10">{children}</main>
        </AuthProvider>
      </body>
    </html>
  );
}
