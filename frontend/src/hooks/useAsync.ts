import { useState, useCallback } from 'react';
import { extractApiError, ApiError } from '../api/client';

export interface AsyncState<T> {
  data: T | null;
  loading: boolean;
  error: ApiError | null;
}

/**
 * Hook générique pour gérer chargement et erreur (F3).
 */
export function useAsync<T>() {
  const [state, setState] = useState<AsyncState<T>>({
    data: null,
    loading: false,
    error: null,
  });

  const run = useCallback(async (fn: () => Promise<T>) => {
    setState({ data: null, loading: true, error: null });
    try {
      const data = await fn();
      setState({ data, loading: false, error: null });
      return data;
    } catch (err) {
      setState({ data: null, loading: false, error: extractApiError(err) });
      return null;
    }
  }, []);

  return { ...state, run };
}
