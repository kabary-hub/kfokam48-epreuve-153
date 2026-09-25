import React, { useState } from 'react';
import { ExerciceService } from '../services/ExerciceService';

interface ExerciceFormProps {
  sessionId: number;
  onExerciceDeposited: (exercice: any) => void;
}

export const ExerciceForm: React.FC<ExerciceFormProps> = ({ sessionId, onExerciceDeposited }) => {
  const [lien, setLien] = useState('');
  const [etudiant, setEtudiant] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const exercice = await ExerciceService.deposerExercice(sessionId, lien, etudiant);
      setMessage('Exercice déposé avec succès !');
      onExerciceDeposited(exercice);
      setLien('');
      setEtudiant('');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du dépôt');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Déposer un lien d'exercice</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <div style={{ marginBottom: '10px' }}>
        <label>Lien:</label>
        <input
          type="url"
          value={lien}
          onChange={(e) => setLien(e.target.value)}
          placeholder="https://..."
          required
        />
      </div>
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
      <button type="submit">Déposer l'exercice</button>
    </form>
  );
};
