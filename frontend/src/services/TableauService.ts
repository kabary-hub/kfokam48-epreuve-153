import axios from 'axios';

export const TableauService = {
  async getTableau() {
    const response = await axios.get('/api/sessions/tableau');
    return response.data;
  },
  async getTableauBySession(code: string) {
    const response = await axios.get(`/api/sessions/tableau/${code}`);
    return response.data;
  }
};
