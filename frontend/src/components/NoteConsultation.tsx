import React, { useEffect, useState } from 'react';
import { NoteService } from '../services/NoteService';

interface NoteConsultationProps {
  sessionId: number;
  etudiant: string;
}

export const NoteConsultation: React.FC<NoteConsultationProps> = ({ sessionId, etudiant }) => {
  const [note, setNote] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadNote();
  }, [sessionId, etudiant]);

  const loadNote = async () => {
    try {
      const data = await NoteService.getNote(sessionId, etudiant);
      setNote(data);
      setLoading(false);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du chargement');
      setLoading(false);
    }
  };

  if (loading) return <p style={{ textAlign: 'center' }}>Chargement...</p>;
  if (message) return <p style={{ color: 'red', textAlign: 'center' }}>{message}</p>;

  return (
    <div style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Votre note et commentaire</h2>
      <div style={{ marginBottom: '10px' }}>
        <label>Étudiant:</label>
        <p style={{ padding: '8px' }}>{note?.etudiant}</p>
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Code:</label>
        <p style={{ padding: '8px' }}>{note?.code}</p>
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Timestamp:</label>
        <p style={{ padding: '8px' }}>{note?.timestamp}</p>
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Commentaire:</label>
        <p style={{ padding: '8px', backgroundColor: '#f9f9f9' }}>{note?.commentaire}</p>
      </div>
    </div>
  );
};
