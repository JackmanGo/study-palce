const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}
const formatTimeMany = () => {
	const date = new Date();
	const year = date.getFullYear()
	const month = date.getMonth() + 1
	const day = date.getDate()
	const weekDay = ['日', '一', '二', '三', '四', '五', '六'][date.getDay()]
	const hour = date.getHours()
	const minute = date.getMinutes()
	const second = date.getSeconds()
	return {
		date: [year, month, day].map(formatNumber).join('-'),
		time:[hour, minute, second].map(formatNumber).join(':'),
		week: '星期'+weekDay
	}
}


const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

module.exports = {
  formatTime: formatTime,
  formatTimeMany: formatTimeMany
}