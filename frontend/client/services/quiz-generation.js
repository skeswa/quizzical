import Network from 'utils/network'
import { crudService } from './helpers/crud'

export default crudService(
  'quiz-generation',
  endpoint => ({
    whattype: payload => Network.get(`${endpoint}/whattype`, payload),
  }))
