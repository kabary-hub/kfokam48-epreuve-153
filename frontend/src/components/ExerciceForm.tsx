import { useState, type FormEvent } from 'react';
import {
  deposerExercice,
  type ExerciceResponseDto,
} from '../api/exercicesApi';
import { useAsync } from '../hooks/useAsync';

/** Formulaire de dépôt d'exercice (M4, EF4). */
export function ExerciceForm() {
  const [sessionId, setSessionId] = useState(1);
  const [etudiantId, setEtudiantId] = useState(1);
  const [lien, setLien] = useState('');
  const { data, loading, error, run } = useAsync<ExerciceResponseDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await run(() => deposerExercice({ sessionId, etudiantId, lien: lien.trim() }));
  };

  return (
    <section className="exercice-form">
      <h2>Déposer mon exercice</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="exercice-session-id">Session</label>
          <input
            id="exercice-session-id"
            type="number"
            min={1}
            value={sessionId}
            onChange={(event) => setSessionId(Number(event.target.value))}
            required
          />
        </div>

        <div>
          <label htmlFor="exercice-etudiant-id">Étudiant</label>
          <select
            id="exercice-etudiant-id"
            value={etudiantId}
            onChange={(event) => setEtudiantId(Number(event.target.value))}
          >
            <option value={1}>Étudiant Démo 1</option>
            <option value={2}>Étudiant Démo 2</option>
            <option value={3}>Étudiant Démo 3</option>
          </select>
        </div>

        <div>
          <label htmlFor="exercice-lien">Lien de l'exercice (URL)</label>
          <input
            id="exercice-lien"
            type="url"
            value={lien}
            onChange={(event) => setLien(event.target.value)}
            required
            maxLength={500}
            placeholder="https://…"
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Dépôt…' : 'Déposer'}
        </button>
      </form>

      {error && (
        <div role="alert" className="error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="success">
          <p>Exercice déposé (statut : {data.statut}).</p>
        </div>
      )}
    </section>
  );
}
