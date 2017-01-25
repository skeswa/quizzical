'use strict';

var msgs = {
	'unknown_error': 'The request failed for unknown reasons.',
	'signup_success': 'You have successfully signed up.',
	'account_verified': 'Account is successfully verified.',
	'credentials_updated': 'Your credentials are successfully updated.',
	'reset_requested': 'Check your email to proceed your credential reset...',
};

function showErrorMessage() {
	var queryDict = {};
	location.search.substr(1).split("&").forEach(function(item) {
		queryDict[item.split("=")[0]] = item.split("=")[1]
	})

	var el = document.getElementById('error_msg')
	var err = queryDict['msg']
	if (err != undefined) {
		el.innerText = msgs[err] || msgs['unknown_error']
		el.style.display = 'block'
	} else {
		el.innerText = ''
		el.style.display = 'none'
	}
}

function updateElement(name, val) {
	var el = document.getElementById(name)
	if (!el) {
		return
	}
	if (el.nodeName === 'INPUT') {
		el.value = decodeURIComponent(val || '')
	} else {
		el.innerText = decodeURIComponent(val || '')
	}
}

function fillPageDetails(profile) {
	if (!profile) {
		return
	}
	updateElement('change_pwd_email', profile.email)
	updateElement('userName', profile.name)
}

function bootstrapPage() {
	var queryDict = {};
	location.search.substr(1).split("&").forEach(function(item) {
		queryDict[item.split("=")[0]] = item.split("=")[1]
	})

	if (queryDict['msg']) {
		var msg = msgs[queryDict['msg']] || msgs['unknown_error']
		updateElement('error_msg', msg)
	}

	var xhr = new XMLHttpRequest()
	xhr.open('GET', '/rest/whoami', true)
    xhr.responseType = 'json'
    xhr.onload = function(e) {
        if (this.status != 200) {
            console.log("failed to obtain login state! Status = " + this.status)
        } else {
            fillPageDetails(this.response)
        }
    }
    xhr.send()
}

document.addEventListener("DOMContentLoaded", bootstrapPage);

// EOF