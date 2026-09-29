import { useEffect, useState } from 'react';
import { chargerTableau, type TableauLigne } from '../api/tableauApi';
import { useAsync } from '../hooks/useAsync';

/** Tableau récapitulatif du formateur (M7, EF9 + issue #53). */
export function TableauFormateur() {
  const [promotionId, setPromotionId] = useState(1);
  const { data, loading, error, run } = useAsync<TableauLigne[]>();

  useEffect(() => {
    void run(() => chargerTableau(promotionId));
  }, [promotionId, run]);

  return (
    <section className="card tableau-card">
      <h2>Tableau récapitulatif</h2>

      <div className="form-group">
        <label htmlFor="promotionId-tableau">Promotion</label>
        <select
          id="promotionId-tableau"
          value={promotionId}
          onChange={(event) => setPromotionId(Number(event.target.value))}
        >
          <option value={1}>KFOKAM48 — Promotion 2026</option>
        </select>
      </div>

      {loading && <p className="text-muted" role="status">Chargement…</p>}

      {error && (
        <div role="alert" className="alert alert-error">
          <strong>{error.code}</strong> : {error.message}
        </div>
      )}

      {data && data.length === 0 && (
        <p className="text-muted">Aucun étudiant dans cette promotion.</p>
      )}

      {data && data.length > 0 && (
        <div className="tableau-scroll">
          <table className="tableau">
            <thead>
              <tr>
                <th>Étudiant</th>
                <th>Présences</th>
                <th>Dépôts</th>
                <th>Moyenne</th>
                <th>Relectures en attente</th>
              </tr>
            </thead>
            <tbody>
              {data.map((ligne) => (
                <tr key={ligne.etudiantId}>
                  <td>{ligne.nom}</td>
                  <td>{ligne.presences}</td>
                  <td>{ligne.exercicesDeposes}</td>
                  <td>
                    {ligne.moyenne === null ? '—' : ligne.moyenne.toFixed(2)}
                    {ligne.provisoire && (
                      <span className="badge badge-provisoire" title="Note provisoire : une seule relecture rendue">
                        provisoire
                      </span>
                    )}
                  </td>
                  <td>{ligne.relecturesEnAttente}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
