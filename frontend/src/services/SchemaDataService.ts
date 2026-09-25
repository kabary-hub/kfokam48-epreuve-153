import axios from 'axios';

export const SchemaDataService = {
  async getSchemaData() {
    const response = await axios.get('/api/schema-data');
    return response.data;
  }
};
