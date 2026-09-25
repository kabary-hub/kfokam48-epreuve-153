import axios from 'axios';

export const AutoClotureService = {
  async clôturer(id: number) {
    const response = await axios.put(`/api/sessions/${id}/auto-cloture`);
    return response.data;
  },
  async getStatus(id: number) {
    const response = await axios.get(`/api/sessions/${id}/auto-cloture`);
    return response.data;
  }
};
