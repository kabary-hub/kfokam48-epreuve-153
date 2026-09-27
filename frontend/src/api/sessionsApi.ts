import { apiClient } from './client';

export interface SessionCreateDto {
  titre: string;
  promotionId: number;
}

export interface SessionResponseDto {
  id: number;
  code: string;
  ouvertureAt: string;
  expirationAt: string;
}

/**
 * POST /api/sessions — ouvrir une session et obtenir un code (EF2).
 */
export async function ouvrirSession(
  dto: SessionCreateDto
): Promise<SessionResponseDto> {
  const response = await apiClient.post<SessionResponseDto>(
    '/api/sessions',
    dto
  );
  return response.data;
}
