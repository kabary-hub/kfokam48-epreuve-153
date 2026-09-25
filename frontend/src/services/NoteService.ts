import axios from 'axios';

export const NoteService = {
  async getNote(sessionId: number, etudiant: string) {
    const response = await axios.get(`/api/sessions/${sessionId}/notes/${etudiant}`);
    return response.data;
  }
};
