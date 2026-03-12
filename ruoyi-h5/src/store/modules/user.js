import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    userId: localStorage.getItem('userId') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
      setToken(token)
    },
    SET_USER_ID: (state, userId) => {
      state.userId = userId
      localStorage.setItem('userId', userId)
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    }
  },

  actions: {
    Login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        commit('SET_TOKEN', userInfo.token)
        commit('SET_USER_ID', userInfo.userId)
        commit('SET_USER_INFO', userInfo)
        resolve()
      })
    },
    
    LogOut({ commit }) {
      return new Promise((resolve, reject) => {
        commit('SET_TOKEN', '')
        commit('SET_USER_ID', '')
        commit('SET_USER_INFO', {})
        removeToken()
        localStorage.removeItem('userId')
        localStorage.removeItem('userInfo')
        resolve()
      })
    }
  }
}

export default user
