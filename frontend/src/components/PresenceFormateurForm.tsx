import { useState, type FormEvent } from 'react';
import {
  ajouterPresenceFormateur,
  type PresenceResponseDto,
} from '../api/presencesApi';
import { useAsync } from '../hooks/useAsync';

/**
 * Formulaire d'ajout manuel de présence (M3, EF3, Q14).
 * La présence créée est marquée source = "FORMATEUR" (RG13).
 */
export function PresenceFormateurForm() {
  const [sessionId, setSessionId] = useState(1);
  const [etudiantId, setEtudiantId] = useState(1);
  const { data, loading, error, run } = useAsync<PresenceResponseDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await run(() => ajouterPresenceFormateur({ sessionId, etudiantId }));
  };

  return (
    <section className="presence-formateur-form">
      <h2>Ajouter une présence (formateur)</h2>
      <p>
        <em>La présence sera marquée « ajoutée par le formateur ».</em>
      </p>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="presence-formateur-session">Session</label>
          <input
            id="presence-formateur-session"
            type="number"
            min={1}
            value={sessionId}
            onChange={(event) => setSessionId(Number(event.target.value))}
            required
          />
        </div>

        <div>
          <label htmlFor="presence-formateur-etudiant">Étudiant</label>
          <select
            id="presence-formateur-etudiant"
            value={etudiantId}
            onChange={(event) => setEtudiantId(Number(event.target.value))}
          >
            <option value={1}>Étudiant Démo 1</option>
            <option value={2}>Étudiant Démo 2</option>
            <option value={3}>Étudiant Démo 3</option>
          </select>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Envoi…' : 'Ajouter la présence'}
        </button>
      </form>

      {error && (
        <div role="alert" className="error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="success">
          <p>Présence ajoutée (source : {data.source}).</p>
          <p>
            Session : {data.sessionId} — Étudiant : {data.etudiantId}
          </p>
        </div>
      )}
    </section>
  );
}
