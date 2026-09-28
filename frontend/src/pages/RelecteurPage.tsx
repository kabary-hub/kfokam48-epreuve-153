import { RelectureForm } from '../components/RelectureForm';

export function RelecteurPage() {
  return (
    <div>
      <h1 className="page-title">Espace Relecteur</h1>
      <div className="card-grid">
        <RelectureForm />
      </div>
    </div>
  );
}
