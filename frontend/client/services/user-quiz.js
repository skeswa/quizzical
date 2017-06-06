import Network from 'utils/network'
import Session from 'utils/session'



const UserQuizService = {
  latest() {
    return Network.get('/api/quiz/submissions/latest')
  },
  recent() {
    return Network.get('/api/quiz/submissions/recent')
  },
  unsubmitted() {
    return Network.get('/api/quiz/submissions/unsubmitted')
  }
}

export default UserQuizService
