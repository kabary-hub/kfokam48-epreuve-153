import axios from 'axios';

export const ManualPresenceService = {
  async ajouterManuel(sessionId: number, etudiant: string, code: string) {
    const response = await axios.post(`/api/sessions/${sessionId}/manual-presence`, null, {
      params: { etudiant, code }
    });
    return response.data;
  }
};
