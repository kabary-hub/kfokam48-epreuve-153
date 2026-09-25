import React from 'react';
import { SessionForm } from './components/SessionForm';

function App() {
  const handleSessionCreated = (session: any) => {
    console.log('Session créée:', session);
  };

  return (
    <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
      <h1>PresenceKF - Gestion des sessions</h1>
      <SessionForm onSessionCreated={handleSessionCreated} />
    </div>
  );
}

export default App;
