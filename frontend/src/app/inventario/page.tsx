import type { Metadata } from "next";
import { ModulePlaceholder } from "@/components/ui/ModulePlaceholder";

export const metadata: Metadata = { title: "Inventário" };

export default function Page() {
  return <ModulePlaceholder slug="inventario" />;
}