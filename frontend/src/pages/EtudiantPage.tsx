import { ExerciceForm } from '../components/ExerciceForm';
import { PresenceForm } from '../components/PresenceForm';

export function EtudiantPage() {
  return (
    <div>
      <h1 className="page-title">Espace Étudiant</h1>
      <div className="card-grid">
        <PresenceForm />
        <ExerciceForm />
      </div>
    </div>
  );
}
