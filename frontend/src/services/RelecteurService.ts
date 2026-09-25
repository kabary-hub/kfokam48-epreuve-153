import axios from 'axios';

export const RelecteurService = {
  async affecterRelecteur(sessionId: number, etudiant: string, exerciceId: number) {
    const response = await axios.post(`/api/sessions/${sessionId}/relecteurs`, null, {
      params: { etudiant, exerciceId }
    });
    return response.data;
  },
  async getRelecteur(sessionId: number, id: number) {
    const response = await axios.get(`/api/sessions/${sessionId}/relecteurs/${id}`);
    return response.data;
  }
};
