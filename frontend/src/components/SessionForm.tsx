import { useState, type FormEvent } from 'react';
import { ouvrirSession, type SessionResponseDto } from '../api/sessionsApi';
import { useAsync } from '../hooks/useAsync';

/**
 * Formulaire d'ouverture de session (M1, EF2).
 *
 * Champs :
 * - titre (texte)
 * - promotionId (sélection dans une liste — Q1 : pas d'auth, l'étudiant
 *   choisit son nom dans une liste, idem pour la promotion)
 *
 * Affiche :
 * - le code généré + horaires en cas de succès
 * - l'erreur {code, message} en cas d'échec
 */
export function SessionForm() {
  const [titre, setTitre] = useState('');
  const [promotionId, setPromotionId] = useState(1);
  const { data, loading, error, run } = useAsync<SessionResponseDto>();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    await run(() => ouvrirSession({ titre, promotionId }));
  };

  return (
    <div className="session-form">
      <h2>Ouvrir une session</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="titre">Titre de la session</label>
          <input
            id="titre"
            type="text"
            value={titre}
            onChange={(e) => setTitre(e.target.value)}
            required
            maxLength={255}
          />
        </div>

        <div>
          <label htmlFor="promotionId">Promotion</label>
          <select
            id="promotionId"
            value={promotionId}
            onChange={(e) => setPromotionId(Number(e.target.value))}
          >
            <option value={1}>KFOKAM48 — Promotion 2026</option>
          </select>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Ouverture…' : 'Ouvrir une session'}
        </button>
      </form>

      {error && (
        <div role="alert" className="error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div className="success">
          <p>Session ouverte avec succès.</p>
          <p>
            Code de présence : <strong>{data.code}</strong>
          </p>
          <p>Ouverture : {new Date(data.ouvertureAt).toLocaleString()}</p>
          <p>Expiration : {new Date(data.expirationAt).toLocaleString()}</p>
        </div>
      )}
    </div>
  );
}
