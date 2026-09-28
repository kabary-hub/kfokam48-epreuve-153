import { useState, type FormEvent } from 'react';
import { ouvrirSession, type SessionResponseDto } from '../api/sessionsApi';
import { useAsync } from '../hooks/useAsync';

/** Formulaire d'ouverture de session (M1, EF2). */
export function SessionForm() {
  const [titre, setTitre] = useState('');
  const [promotionId, setPromotionId] = useState(1);
  const { data, loading, error, run } = useAsync<SessionResponseDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await run(() => ouvrirSession({ titre, promotionId }));
  };

  return (
    <section className="card">
      <h2>Ouvrir une session</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="titre">Titre de la session</label>
          <input
            id="titre"
            type="text"
            value={titre}
            onChange={(event) => setTitre(event.target.value)}
            required
            maxLength={255}
          />
        </div>

        <div className="form-group">
          <label htmlFor="promotionId">Promotion</label>
          <select
            id="promotionId"
            value={promotionId}
            onChange={(event) => setPromotionId(Number(event.target.value))}
          >
            <option value={1}>KFOKAM48 — Promotion 2026</option>
          </select>
        </div>

        <button type="submit" className="btn" disabled={loading}>
          {loading ? 'Ouverture…' : 'Ouvrir la session'}
        </button>
      </form>

      {error && (
        <div role="alert" className="alert alert-error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="alert alert-success">
          <p><strong>Session ouverte.</strong></p>
          <p>Code de présence : <code>{data.code}</code></p>
          <p className="text-small text-muted">
            Ouverture : {new Date(data.ouvertureAt).toLocaleString()}<br />
            Expiration : {new Date(data.expirationAt).toLocaleString()}
          </p>
        </div>
      )}
    </section>
  );
}
