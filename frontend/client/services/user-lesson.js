import Network from 'utils/network'
import Session from 'utils/session'



const UserLessonService = {
  current() {
    return Network.get('/api/user/lesson/current')
  },
  upcoming() {
    return Network.get('/api/user/lesson/upcoming')
  },
  finished() {
    return Network.get('/api/user/lesson/finished')
  }
}

export default UserLessonService
