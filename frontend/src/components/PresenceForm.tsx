import { useState, type FormEvent } from 'react';
import { marquerPresence, type PresenceResponseDto } from '../api/presencesApi';
import { useAsync } from '../hooks/useAsync';

/**
 * Formulaire de marquage de présence (M2, EF1).
 * L'identité de l'étudiant est choisie dans la liste de démonstration (Q1).
 */
export function PresenceForm() {
  const [code, setCode] = useState('');
  const [etudiantId, setEtudiantId] = useState(1);
  const { data, loading, error, run } = useAsync<PresenceResponseDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await run(() => marquerPresence({ code: code.trim().toUpperCase(), etudiantId }));
  };

  return (
    <section className="presence-form">
      <h2>Marquer ma présence</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="presence-code">Code de présence</label>
          <input
            id="presence-code"
            type="text"
            value={code}
            onChange={(event) => setCode(event.target.value.toUpperCase())}
            required
            maxLength={6}
            minLength={6}
            pattern="[A-Z0-9]{6}"
            placeholder="ABC123"
            autoComplete="off"
          />
        </div>

        <div>
          <label htmlFor="presence-etudiant">Étudiant</label>
          <select
            id="presence-etudiant"
            value={etudiantId}
            onChange={(event) => setEtudiantId(Number(event.target.value))}
          >
            <option value={1}>Étudiant Démo 1</option>
            <option value={2}>Étudiant Démo 2</option>
            <option value={3}>Étudiant Démo 3</option>
          </select>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Envoi…' : 'Marquer ma présence'}
        </button>
      </form>

      {error && (
        <div role="alert" className="error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="success">
          <p>Présence enregistrée.</p>
          <p>Session : {data.sessionId}</p>
          <p>Source : {data.source}</p>
        </div>
      )}
    </section>
  );
}
