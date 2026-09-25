import axios from 'axios';

export const RèglesService = {
  async getAllRègles() {
    const response = await axios.get('/api/règles');
    return response.data;
  },
  async getRègle(id: number) {
    const response = await axios.get(`/api/règles/${id}`);
    return response.data;
  }
};
