import React, { useEffect, useState } from 'react';
import { RèglesService } from '../services/RèglesService';

export const Règles: React.FC = () => {
  const [règles, setRègles] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadRègles();
  }, []);

  const loadRègles = async () => {
    try {
      const data = await RèglesService.getAllRègles();
      setRègles(data);
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
      <h2>Règles de gestion</h2>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {règles.map((règle: any) => (
          <li key={règle.id} style={{ border: '1px solid #ccc', marginBottom: '10px', padding: '10px' }}>
            <strong>{règle.id}:</strong> {règle.description}
          </li>
        ))}
      </ul>
    </div>
  );
};
