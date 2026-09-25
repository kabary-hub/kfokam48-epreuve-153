import axios from 'axios';

export const RelectureAffectationService = {
  async getRelecturesAffectees(sessionId: number) {
    const response = await axios.get(`/api/sessions/${sessionId}/relectures-affectees`);
    return response.data;
  }
};
