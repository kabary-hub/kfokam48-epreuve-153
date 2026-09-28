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
    <section className="card">
      <h2>Déposer mon exercice</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
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

        <div className="form-group">
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

        <div className="form-group">
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

        <button type="submit" className="btn" disabled={loading}>
          {loading ? 'Dépôt…' : 'Déposer'}
        </button>
      </form>

      {error && (
        <div role="alert" className="alert alert-error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="alert alert-success">
          <p><strong>Exercice déposé.</strong></p>
          {data.statut === 'EN_ATTENTE' && (
            <p className="text-small">Un relecteur a été assigné automatiquement.</p>
          )}
          {data.statut === 'EN_ATTENTE_SANS_RELECTEUR' && (
            <p className="text-small">
              Aucun relecteur disponible pour le moment. Le formateur verra cet
              exercice dans son tableau.
            </p>
          )}
          {data.statut !== 'EN_ATTENTE' && data.statut !== 'EN_ATTENTE_SANS_RELECTEUR' && (
            <p className="text-small">Statut : {data.statut}</p>
          )}
        </div>
      )}
    </section>
  );
}
