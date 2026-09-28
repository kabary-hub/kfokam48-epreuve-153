import { apiClient } from './client';

export interface TableauLigne {
  etudiantId: number;
  nom: string;
  presences: number;
  exercicesDeposes: number;
  moyenne: number | null;
  relecturesEnAttente: number;
}

/**
 * GET /api/tableau?promotionId=X — tableau récapitulatif (M7, EF9).
 * La moyenne est calculée côté API (F3 du CDC).
 */
export async function chargerTableau(promotionId: number): Promise<TableauLigne[]> {
  const response = await apiClient.get<TableauLigne[]>('/api/tableau', {
    params: { promotionId },
  });
  return response.data;
}
