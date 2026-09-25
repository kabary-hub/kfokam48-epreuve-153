import React, { useState } from 'react';
import { RelectureService } from '../services/RelectureService';

interface RelectureFormProps {
  exerciceId: number;
  onRelectureDemarre: (relecture: any) => void;
}

export const RelectureForm: React.FC<RelectureFormProps> = ({ exerciceId, onRelectureDemarre }) => {
  const [message, setMessage] = useState('');

  const handleDemarrer = async () => {
    try {
      const relecture = await RelectureService.demarrerRelecture(exerciceId);
      setMessage('Relecture démarrée avec succès !');
      onRelectureDemarre(relecture);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du démarrage');
    }
  };

  const handleRendre = async () => {
    try {
      const relecture = await RelectureService.rendreRelecture(exerciceId);
      setMessage('Relecture rendue avec succès !');
      onRelectureDemarre(relecture);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du rendu');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Gérer la relecture</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <button onClick={handleDemarrer} style={{ marginRight: '10px' }}>
        Démarrer la relecture
      </button>
      <button onClick={handleRendre}>
        Rendre la relecture
      </button>
    </div>
  );
};
