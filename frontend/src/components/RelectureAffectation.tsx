import React, { useEffect, useState } from 'react';
import { RelectureAffectationService } from '../services/RelectureAffectationService';

interface RelectureAffectationProps {
  sessionId: number;
}

export const RelectureAffectation: React.FC<RelectureAffectationProps> = ({ sessionId }) => {
  const [affectations, setAffectations] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadAffectations();
  }, [sessionId]);

  const loadAffectations = async () => {
    try {
      const data = await RelectureAffectationService.getRelecturesAffectees(sessionId);
      setAffectations(data);
      setLoading(false);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du chargement');
      setLoading(false);
    }
  };

  if (loading) return <p style={{ textAlign: 'center' }}>Chargement...</p>;
  if (message) return <p style={{ color: 'red', textAlign: 'center' }}>{message}</p>;

  return (
    <div>
      <h2>Relectures affectées</h2>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {affectations.map((aff: any) => (
          <li key={aff.id} style={{ border: '1px solid #ccc', marginBottom: '10px', padding: '10px' }}>
            <strong>Étudiant:</strong> {aff.etudiant} <br />
            <strong>Exercice:</strong> {aff.exerciceId} <br />
            <strong>Lien:</strong> {aff.lienExercice}
          </li>
        ))}
      </ul>
    </div>
  );
};
