import React, { useState } from 'react';
import { SessionService } from '../services/SessionService';

interface SessionFormProps {
  onSessionCreated: (session: any) => void;
}

export const SessionForm: React.FC<SessionFormProps> = ({ onSessionCreated }) => {
  const [code, setCode] = useState('');
  const [debut, setDebut] = useState('');
  const [fin, setFin] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const session = await SessionService.ouvrirSession({
        code,
        debut: new Date(debut),
        fin: new Date(fin)
      });
      setMessage('Session créée avec succès !');
      onSessionCreated(session);
      setCode('');
      setDebut('');
      setFin('');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors de la création');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Ouvrir une session</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <div style={{ marginBottom: '10px' }}>
        <label>Code de la session:</label>
        <input
          type="text"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          placeholder="Ex: S2024-001"
          required
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Début:</label>
        <input
          type="datetime-local"
          value={debut}
          onChange={(e) => setDebut(e.target.value)}
          required
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Fin:</label>
        <input
          type="datetime-local"
          value={fin}
          onChange={(e) => setFin(e.target.value)}
          required
        />
      </div>
      <button type="submit">Créer la session</button>
    </form>
  );
};
