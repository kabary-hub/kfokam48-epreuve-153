import axios from 'axios';

export const RelectureService = {
  async demarrerRelecture(exerciceId: number) {
    const response = await axios.post(`/api/relectures`, null, { params: { exerciceId } });
    return response.data;
  },
  async rendreRelecture(exerciceId: number) {
    const response = await axios.put(`/api/relectures/${exerciceId}`);
    return response.data;
  }
};
