import React, { useState } from 'react';
import { RelecteurService } from '../services/RelecteurService';

interface RelecteurFormProps {
  sessionId: number;
  exerciceId: number;
  onRelecteurAffecte: (relecteur: any) => void;
}

export const RelecteurForm: React.FC<RelecteurFormProps> = ({ sessionId, exerciceId, onRelecteurAffecte }) => {
  const [etudiant, setEtudiant] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const relecteur = await RelecteurService.affecterRelecteur(sessionId, etudiant, exerciceId);
      setMessage('Relecteur affecté avec succès !');
      onRelecteurAffecte(relecteur);
      setEtudiant('');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors de l\'affectation');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Affecter un relecteur</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <div style={{ marginBottom: '10px' }}>
        <label>Étudiant:</label>
        <input
          type="text"
          value={etudiant}
          onChange={(e) => setEtudiant(e.target.value)}
          placeholder="Nom de l'étudiant relecteur"
          required
        />
      </div>
      <button type="submit">Affecter</button>
    </form>
  );
};
