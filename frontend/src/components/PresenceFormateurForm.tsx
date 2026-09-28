import { useState, type FormEvent } from 'react';
import {
  ajouterPresenceFormateur,
  type PresenceResponseDto,
} from '../api/presencesApi';
import { useAsync } from '../hooks/useAsync';

/** Formulaire d'ajout manuel de présence (M3, EF3, Q14). */
export function PresenceFormateurForm() {
  const [sessionId, setSessionId] = useState(1);
  const [etudiantId, setEtudiantId] = useState(1);
  const { data, loading, error, run } = useAsync<PresenceResponseDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await run(() => ajouterPresenceFormateur({ sessionId, etudiantId }));
  };

  return (
    <section className="card">
      <h2>Ajouter une présence</h2>
      <p className="text-muted text-small">
        Cette présence sera identifiée comme ajoutée par le formateur.
      </p>

      <form onSubmit={handleSubmit}>
        <div className="form-group">
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

        <div className="form-group">
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

        <button type="submit" className="btn" disabled={loading}>
          {loading ? 'Envoi…' : 'Ajouter la présence'}
        </button>
      </form>

      {error && (
        <div role="alert" className="alert alert-error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="alert alert-success">
          <p><strong>Présence ajoutée (source : {data.source}).</strong></p>
          <p className="text-small text-muted">
            Session : {data.sessionId} — Étudiant : {data.etudiantId}
          </p>
        </div>
      )}
    </section>
  );
}
