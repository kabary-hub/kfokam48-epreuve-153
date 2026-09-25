import React, { useState } from 'react';
import { RelectureModificationService } from '../services/RelectureModificationService';

interface RelectureModificationFormProps {
  relectureId: number;
  onModificationEnregistree: (modification: any) => void;
}

export const RelectureModificationForm: React.FC<RelectureModificationFormProps> = ({ relectureId, onModificationEnregistree }) => {
  const [note, setNote] = useState('');
  const [commentaire, setCommentaire] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const modification = await RelectureModificationService.modifierRelecture(relectureId, note, commentaire);
      setMessage('Modification enregistrée avec succès !');
      onModificationEnregistree(modification);
      setNote('');
      setCommentaire('');
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors de la modification');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Modifier la relecture</h2>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      <div style={{ marginBottom: '10px' }}>
        <label>Note:</label>
        <input
          type="text"
          value={note}
          onChange={(e) => setNote(e.target.value)}
          placeholder="Note de la relecture"
          required
        />
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Commentaire:</label>
        <textarea
          value={commentaire}
          onChange={(e) => setCommentaire(e.target.value)}
          placeholder="Commentaire de la relecture"
          rows={4}
        />
      </div>
      <button type="submit">Enregistrer la modification</button>
    </form>
  );
};
