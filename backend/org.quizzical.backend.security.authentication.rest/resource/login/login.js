'use strict';

var msgs = {
	'unknown_error': 'The request failed for unknown reasons.',
	'account_verified': 'Your account is successfully verified. You can now proceed your login...',
	'credentials_updated': 'Your credentials are successfully updated. You can now proceed your login...',
	'logged_out': 'You are successfully logged out.',
	'invalid_request': 'The request is missing a required parameter, includes an invalid parameter value, includes a parameter more than once, or is otherwise malformed.',
	'unauthorized_client': 'The client is not authorized to request an authorization code using this method.',
	'access_denied': 'The resource owner or authorization server denied the request.',
	'unsupported_response_type': 'The authorization server does not support obtaining an authorization code using this method.',
	'invalid_scope': 'The requested scope is invalid, unknown, or malformed.',
	'server_error': 'The authorization server encountered an unexpected condition that prevented it from fulfilling the request.',
	'temporarily_unavailable': 'The authorization server is currently unable to handle the request due to a temporary overloading or maintenance of the server.',
	'interaction_required': 'The Authorization Server requires End-User interaction of some form to proceed.',
	'login_required': 'The Authorization Server requires End-User authentication.',
	'account_selection_required': 'The End-User is REQUIRED to select a session at the Authorization Server.',
	'consent_required': 'The Authorization Server requires End-User consent.',
	'invalid_request_uri': 'The request_uri in the Authorization Request returns an error or contains invalid data.',
	'invalid_request_object': 'The request parameter contains an invalid Request Object.',
	'request_not_supported': 'The OP does not support use of the request parameter.',
	'request_uri_not_supported': 'The OP does not support use of the request_uri parameter.',
	'registration_not_supported': 'The OP does not support use of the registration parameter.',
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

function setUpIdProvider(provider) {
	var el = document.getElementById('auth_provider_' + provider)
	if (!el) {
		el = document.getElementById('auth_provider_ext').cloneNode(true)
		el.id = 'auth_provider_' + provider

		var child = el.children[0]
		child.src = 'icon_' + provider + '.svg'
		child.alt = 'Log in using ' + provider

		child = el.children[1]
		child.value = provider
		document.getElementById('auth_providers').appendChild(el)
	}
	el.style.display = 'inline'
}

function setUpIdProviders(providers) {
	if (providers) {
		providers.sort().forEach(setUpIdProvider)
	}
}

function ajaxGet(url, callback) {
	var xhr = new XMLHttpRequest()
	xhr.open('GET', url)
    xhr.responseType = 'json'
	xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest')
	xhr.onload = callback.bind(xhr)
	xhr.send()
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
	updateElement('local_email', queryDict['email'])

	ajaxGet('/auth/rest/login/providers', function(e) {
        if (this.status != 200) {
            console.log("failed to obtain login providers! Status = " + this.status)
        } else {
            setUpIdProviders(this.response)
        }
	})
}

document.addEventListener("DOMContentLoaded", bootstrapPage);

// EOF