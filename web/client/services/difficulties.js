export const endpoint = '/api/difficulties';

export function get(id) {
  return fetch(endpoint + '/' + id, { method: 'GET' })
    .then(response => response.ok ?
      response.json() : Promise.reject(response.json()));
}

export function getAll() {
  return fetch(endpoint, { method: 'GET' })
    .then(response => response.ok ?
      response.json() : Promise.reject(response.json()));
}

export function create(payload) {
  return fetch(endpoint, { method: 'POST', body: JSON.stringify(payload) })
    .then(response => response.ok ?
      response.json() : Promise.reject(response.json()));
}

export function delete(id) {
  return fetch(endpoint + '/' + id, { method: 'POST' })
    .then(response => response.ok ?
      Promise.resolve({ id }) : Promise.reject(response.json()));
}
