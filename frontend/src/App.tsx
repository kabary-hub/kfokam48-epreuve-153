import { BrowserRouter, Link, Navigate, Route, Routes } from 'react-router-dom';
import { PresenceFormateurForm } from './components/PresenceFormateurForm';
import { SessionForm } from './components/SessionForm';
import { TableauFormateur } from './components/TableauFormateur';
import { EtudiantPage } from './pages/EtudiantPage';
import { RelecteurPage } from './pages/RelecteurPage';

function FormateurPage() {
  return (
    <div>
      <h1 className="page-title">Espace Formateur</h1>
      <div className="card-grid">
        <SessionForm />
        <PresenceFormateurForm />
      </div>
      <div className="tableau-section">
        <TableauFormateur />
      </div>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div className="app">
        <header className="app-header">
          <h1>PresenceKF — Épreuve KFOKAM48</h1>
          <nav className="app-nav" aria-label="Navigation principale">
            <Link to="/formateur">Formateur</Link>
            <Link to="/etudiant">Étudiant</Link>
            <Link to="/relecteur">Relecteur</Link>
          </nav>
        </header>
        <main className="app-main">
          <Routes>
            <Route path="/" element={<Navigate to="/etudiant" replace />} />
            <Route path="/formateur" element={<FormateurPage />} />
            <Route path="/etudiant" element={<EtudiantPage />} />
            <Route path="/relecteur" element={<RelecteurPage />} />
            <Route path="*" element={<Navigate to="/etudiant" replace />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
