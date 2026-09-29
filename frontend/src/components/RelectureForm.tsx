import { useState, type FormEvent } from 'react';
import {
  rendreRelecture,
  type RelectureResponseDto,
} from '../api/relecturesApi';
import { useAsync } from '../hooks/useAsync';

/** Formulaire de relecture (M6, EF7 + issue #53). */
export function RelectureForm() {
  const [exerciceId, setExerciceId] = useState(1);
  const [relecteurId, setRelecteurId] = useState<string>('');
  const [note, setNote] = useState(15);
  const [commentaire, setCommentaire] = useState('');
  const { data, loading, error, run } = useAsync<RelectureResponseDto>();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const rid = relecteurId.trim() === '' ? undefined : Number(relecteurId);
    await run(() => rendreRelecture(exerciceId, { note, commentaire }, rid));
  };

  return (
    <section className="card">
      <h2>Rendre une relecture</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="exercice-id-relecture">Identifiant de l’exercice</label>
          <input
            id="exercice-id-relecture"
            type="number"
            min={1}
            step={1}
            value={exerciceId}
            onChange={(event) => setExerciceId(Number(event.target.value))}
            required
          />
          <p className="form-help">
            Saisissez l’identifiant de l’exercice qui vous a été attribué.
          </p>
        </div>

        <div className="form-group">
          <label htmlFor="relecteur-id-relecture">
            Identifiant du relecteur (optionnel)
          </label>
          <input
            id="relecteur-id-relecture"
            type="number"
            min={1}
            step={1}
            value={relecteurId}
            onChange={(event) => setRelecteurId(event.target.value)}
            placeholder="Laisser vide pour choisir automatiquement"
          />
          <p className="form-help">
            Si vous laissez vide, le système choisit le premier relecteur
            assigné qui n’a pas encore rendu sa relecture.
          </p>
        </div>

        <div className="form-group">
          <label htmlFor="note-relecture">Note (0 à 20)</label>
          <input
            id="note-relecture"
            type="number"
            min={0}
            max={20}
            step={1}
            value={note}
            onChange={(event) => setNote(Number(event.target.value))}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="commentaire-relecture">Commentaire</label>
          <textarea
            id="commentaire-relecture"
            value={commentaire}
            onChange={(event) => setCommentaire(event.target.value)}
            rows={4}
            maxLength={2000}
          />
        </div>

        <button type="submit" className="btn" disabled={loading}>
          {loading ? 'Envoi…' : 'Rendre la relecture'}
        </button>
      </form>

      {error && (
        <div role="alert" className="alert alert-error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && (
        <div role="status" className="alert alert-success">
          <p><strong>Relecture enregistrée.</strong></p>
          <p className="text-small text-muted">
            Statut : {data.statut} — Exercice #{data.exerciceId}
          </p>
        </div>
      )}
    </section>
  );
}
