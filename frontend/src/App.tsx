import { BrowserRouter, Link, Navigate, Route, Routes } from 'react-router-dom';
import { PresenceFormateurForm } from './components/PresenceFormateurForm';
import { SessionForm } from './components/SessionForm';
import { EtudiantPage } from './pages/EtudiantPage';
import { RelecteurPage } from './pages/RelecteurPage';

function App() {
  return (
    <BrowserRouter>
      <div className="app">
        <header>
          <h1>PresenceKF — Épreuve KFOKAM48</h1>
          <nav aria-label="Navigation principale">
            <Link to="/formateur">Formateur</Link>
            {' | '}
            <Link to="/etudiant">Étudiant</Link>
            {' | '}
            <Link to="/relecteur">Relecteur</Link>
          </nav>
        </header>
        <main>
          <Routes>
            <Route path="/" element={<Navigate to="/etudiant" replace />} />
            <Route
              path="/formateur"
              element={
                <div className="page-formateur">
                  <h1>Espace Formateur</h1>
                  <SessionForm />
                  <PresenceFormateurForm />
                </div>
              }
            />
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
