import axios, { AxiosInstance } from 'axios';

/**
 * Client HTTP partagé pour tous les appels API.
 * Aucun fetch dispersé dans les composants (F3).
 */
export const apiClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
  timeout: 10000,
});

/**
 * Format d'erreur imposé par le contrat : {code, message}.
 */
export interface ApiError {
  code: string;
  message: string;
}

/**
 * Extrait une ApiError d'une réponse Axios, ou renvoie un fallback.
 */
export function extractApiError(error: unknown): ApiError {
  if (axios.isAxiosError(error) && error.response?.data) {
    const data = error.response.data;
    if (data.code && data.message) {
      return { code: data.code, message: data.message };
    }
  }
  return {
    code: 'ERREUR_INCONNUE',
    message: 'Une erreur est survenue.',
  };
}
