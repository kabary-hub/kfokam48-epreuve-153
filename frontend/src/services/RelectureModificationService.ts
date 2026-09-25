import axios from 'axios';

export const RelectureModificationService = {
  async modifierRelecture(id: number, note: string, commentaire: string) {
    const response = await axios.put(`/api/relectures/${id}/modification`, null, {
      params: { note, commentaire }
    });
    return response.data;
  }
};
