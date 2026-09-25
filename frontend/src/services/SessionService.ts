import axios from 'axios';

const API_URL = 'http://localhost:8080/api/sessions';

export const SessionService = {
  async ouvrirSession(data: { code: string; debut: Date; fin: Date }) {
    const response = await axios.post(API_URL, data);
    return response.data;
  },
  async clôturerSession(id: number) {
    const response = await axios.put(`${API_URL}/${id}/cloture`);
    return response.data;
  },
  async marquerPresence(sessionId: number, etudiant: string, code: string) {
    const response = await axios.post(`${API_URL}/${sessionId}/presence`, null, {
      params: { etudiant, code }
    });
    return response.data;
  },
  async getSessionByCode(code: string) {
    const response = await axios.get(`${API_URL}/${code}`);
    return response.data;
  },
  async getAllSessions() {
    const response = await axios.get(API_URL);
    return response.data;
  }
};
