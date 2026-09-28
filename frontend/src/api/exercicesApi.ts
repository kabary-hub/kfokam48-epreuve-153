import { apiClient } from './client';

export interface ExerciceCreateDto {
  sessionId: number;
  etudiantId: number;
  lien: string;
}

export interface ExerciceResponseDto {
  id: number;
  statut: string;
}

/**
 * POST /api/exercices — déposer un exercice (EF4).
 */
export async function deposerExercice(
  dto: ExerciceCreateDto
): Promise<ExerciceResponseDto> {
  const response = await apiClient.post<ExerciceResponseDto>(
    '/api/exercices',
    dto
  );
  return response.data;
}
