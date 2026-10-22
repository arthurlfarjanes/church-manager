import type { Metadata } from "next";
import { ModulePlaceholder } from "@/components/ui/ModulePlaceholder";

export const metadata: Metadata = { title: "Pastorais" };

export default function Page() {
  return <ModulePlaceholder slug="pastorais" />;
}