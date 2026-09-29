import { apiClient } from './client';

export interface RelectureCreateDto {
  note: number;
  commentaire: string;
}

export interface RelectureResponseDto {
  id: number;
  exerciceId: number;
  statut: string;
}

/**
 * POST /api/relectures/{id} — rendre une relecture (EF7).
 * {id} est l'identifiant de l'EXERCICE (conforme au contrat).
 *
 * @param exerciceId  identifiant de l'exercice à relire
 * @param dto         note et commentaire
 * @param relecteurId identifiant du relecteur (optionnel, issue #53).
 *                    Si absent, le backend choisit automatiquement le
 *                    premier relecteur assigné qui n'a pas encore rendu.
 */
export async function rendreRelecture(
  exerciceId: number,
  dto: RelectureCreateDto,
  relecteurId?: number
): Promise<RelectureResponseDto> {
  const params = relecteurId != null ? { relecteurId } : undefined;
  const response = await apiClient.post<RelectureResponseDto>(
    `/api/relectures/${exerciceId}`,
    dto,
    { params }
  );
  return response.data;
}
