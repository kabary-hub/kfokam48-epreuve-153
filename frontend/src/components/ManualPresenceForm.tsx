import React, { useState } from 'react';
import { ManualPresenceService } from '../services/ManualPresenceService';

interface ManualPresenceFormProps {
  sessionId: number;
  onPresenceAdded: (presence: any) => void;
}

export const ManualPresenceForm: React.FC<ManualPresenceFormProps> = ({ sessionId, onPresenceAdded }) => {
  const [etudiant, setEtudiant] = useState('');
  const [code, setCode] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const presence = await ManualPresenceService.ajouterManuel(sessionId, etudiant, code);
      setMessage('Présence ajoutée manuellement avec succès !');
      onPresenceAdded(presence);
      setEtudiant('');
      setCode('');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors de l\'ajout manuel');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Ajouter une présence manuellement</h2>
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
        <label>Code:</label>
        <input
          type="text"
          value={code}
          onChange={(e) => setCode(e.target.value)}
          placeholder="Code de la session"
          required
        />
      </div>
      <button type="submit">Ajouter manuellement</button>
    </form>
  );
};
