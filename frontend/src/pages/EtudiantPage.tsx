import { ExerciceForm } from '../components/ExerciceForm';
import { PresenceForm } from '../components/PresenceForm';

export function EtudiantPage() {
  return (
    <div className="page-etudiant">
      <h1>Espace Étudiant</h1>
      <PresenceForm />
      <ExerciceForm />
    </div>
  );
}
