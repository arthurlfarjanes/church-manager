// Tipos compartilhados (espelham os DTOs da API Java).

export type UUID = string;

export type Page<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
};
