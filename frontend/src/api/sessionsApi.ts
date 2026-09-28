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

export interface SessionDetailDto {
  id: number;
  titre: string;
  code: string;
  ouvertureAt: string;
  expirationAt: string;
  clotureAt: string | null;
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

/**
 * POST /api/sessions/{id}/cloture — clôturer une session (M8, EF11).
 */
export async function cloturerSession(sessionId: number): Promise<SessionDetailDto> {
  const response = await apiClient.post<SessionDetailDto>(
    `/api/sessions/${sessionId}/cloture`
  );
  return response.data;
}
