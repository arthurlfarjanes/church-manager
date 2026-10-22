import type { Metadata } from "next";
import { ModulePlaceholder } from "@/components/ui/ModulePlaceholder";

export const metadata: Metadata = { title: "Missas" };

export default function Page() {
  return <ModulePlaceholder slug="missas" />;
}