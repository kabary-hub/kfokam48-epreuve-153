import React, { useEffect, useState } from 'react';
import { AutoClotureService } from '../services/AutoClotureService';

interface AutoClotureProps {
  sessionId: number;
}

export const AutoCloture: React.FC<AutoClotureProps> = ({ sessionId }) => {
  const [status, setStatus] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadStatus();
  }, [sessionId]);

  const loadStatus = async () => {
    try {
      const data = await AutoClotureService.getStatus(sessionId);
      setStatus(data);
      setLoading(false);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du chargement');
      setLoading(false);
    }
  };

  const handleClôturer = async () => {
    try {
      const data = await AutoClotureService.clôturer(sessionId);
      setStatus(data);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors de la clôture');
    }
  };

  if (loading) return <p style={{ textAlign: 'center' }}>Chargement...</p>;
  if (message) return <p style={{ color: 'red', textAlign: 'center' }}>{message}</p>;

  return (
    <div style={{ maxWidth: '400px', margin: '0 auto' }}>
      <h2>Auto-clôture de session</h2>
      <div style={{ marginBottom: '10px' }}>
        <label>Statut:</label>
        <p style={{ padding: '8px', backgroundColor: status?.ouverte ? '#e6ffe6' : '#ffe6e6' }}>
          {status?.ouverte ? 'Ouverte' : 'Fermée'}
        </p>
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Fin prévue:</label>
        <p style={{ padding: '8px' }}>{status?.fin}</p>
      </div>
      <div style={{ marginBottom: '10px' }}>
        <label>Expirée:</label>
        <p style={{ padding: '8px', color: status?.expirée ? 'red' : 'green' }}>
          {status?.expirée ? 'Oui' : 'Non'}
        </p>
      </div>
      {status?.ouverte && (
        <button onClick={handleClôturer} style={{ backgroundColor: '#ff4444', color: 'white', padding: '10px 20px' }}>
          Fermer la session
        </button>
      )}
    </div>
  );
};
