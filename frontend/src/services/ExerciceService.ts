import axios from 'axios';

export const ExerciceService = {
  async deposerExercice(sessionId: number, lien: string, etudiant: string) {
    const response = await axios.post(`/api/sessions/${sessionId}/exercices`, null, {
      params: { lien, etudiant }
    });
    return response.data;
  },
  async getExercice(sessionId: number, id: number) {
    const response = await axios.get(`/api/sessions/${sessionId}/exercices/${id}`);
    return response.data;
  }
};
