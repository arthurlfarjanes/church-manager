import {
  Boxes,
  CalendarDays,
  Church,
  HandHeart,
  LayoutDashboard,
  Users,
  type LucideIcon,
} from "lucide-react";

export type AppModule = {
  slug: string;
  href: string;
  title: string;
  description: string;
  icon: LucideIcon;
};

/** Módulos do sistema — usados na navegação e no dashboard. */
export const modules: AppModule[] = [
  {
    slug: "inventario",
    href: "/inventario",
    title: "Inventário",
    description: "Bens, objetos litúrgicos, móveis e equipamentos da paróquia.",
    icon: Boxes,
  },
  {
    slug: "eventos",
    href: "/eventos",
    title: "Eventos",
    description: "Festas, retiros, encontros e reservas de espaços.",
    icon: CalendarDays,
  },
  {
    slug: "missas",
    href: "/missas",
    title: "Missas",
    description: "Horários de celebrações, intenções e escalas.",
    icon: Church,
  },
  {
    slug: "pastorais",
    href: "/pastorais",
    title: "Pastorais",
    description: "Pastorais, coordenadores, membros e atividades.",
    icon: HandHeart,
  },
  {
    slug: "pessoas",
    href: "/pessoas",
    title: "Pessoas",
    description: "Paroquianos, voluntários e agentes de pastoral.",
    icon: Users,
  },
];

export const dashboardItem = {
  href: "/",
  title: "Painel",
  icon: LayoutDashboard,
};
