import { apiClient } from './client';

export interface PresenceCreateDto {
  code: string;
  etudiantId: number;
}

export interface PresenceResponseDto {
  id: number;
  sessionId: number;
  etudiantId: number;
  source: 'ETUDIANT' | 'FORMATEUR';
}

/**
 * POST /api/presences — marquer la présence avec un code (EF1).
 * Erreurs possibles : 400 CODE_INCONNU, 409 DEJA_PRESENT, 410 CODE_EXPIRE,
 * 429 TROP_TENTATIVES (RG3).
 */
export async function marquerPresence(
  dto: PresenceCreateDto
): Promise<PresenceResponseDto> {
  const response = await apiClient.post<PresenceResponseDto>(
    '/api/presences',
    dto
  );
  return response.data;
}
