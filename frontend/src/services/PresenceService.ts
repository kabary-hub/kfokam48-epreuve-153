import axios from 'axios';

export const PresenceService = {
  async marquerPresence(sessionId: number, etudiant: string, code: string) {
    const response = await axios.post(`/api/sessions/${sessionId}/presences`, null, {
      params: { etudiant, code }
    });
    return response.data;
  },
  async getPresence(sessionId: number, id: number) {
    const response = await axios.get(`/api/sessions/${sessionId}/presences/${id}`);
    return response.data;
  }
};
