
import pluralize from 'pluralize'

const ENTITY_SPLIT_REGEX = /[ \-_\/]/

function partIsLast(parts, i) {
  return i === parts.length - 1
}

function lowerCaser(part) {
  return part.toLowerCase()
}

function capitalizer(part) {
  return part.charAt(0).toUpperCase() + part.slice(1)
}

function pluralizer(part, i, parts) {
  if (partIsLast(parts, i)) return pluralize(part)
  return part
}

export function endpointSuffixFor(entity) {
  return entity
    .split(ENTITY_SPLIT_REGEX)
    .map(lowerCaser)
    .map(pluralizer)
    .join('/')
}

export function normalizeEntity(entity) {
  return entity
    .split(ENTITY_SPLIT_REGEX)
    .map(lowerCaser)
    .join(' ')
}

export function pluralizeNormalizedEntity(normalizedEntity) {
  return normalizedEntity
    .split(ENTITY_SPLIT_REGEX)
    .map(pluralizer)
    .join(' ')
}

export function camelizeNormalizedEntity(normalizedEntity) {
  return normalizedEntity
    .split(ENTITY_SPLIT_REGEX)
    .map(capitalizer)
    .join('')
}
