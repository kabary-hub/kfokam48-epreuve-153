import React, { useEffect, useState } from 'react';
import { SchemaDataService } from '../services/SchemaDataService';

export const SchemaData: React.FC = () => {
  const [schema, setSchema] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadSchema();
  }, []);

  const loadSchema = async () => {
    try {
      const data = await SchemaDataService.getSchemaData();
      setSchema(data);
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
      <h2>Schéma de données</h2>
      {schema?.tables?.map((table: any) => (
        <div key={table.name} style={{ marginBottom: '20px' }}>
          <h3 style={{ color: '#333' }}>{table.name}</h3>
          <table style={{ borderCollapse: 'collapse', width: '100%' }}>
            <thead>
              <tr style={{ background: '#e9e9e9' }}>
                <th style={{ border: '1px solid #ccc', padding: '6px' }}>Colonne</th>
                <th style={{ border: '1px solid #ccc', padding: '6px' }}>Type</th>
                <th style={{ border: '1px solid #ccc', padding: '6px' }}>Contraintes</th>
              </tr>
            </thead>
            <tbody>
              {table.columns?.map((col: any) => (
                <tr key={col.name}>
                  <td style={{ border: '1px solid #ccc', padding: '6px' }}>{col.name}</td>
                  <td style={{ border: '1px solid #ccc', padding: '6px' }}>{col.type}</td>
                  <td style={{ border: '1px solid #ccc', padding: '6px' }}>{col.constraints}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ))}
    </div>
  );
};
