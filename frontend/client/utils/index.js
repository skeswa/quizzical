
const hexRegex = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i

function twoDigitify(n) {
  return n > 9 ? '' + n: '0' + n
}

function hexToRGB(hex) {
  const result = hexRegex.exec(hex)
  return result
    ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16),
    }
    : null
}

export function textColorForBackground(bgHex) {
  const bgRGB = hexToRGB(bgHex)
  const luminosity = Math.round(
    (
      (parseInt(bgRGB.r) * 299)
      + (parseInt(bgRGB.g) * 587)
      + (parseInt(bgRGB.b) * 114)
    ) / 1000
  )

  return luminosity > 125
    ? '#000'
    : '#fff'
}

export function pictureIdToBackgroundURL(pictureId) {
  return `/api/problems/pictures/${pictureId}`
}

export function timeSince(dateString) {
  const date = new Date(dateString)
  const seconds = Math.floor((new Date() - date) / 1000);
  let interval = Math.floor(seconds / 31536000);

  if (interval > 1) {
    return interval + ' years';
  }
  interval = Math.floor(seconds / 2592000)
  if (interval > 1) {
    return interval + ' months';
  }
  interval = Math.floor(seconds / 86400)
  if (interval > 1) {
    return interval + ' days';
  }
  interval = Math.floor(seconds / 3600)
  if (interval > 1) {
    return interval + ' hours';
  }
  interval = Math.floor(seconds / 60)
  if (interval > 1) {
    return interval + ' minutes'
  }

  return Math.floor(seconds) + ' seconds'
}

export function formatDateCreated(dateString) {
  const date = new Date(dateString)
  let time = date.getHours() > 11
    ? `${twoDigitify(date.getHours() > 12 ? (date.getHours() - 12) : 12)}:${twoDigitify(date.getMinutes())} PM`
    : `${twoDigitify(date.getHours())}:${twoDigitify(date.getMinutes())} AM`

  return `${date.getMonth() + 1}/${date.getDate()}/${date.getYear() - 100} at ${time}`
}
