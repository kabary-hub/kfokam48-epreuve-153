import { useState, type FormEvent } from 'react';
import { cloturerSession, type SessionDetailDto } from '../api/sessionsApi';
import { useAsync } from '../hooks/useAsync';

/**
 * Formulaire de clôture de session (M8, EF11).
 * Après clôture, plus aucun dépôt ni modification de relecture n'est
 * possible (RG11, RG14, Q12).
 */
export function SessionClotureForm() {
  const [sessionId, setSessionId] = useState(1);
  const { data, loading, error, run } = useAsync<SessionDetailDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!window.confirm('Clôturer cette session ? Cette action est définitive.')) {
      return;
    }
    await run(() => cloturerSession(sessionId));
  };

  return (
    <section className="card">
      <h2>Clôturer une session</h2>
      <p className="text-small text-muted">
        Après clôture, plus aucun dépôt ni modification de relecture n'est possible.
      </p>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="sessionId-cloture">Identifiant de la session</label>
          <input
            id="sessionId-cloture"
            type="number"
            min={1}
            value={sessionId}
            onChange={(event) => setSessionId(Number(event.target.value))}
            required
          />
        </div>

        <button type="submit" className="btn btn-secondary" disabled={loading}>
          {loading ? 'Clôture…' : 'Clôturer la session'}
        </button>
      </form>

      {error && (
        <div role="alert" className="alert alert-error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="alert alert-success">
          <p><strong>Session clôturée.</strong></p>
          <p className="text-small text-muted">
            Session #{data.id} — clôturée le{' '}
            {data.clotureAt && new Date(data.clotureAt).toLocaleString()}
          </p>
        </div>
      )}
    </section>
  );
}
