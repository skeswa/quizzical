'use strict';

var msgs = {
	'unknown_error': 'The request failed for unknown reasons.',
	'signup_success': 'You have successfully signed up.',
	'account_verified': 'Account is successfully verified.',
	'credentials_updated': 'Your credentials are successfully updated.',
	'reset_requested': 'Check your email to proceed your credential reset...',
};

function updateElement(name, val) {
	var el = document.getElementById(name)
	if (!el) {
		return
	}
	if (el.nodeName === 'INPUT') {
		el.value = val || ''
	} else {
		el.innerText = val || ''
	}
}

function bootstrapPage() {
	var queryDict = {};
	location.search.substr(1).split("&").forEach(function(item) {
	    var entries = item.split("=")
		queryDict[decodeURIComponent(entries[0])] = decodeURIComponent(entries[1] || '')
	})

	if (queryDict['msg']) {
		var msg = msgs[queryDict['msg']] || msgs['unknown_error']
		updateElement('error_msg', msg)
	}

	updateElement('change_accessToken', queryDict['accessToken'])
	updateElement('change_email', queryDict['email'])
	updateElement('verify_email', queryDict['email'])
}

function urlEncode(data) {
	var p, str = [];
    if (typeof data === "string") {
    	return data;
	}
    for (p in data) {
    	if (data.hasOwnProperty(p)) {
    		str.push(encodeURIComponent(p) + "=" + encodeURIComponent(data[p]));
    	}
	}
    return str.join('&')
}

function ajaxGet(url, callback) {
	var xhr = new XMLHttpRequest()
	xhr.open('GET', url)
    xhr.responseType = 'json'
	xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest')
	xhr.onload = callback.bind(xhr)
	xhr.send()
}

function ajaxPost(url, data, callback) {
	var xhr = new XMLHttpRequest()
	xhr.open('POST', url)
    xhr.responseType = 'json'
	xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest')
	xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded')
	xhr.onload = callback.bind(xhr)
	xhr.send(urlEncode(data))
}

function doSignUp() {
	var email = document.getElementById('signup_email').value || ''
	var pwd = document.getElementById('signup_pwd').value || ''

	ajaxGet('/account/rest/accounts/' + encodeURIComponent(email), function(e) {
    	if (this.response) {
    		alert('sign up failed, the email is probably already in use...')
    		return;
    	}

    	ajaxPost('/account/rest/signup', {email: email, password: pwd}, function(e) {
    		if (this.status !== 200) {
    			alert('sign up failed!')
    			return;
    		}

			if ('ACCOUNT_VERIFICATION_NEEDED' === this.response.state) {
				window.location = '/account/verify.html?email=' + encodeURIComponent(this.response.email)
			} else {
				window.location = '/auth/login.html?email=' + encodeURIComponent(this.response.email)
			}
    	})
	})
}

function doVerify() {
	var email = document.getElementById('verify_email').value || ''
	var token = document.getElementById('verify_token').value || ''

	ajaxGet('/account/rest/verify?' + urlEncode({accountId: email, verifyToken: token}), function(e) {
		if (this.status !== 200) {
    		alert('account verification failed!')
    		return;
    	}
		window.location = '/auth/login.html?email=' + encodeURIComponent(this.response.email);
	})
}

document.addEventListener("DOMContentLoaded", bootstrapPage);

// EOF