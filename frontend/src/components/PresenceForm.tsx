import React, { useState } from 'react';
import { PresenceService } from '../services/PresenceService';

interface PresenceFormProps {
  sessionId: number;
  onPresenceMarked: (presence: any) => void;
}

export const PresenceForm: React.FC<PresenceFormProps> = ({ sessionId, onPresenceMarked }) => {
  const [etudiant, setEtudiant] = useState('');
  const [code, setCode] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const presence = await PresenceService.marquerPresence(sessionId, etudiant, code);
      setMessage('Présence marquée avec succès !');
      onPresenceMarked(presence);
      setEtudiant('');
      setCode('');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du marquage');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Marquer une présence</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <div style={{ marginBottom: '10px' }}>
        <label>Étudiant:</label>
        <input
          type="text"
          value={etudiant}
          onChange={(e) => setEtudiant(e.target.value)}
          placeholder="Nom de l'étudiant"
          required
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Code de présence:</label>
        <input
          type="text"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          placeholder="Code de la session"
          required
        />
      </div>
      <button type="submit">Marquer la présence</button>
    </form>
  );
};
