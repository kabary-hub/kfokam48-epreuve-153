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
 */
export async function rendreRelecture(
  exerciceId: number,
  dto: RelectureCreateDto
): Promise<RelectureResponseDto> {
  const response = await apiClient.post<RelectureResponseDto>(
    `/api/relectures/${exerciceId}`,
    dto
  );
  return response.data;
}
