
import Network from 'utils/network'
import { crudService } from './helpers/crud'

export default crudService(
  'quiz',
  endpoint => ({
    generate: payload => Network.post(`${endpoint}/generate`, payload),
  }))
