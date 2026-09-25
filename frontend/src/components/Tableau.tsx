import React, { useEffect, useState } from 'react';
import { TableauService } from '../services/TableauService';

export const Tableau: React.FC = () => {
  const [sessions, setSessions] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadTableau();
  }, []);

  const loadTableau = async () => {
    try {
      const data = await TableauService.getTableau();
      setSessions(data);
      setLoading(false);
    } catch (error: any) {
      setMessage(error.response?.data?.message || 'Erreur lors du chargement');
      setLoading(false);
    }
  };

  if (loading) return <p style={{ textAlign: 'center' }}>Chargement...</p>;
  if (message) return <p style={{ color: 'red', textAlign: 'center' }}>{message}</p>;

  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr style={{ background: '#f0f0f0' }}>
            <th style={{ border: '1px solid #ccc', padding: '8px' }}>Session</th>
            <th style={{ border: '1px solid #ccc', padding: '8px' }}>Statut</th>
            <th style={{ border: '1px solid #ccc', padding: '8px' }}>Présences</th>
            <th style={{ border: '1px solid #ccc', padding: '8px' }}>Exercices</th>
          </tr>
        </thead>
        <tbody>
          {sessions.map((session: any) => (
            <tr key={session.id}>
              <td style={{ border: '1px solid #ccc', padding: '8px' }}>{session.code}</td>
              <td style={{ border: '1px solid #ccc', padding: '8px' }}>{session.ouverte ? 'Ouverte' : 'Fermée'}</td>
              <td style={{ border: '1px solid #ccc', padding: '8px' }}>{session.presences?.length || 0}</td>
              <td style={{ border: '1px solid #ccc', padding: '8px' }}>{session.exercices?.length || 0}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
